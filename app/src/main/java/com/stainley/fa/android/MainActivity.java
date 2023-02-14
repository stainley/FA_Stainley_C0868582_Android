package com.stainley.fa.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

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

public class MainActivity extends AppCompatActivity {

    protected ActivityMainBinding binding;
    private RecyclerView productRecycleView;
    private ProductRVAdapter adapter;
    private final List<Product> products = new ArrayList<>();
    private ProductViewModel productViewModel;
    private SwipeHelper swipeHelper;


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
            adapter.notifyItemChanged(productResult.size());
        });

        binding.productAdd.setOnClickListener(this::addNewProduct);

        // using SwipeHelper class
        swipeHelper = new SwipeHelper(this, 300, productRecycleView) {
            @Override
            protected void instantiateSwipeButton(RecyclerView.ViewHolder viewHolder, List<SwipeUnderlayButton> buffer) {
                buffer.add(new SwipeUnderlayButton(MainActivity.this,
                        "Delete",
                        R.drawable.ic_delete,
                        30,
                        50,
                        Color.parseColor("#ff3c30"),
                        SwipeDirection.LEFT,
                        new SwipeUnderlayButtonClickListener() {
                            @Override
                            public void onClick(int position) {
                                //deleteEmployee(position);
                                System.out.println("DELETING");
                            }
                        }));
                buffer.add(new SwipeUnderlayButton(MainActivity.this,
                        "Update",
                        R.drawable.ic_edit,
                        30,
                        50,
                        Color.parseColor("#ff9502"),
                        SwipeDirection.LEFT,
                        new SwipeUnderlayButtonClickListener() {
                            @Override
                            public void onClick(int position) {
                                //displayEmployeeForEditing(position);
                                System.out.println("UPDATING");
                            }
                        }));
            }
        };

    }

    public void addNewProduct(View view) {
        Intent intent = new Intent(this, ProductAddActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }
}