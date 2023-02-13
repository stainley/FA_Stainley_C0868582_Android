package com.stainley.fa.android.util;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.stainley.fa.android.data.ProductDao;
import com.stainley.fa.android.model.Product;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class ProductRoomDatabase extends RoomDatabase {

    public abstract ProductDao productDao();

    private static volatile ProductRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    // executor service helps to do tasks in background thread
    public static final ExecutorService databaseWriteExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ProductRoomDatabase getInstance(final Application context) {
        if (INSTANCE == null) {
            synchronized (ProductRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ProductRoomDatabase.class,
                                    "PRODUCT_DATABASE")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
