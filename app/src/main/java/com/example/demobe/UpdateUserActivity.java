package com.example.demobe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demobe.database.UserDatabase;
import com.example.demobe.model.User;

public class UpdateUserActivity extends AppCompatActivity {
    private EditText edtUsername;
    private EditText edtAddress;
    private Button btnUpdateUser;

    private User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        edtUsername = findViewById(R.id.edit_username);
        edtAddress = findViewById(R.id.edit_address);
        btnUpdateUser = findViewById(R.id.btn_update_user);

        mUser = (User) getIntent().getExtras().get("object_user");
        if (mUser != null){
            edtUsername.setText(mUser.getUsername());
            edtAddress.setText(mUser.getAddress());
        }

        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });
    }

    private void updateUser() {
        String strUsername = edtUsername.getText().toString().trim();
        String strAddress = edtAddress.getText().toString().trim();
        if (TextUtils.isEmpty(strUsername) || TextUtils.isEmpty(strAddress)){
            return;
        }

        mUser.setUsername(strUsername);
        mUser.setAddress(strAddress);

        UserDatabase.getInstance(this).userDAO().updateUser(mUser);

//        ContentResolver contentResolver = getContentResolver();
//        Uri uri = Uri.parse("content://com.example.demobe.UserProvider/user");
//
//        ContentValues values = new ContentValues();
//        values.put("id", mUser.getId());
//        values.put("username",mUser.getUsername());
//        values.put("address",mUser.getAddress());
//
//        int rowsUpdated = contentResolver.update(uri, values, null, null);

        Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}