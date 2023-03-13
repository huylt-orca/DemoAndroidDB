package com.example.demobe.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.demobe.DAO.UserDAO;
import com.example.demobe.database.UserDatabase;
import com.example.demobe.model.User;

public class UserContProvider extends ContentProvider {

    public static String AUTHOR ="com.example.demobe.provider.UserContProvider";
    public static String TABLE_USER = "user";
    public static String URI_TABLE_USER = "content://" + AUTHOR + "/" +TABLE_USER;
    public static UriMatcher uriMatcher ;
    UserDAO userDAO;

    @Override
    public boolean onCreate() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHOR,TABLE_USER,1);
        uriMatcher.addURI(AUTHOR,TABLE_USER+"/#",2);
        uriMatcher.addURI(AUTHOR,TABLE_USER+"/*",3);
        final Context context = getContext();
        if (context == null){
            return  false;
        }
        userDAO = UserDatabase.getInstance(context).userDAO();
        return true;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        int matcher = uriMatcher.match(uri);

        Cursor cursor = null;

        switch (matcher){
            case 1:
                cursor = userDAO.getListUser();
                break;
            case 2:

                break;
            case 3:
                String name = uri.getLastPathSegment();
                cursor = userDAO.searchUser(name);
                break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch(uriMatcher.match(uri)){
            case 1:
                return "vnd.android.cursor.dir/com.example.demobe.UserContProvider";
            case 2:
                return "vnd.android.cursor.item/com.example.demobe.UserContProvider";
            case 3:
                return "vnd.android.cursor.dir/com.example.demobe.UserContProvider";
            default:
                throw  new IllegalArgumentException("Unknow URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        if (contentValues != null){
            userDAO.inserUser(User.fromContentValues(contentValues));
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int matcher = uriMatcher.match(uri);
        switch (matcher){
            case 1:
                userDAO.deleteAllUser();
                break;
            case 2:
                int userId = Integer.parseInt(uri.getLastPathSegment());
                userDAO.deleteUserById(userId);
                break;
            case 3:
                break;
        }

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int matcher = uriMatcher.match(uri);
//        int userId = Integer.parseInt(uri.getLastPathSegment());
        userDAO.updateUser(User.fromContentValues(contentValues));
        return 0;
    }


}
