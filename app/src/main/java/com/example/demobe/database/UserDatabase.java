package com.example.demobe.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.demobe.DAO.UserDAO;
import com.example.demobe.model.User;

@Database(entities = {User.class},version = 1)
public abstract class UserDatabase extends RoomDatabase {

    // Them nhieu cot
    // ALTER TABLE ten_bang ADD cot1 type_cot1, cot2 type_cot2

    // Change type of column
    // ALTER TABLE ten_bang ALTER COLUMN ten_cot type;

    // Delete colun
    // ALTER TABLE ten_bang DROP COLUMN ten_cot


//    static Migration migration =  new Migration(1,2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("Alter TAble user Add Column birthday Text");
//        }
//    };

    private static final String DATABASE_NAME ="user.db";
    private  static  UserDatabase instance;


    public static synchronized UserDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    UserDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
//                    .addMigrations(migration)
                    .build();
        }
        return instance;
    }

    public abstract UserDAO userDAO();
}
