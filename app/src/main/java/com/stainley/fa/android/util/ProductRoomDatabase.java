package com.stainley.fa.android.util;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.stainley.fa.android.data.ProductDao;
import com.stainley.fa.android.model.Location;
import com.stainley.fa.android.model.Product;

import java.util.Arrays;
import java.util.List;
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
                            .addCallback(addDataFromDB)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDataFromDB =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                    databaseWriteExecutor.execute(() -> {
                        ProductDao productDao = INSTANCE.productDao();
                        Location torontoLocation = new Location();
                        torontoLocation.setLatitude(43.6532);
                        torontoLocation.setLongitude(-79.3832);

                        List<Product> products = Arrays.asList(
                                new Product("Rice", "Long grain", 6.55, torontoLocation),
                                new Product("Tomatoes", "Tomatoes from DR", 1.0, torontoLocation),
                                new Product("Tuna", "Light water", 3.62, torontoLocation),
                                new Product("Pita Pizza", "Greek version", 1.12, torontoLocation),
                                new Product("Chocolate", "Tim Holton Black", 3.55, torontoLocation),
                                new Product("Cereal", "For Baby", 14.55, torontoLocation),
                                new Product("Shampoo", "Jonson and Joson", 14.15, torontoLocation),
                                new Product("MacBook", "Macbook Pro 14", 1400.99, torontoLocation),
                                new Product("iPad", "iPad Pro 11", 899.99, torontoLocation),
                                new Product("Android Phone", "Pixel Version", 577.88, torontoLocation));

                        products.forEach(product -> {
                            databaseWriteExecutor.execute(() -> productDao.save(product));
                        });

                    });
                }
            };


}
