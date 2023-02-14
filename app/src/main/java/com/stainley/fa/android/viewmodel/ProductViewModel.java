package com.stainley.fa.android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.stainley.fa.android.data.ProductRepository;
import com.stainley.fa.android.model.Product;

import java.util.List;

public class ProductViewModel extends ViewModel {

    private final ProductRepository productRepository;

    public ProductViewModel(Application application) {
        productRepository = new ProductRepository(application);
    }

    public void saveProduct(@NonNull Product product) {
        productRepository.saveProduct(product);
    }

    public void deleteProduct(@NonNull Product product) {
        productRepository.deleteProduct(product);
    }

    public void updateProduct(@NonNull Product product) {
        productRepository.updateProduct(product);
    }

    public LiveData<List<Product>> fetchAllProducts() {
        return productRepository.fetchAllProducts();
    }

    public LiveData<List<Product>> fetchAllByProductName(String name) {
        return productRepository.fetAllByProductName(name);
    }

    public LiveData<Product> findProductById(long id) {
        return productRepository.findProductById(id);
    }
}
