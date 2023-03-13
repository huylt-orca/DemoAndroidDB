package com.example.demobe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demobe.adapter.UserAdapter;
import com.example.demobe.database.UserDatabase;
import com.example.demobe.model.User;
import com.example.demobe.provider.UserContProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 10;
    private EditText edtUsername;
    private EditText edtAddress;
    private Button btnAddress;
    private RecyclerView rcvUser;
    private TextView tvDeleteAll;
    private EditText edtSearch;

    private UserAdapter userAdapter;
    private List<User> mListUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();

        userAdapter = new UserAdapter(new UserAdapter.IClickItemUser() {
            @Override
            public void updateUser(User user) {
                clickUpdateUser(user);
            }

            @Override
            public void deleteUser(User user) {
                clickDeleteUser(user);
            }


        });
        mListUser = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvUser.setLayoutManager(linearLayoutManager);

        rcvUser.setAdapter(userAdapter);

        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });

        tvDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDeleteAllUser();
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH){
                    handleSearchUser();
                }
                return false;
            }
        });

        loadData();


        PackageManager pm = getPackageManager();
        ProviderInfo pi = pm.resolveContentProvider(UserContProvider.URI_TABLE_USER, 0);
        if (pi != null) {
            Toast.makeText(MainActivity.this,"publish",Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(MainActivity.this,"private",Toast.LENGTH_SHORT).show();

        }
    }

    private void handleSearchUser() {
        String strKeywords =  edtSearch.getText().toString().trim();
        mListUser.clear();
//        Cursor cursor = UserDatabase.getInstance(this).userDAO().searchUser(strKeywords);
        ContentResolver contentResolver = getContentResolver();
        Uri uri = Uri.parse(UserContProvider.URI_TABLE_USER + "/" +strKeywords);
        Cursor cursor = contentResolver.query(uri,null,null,null,null);

        mListUser = User.ConvertCursorToListUser(cursor);

        userAdapter.setDate(mListUser);
        hideSoftKeyboard();
    }

    private void clickDeleteAllUser() {
        new AlertDialog.Builder(this)
                .setTitle("Cofirm delete all user")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Delete User
//                        UserDatabase.getInstance(MainActivity.this).userDAO().deleteAllUser();
                        ContentResolver contentResolver = getContentResolver();
                        Uri uri = Uri.parse(UserContProvider.URI_TABLE_USER);
                        contentResolver.delete(uri,null);

                        Toast.makeText(MainActivity.this,"Delete All User Successfully",Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void clickDeleteUser(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Cofirm delete user")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Delete User
//                        UserDatabase.getInstance(MainActivity.this).userDAO().deleteUser(user);
                        ContentResolver contentResolver = getContentResolver();
                        Uri uri = Uri.parse(UserContProvider.URI_TABLE_USER + "/" +user.getId());
//                        String selection = "id=?";
//                        String[] selectionArgs = new String[]{String.valueOf(user.getId())};

                        int rowsDeleted = getContentResolver().delete(uri, null, null);



                        Toast.makeText(MainActivity.this,"Delete User Successfully",Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

    private  void initUi(){
        edtUsername = findViewById(R.id.edit_username);
        edtAddress = findViewById(R.id.edit_address);
        btnAddress = findViewById(R.id.btn_add_user);
        rcvUser = findViewById(R.id.rcv_user);
        tvDeleteAll = findViewById(R.id.tv_delete_all);
        edtSearch = findViewById(R.id.edt_search);
    }

    private void addUser(){
        String strUsername = edtUsername.getText().toString().trim();
        String strAddress = edtAddress.getText().toString().trim();
        if (TextUtils.isEmpty(strUsername) || TextUtils.isEmpty(strAddress)){
            return;
        }
        User user = new User(strUsername, strAddress);

        if (isUserExist(user)){
            Toast.makeText(this, "User is exist", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentResolver contentResolver = getContentResolver();
        Uri uri = Uri.parse(UserContProvider.URI_TABLE_USER);

        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("address", user.getAddress());

        contentResolver.insert(uri,values);
//        UserDatabase.getInstance(this).userDAO().inserUser(user);
        Toast.makeText(this, "Add User Successfull", Toast.LENGTH_SHORT).show();

        edtUsername.setText("");
        edtAddress.setText("");
        hideSoftKeyboard();

        loadData();
    }

    public void hideSoftKeyboard(){
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }
    private void loadData(){
        ContentResolver contentResolver = getContentResolver();
        Uri uri = Uri.parse(UserContProvider.URI_TABLE_USER);
        Cursor cursor = contentResolver.query(uri,null,null,null,null);
//        Cursor cursor = UserDatabase.getInstance(this).userDAO().getListUser();
        mListUser = User.ConvertCursorToListUser(cursor);
        userAdapter.setDate(mListUser);
    }

    private boolean isUserExist (User user){
        Cursor cursor = UserDatabase.getInstance(this).userDAO().checkUser(user.getUsername());

        return cursor.getCount() != 0;
    }

    private void clickUpdateUser(User user) {
        Intent intent = new Intent(MainActivity.this,UpdateUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_user",user);
        intent.putExtras(bundle);
        startActivityForResult(intent,MY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            loadData();
        }
    }
}