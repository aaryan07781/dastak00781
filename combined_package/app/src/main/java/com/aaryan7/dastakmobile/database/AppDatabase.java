package com.aaryan7.dastakmobile.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Product.class, BillItem.class, Bill.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract ProductDao productDao();
    public abstract BillDao billDao();
    public abstract BillItemDao billItemDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "dastak_mobile_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
