package com.stainley.fa.android.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.stainley.fa.android.databinding.ActivityAddProductBinding;
import com.stainley.fa.android.model.Product;
import com.stainley.fa.android.viewmodel.ProductViewModel;
import com.stainley.fa.android.viewmodel.ProductViewModelFactory;

public class ProductAddActivity extends AppCompatActivity {

    private ActivityAddProductBinding binding;
    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddProductBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        productViewModel = new ViewModelProvider(this, new ProductViewModelFactory(getApplication())).get(ProductViewModel.class);

        binding.saveProductBtn.setOnClickListener(this::createProduct);

    }

    public void createProduct(View view) {
        TextInputEditText productNameText = binding.productNameTxt;
        TextInputEditText productDescriptionText = binding.productDescriptionTxt;
        TextInputEditText productPriceText = binding.productPriceTxt;

        Product product = new Product();

        if (!productNameText.getText().toString().isEmpty()
                && !productDescriptionText.getText().toString().isEmpty()
                && !productPriceText.getText().toString().isEmpty()) {

            product.setName(productNameText.getText().toString());
            product.setDescription(productDescriptionText.getText().toString());
            product.setPrice(Double.valueOf(productPriceText.getText().toString()));
            productViewModel.saveProduct(product);
            finish();
        }


    }
}
