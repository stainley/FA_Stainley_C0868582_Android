package com.stainley.fa.android.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.stainley.fa.android.model.Product;

import java.util.List;

@Dao
public interface ProductDao extends AbstractDao<Product> {
    @Insert
    @Override
    void save(Product product);

    @Update
    @Override
    void update(Product product);

    @Delete
    @Override
    void delete(Product product);

    @Query("SELECT * FROM PRODUCT_TBL")
    @Override
    LiveData<List<Product>> fetchAll();

}
