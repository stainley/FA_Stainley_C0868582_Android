package com.stainley.fa.android.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.stainley.fa.android.model.Product;
import com.stainley.fa.android.util.ProductRoomDatabase;

import java.util.List;

public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(Application application) {
        ProductRoomDatabase db = ProductRoomDatabase.getInstance(application);
        productDao = db.productDao();
    }

    public void saveProduct(Product product) {
        ProductRoomDatabase.databaseWriteExecutor.execute(() -> this.productDao.save(product));
    }

    public void deleteProduct(Product product) {
        ProductRoomDatabase.databaseWriteExecutor.execute(() -> this.deleteProduct(product));
    }

    public void updateProduct(Product product) {
        ProductRoomDatabase.databaseWriteExecutor.execute(() -> this.updateProduct(product));
    }


    public LiveData<List<Product>> fetchAllProducts() {
        return this.productDao.fetchAll();
    }
}
