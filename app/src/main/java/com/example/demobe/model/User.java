package com.example.demobe.model;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = User.TABLE_NAME)
public class User implements Serializable {

    public static final String TABLE_NAME = "user";

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name="username")
    private String username;
    private String address;


    public User(int id, String username, String address) {
        this.id = id;
        this.username = username;
        this.address = address;
    }

    @Ignore
    public User() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Ignore
    public User(String username, String address) {
        this.username = username;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static User ConvertCursorToUser(Cursor cursor){
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            return new User(id,username,address);
        }
        return null;
    }

    public static List<User> ConvertCursorToListUser(Cursor cursor){
        List<User> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                if (idIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                    String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                    list.add(new User(id,username,address));
                }
            } while (cursor.moveToNext());
        }
        return list;
    }

    public static User fromContentValues(ContentValues values) {
        final User user = new User();
        if (values.containsKey("id")) {
            user.id = values.getAsInteger("id");
        }
        if (values.containsKey("username")) {
            user.username = values.getAsString("username");
        }
        if (values.containsKey("address")) {
            user.address = values.getAsString("address");
        }
        return user;
    }

}
