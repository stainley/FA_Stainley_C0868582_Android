package com.stainley.fa.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.stainley.fa.android.databinding.ActivityAddProductBinding;
import com.stainley.fa.android.model.Location;
import com.stainley.fa.android.model.Product;
import com.stainley.fa.android.viewmodel.ProductViewModel;
import com.stainley.fa.android.viewmodel.ProductViewModelFactory;

public class ProductAddActivity extends AppCompatActivity {

    private ActivityAddProductBinding binding;
    private ProductViewModel productViewModel;
    private Product oldProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddProductBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        oldProduct = (Product) getIntent().getSerializableExtra("oldProduct");

        if (oldProduct != null) {
            binding.productNameTxt.setText(oldProduct.getName());
            binding.productDescriptionTxt.setText(oldProduct.getDescription());
            binding.productPriceTxt.setText(String.valueOf(oldProduct.getPrice()));

            if (oldProduct.getLocation() != null) {
                binding.locationTxt.setText(oldProduct.getLocation().getLatitude() + ", " + oldProduct.getLocation().getLongitude());
            }
        }

        productViewModel = new ViewModelProvider(this, new ProductViewModelFactory(getApplication())).get(ProductViewModel.class);
        binding.saveProductBtn.setOnClickListener(this::createProduct);

        binding.locationMapBtn.setOnClickListener(this::addLocation);

    }

    public void addLocation(View view) {
        Intent mapIntent = new Intent(this, MapsActivity.class);
        if (oldProduct != null) {
            if (oldProduct.getLocation() != null) {
                mapIntent.putExtra("product", oldProduct);
            }
        }
        startActivity(mapIntent);
    }

    public void createProduct(View view) {
        TextInputEditText productNameText = binding.productNameTxt;
        TextInputEditText productDescriptionText = binding.productDescriptionTxt;
        TextInputEditText productPriceText = binding.productPriceTxt;

        Product product = new Product();

        if (!productNameText.getText().toString().isEmpty()
                && !productDescriptionText.getText().toString().isEmpty()
                && !productPriceText.getText().toString().isEmpty()) {

            if (oldProduct != null) {
                product = oldProduct;
            }
            product.setName(productNameText.getText().toString());
            product.setDescription(productDescriptionText.getText().toString());
            product.setPrice(Double.valueOf(productPriceText.getText().toString()));
            if (oldProduct != null) {
                productViewModel.updateProduct(product);
            } else {
                productViewModel.saveProduct(product);
            }
            finish();
        }


    }
}
