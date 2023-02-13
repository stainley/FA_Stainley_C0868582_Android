package com.stainley.fa.android.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import com.stainley.fa.android.model.Product;

import java.util.List;

@Dao
public interface ProductDao extends AbstractDao<Product> {
    @Override
    void save(Product product);

    @Override
    void update(Product product);

    @Override
    void delete(Product product);
    @Override
    LiveData<List<Product>> fetchAll();

}
