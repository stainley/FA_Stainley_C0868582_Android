package com.stainley.fa.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.stainley.fa.android.adapter.ProductRVAdapter;
import com.stainley.fa.android.databinding.ActivityMainBinding;
import com.stainley.fa.android.helper.SwipeHelper;
import com.stainley.fa.android.helper.SwipeUnderlayButtonClickListener;
import com.stainley.fa.android.model.Product;
import com.stainley.fa.android.view.ProductAddActivity;
import com.stainley.fa.android.viewmodel.ProductViewModel;
import com.stainley.fa.android.viewmodel.ProductViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    protected ActivityMainBinding binding;
    private RecyclerView productRecycleView;
    private ProductRVAdapter adapter;
    private final List<Product> products = new ArrayList<>();
    private ProductViewModel productViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        productRecycleView = binding.productRecycleView;

        adapter = new ProductRVAdapter(products);
        productRecycleView.setLayoutManager(new LinearLayoutManager(this));
        productRecycleView.setAdapter(adapter);
        productViewModel = new ViewModelProvider(this, new ProductViewModelFactory(getApplication())).get(ProductViewModel.class);

        productViewModel.fetchAllProducts().observe(this, productResult -> {
            products.clear();
            products.addAll(productResult);
            adapter.notifyDataSetChanged();
        });

        binding.productAdd.setOnClickListener(this::addNewProduct);
        SearchView searchProduct = binding.searchProduct;
        searchProduct.setOnQueryTextListener(this);

        // using SwipeHelper class
        SwipeHelper swipeHelper = new SwipeHelper(this, 300, productRecycleView) {
            @Override
            protected void instantiateSwipeButton(RecyclerView.ViewHolder viewHolder, List<SwipeUnderlayButton> buffer) {
                buffer.add(new SwipeUnderlayButton(MainActivity.this,
                        "Delete",
                        R.drawable.ic_delete,
                        30,
                        50,
                        Color.parseColor("#ff3c30"),
                        SwipeDirection.LEFT,
                        position -> {
                            deleteProduct(position);
                            Toast.makeText(getApplicationContext(), "Product deleted", Toast.LENGTH_SHORT).show();
                        }));
                buffer.add(new SwipeUnderlayButton(MainActivity.this,
                        "Update",
                        R.drawable.ic_edit,
                        30,
                        50,
                        Color.parseColor("#ff9502"),
                        SwipeDirection.LEFT,
                        position -> {
                            SharedPreferences.Editor editor = getSharedPreferences("product_sp", MODE_PRIVATE).edit();
                            editor.putLong("long_id", products.get(position).getId());
                            editor.apply();
                            Intent productToEdit = new Intent(getApplicationContext(), ProductAddActivity.class);
                            productToEdit.putExtra("oldProduct", products.get(position));
                            startActivity(productToEdit);
                        }));
            }
        };

    }

    public void addNewProduct(View view) {
        Intent intent = new Intent(this, ProductAddActivity.class);
        startActivity(intent);
    }

    public void deleteProduct(int position) {
        productViewModel.deleteProduct(products.get(position));
        products.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.equals("")) {
            productViewModel.fetchAllProducts().observe(this, productResult -> {
                products.clear();

                products.addAll(productResult);
                adapter.notifyDataSetChanged();

            });
        }

        productViewModel.fetchAllByProductName(newText).observe(this, productResult -> {
            products.clear();
            products.addAll(productResult);
            adapter.notifyDataSetChanged();
        });
        return false;
    }
}