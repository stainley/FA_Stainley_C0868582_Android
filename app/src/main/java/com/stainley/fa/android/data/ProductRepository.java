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
        ProductRoomDatabase.databaseWriteExecutor.execute(() -> this.productDao.delete(product));
    }

    public void updateProduct(Product product) {
        ProductRoomDatabase.databaseWriteExecutor.execute(() -> this.productDao.update(product));
    }


    public LiveData<List<Product>> fetchAllProducts() {
        return this.productDao.fetchAll();
    }

    public LiveData<List<Product>> fetAllByProductName(String name) {
        return this.productDao.findProductByName(name);
    }
}
