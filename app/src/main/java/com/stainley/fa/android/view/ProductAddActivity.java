package com.stainley.fa.android.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.stainley.fa.android.databinding.ActivityAddProductBinding;
import com.stainley.fa.android.model.Product;
import com.stainley.fa.android.viewmodel.ProductViewModel;
import com.stainley.fa.android.viewmodel.ProductViewModelFactory;

public class ProductAddActivity extends AppCompatActivity {

    private ActivityAddProductBinding binding;
    private ProductViewModel productViewModel;
    private Product oldProduct;
    private long productId;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int REQUEST_CODE = 1;

    private SharedPreferences sp;

    private com.stainley.fa.android.model.Location productLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddProductBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = location -> {
            updateLocationInfo(location);
        };

        // if the permission is granted, we request the update.
        // if the permission is not granted, we request for the access.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location lasKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lasKnownLocation != null)
                updateLocationInfo(lasKnownLocation);
        }

        productViewModel = new ViewModelProvider(this, new ProductViewModelFactory(getApplication())).get(ProductViewModel.class);

        sp = getSharedPreferences("product_sp", MODE_PRIVATE);
        productId = sp.getLong("long_id", -1L);

        productViewModel.findProductById(productId).observe(this, productFound -> {
            this.oldProduct = productFound;

            if (oldProduct != null) {
                binding.productNameTxt.setText(oldProduct.getName());
                binding.productDescriptionTxt.setText(oldProduct.getDescription());
                binding.productPriceTxt.setText(String.valueOf(oldProduct.getPrice()));

                if (oldProduct.getLocation() != null) {
                    binding.locationTxt.setText(oldProduct.getLocation().getLatitude() + ", " + oldProduct.getLocation().getLongitude());
                }
            }
        });

        binding.saveProductBtn.setOnClickListener(this::createProduct);
        binding.locationMapBtn.setOnClickListener(this::addLocation);

    }

    private void updateLocationInfo(Location location) {
        productLocation = new com.stainley.fa.android.model.Location();
        productLocation.setLatitude(location.getLatitude());
        productLocation.setLongitude(location.getLongitude());
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

        if (!productNameText.getText().toString().isEmpty() && !productDescriptionText.getText().toString().isEmpty() && !productPriceText.getText().toString().isEmpty()) {

            if (oldProduct != null) {
                product = oldProduct;
            }
            product.setName(productNameText.getText().toString());
            product.setDescription(productDescriptionText.getText().toString());
            product.setPrice(Double.valueOf(productPriceText.getText().toString()));
            product.setLocation(productLocation);
            if (oldProduct != null) {
                productViewModel.updateProduct(product);
            } else {
                productViewModel.saveProduct(product);
            }
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            SharedPreferences.Editor editor = getSharedPreferences("product_sp", MODE_PRIVATE).edit();
            editor.remove("long_id");
            editor.apply();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    private void startListening() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
}
