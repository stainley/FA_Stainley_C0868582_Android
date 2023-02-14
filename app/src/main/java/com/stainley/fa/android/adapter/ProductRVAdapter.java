package com.stainley.fa.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.stainley.fa.android.R;
import com.stainley.fa.android.model.Product;

import java.util.List;

public class ProductRVAdapter extends RecyclerView.Adapter<ProductRVAdapter.ProductViewHolder> {

    private final List<Product> products;
    private final OnProductRowCallback onCallback;

    public ProductRVAdapter(List<Product> products, OnProductRowCallback onCallback) {
        this.products = products;
        this.onCallback = onCallback;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_row, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.productNameTxt.setText(products.get(position).getName());
        holder.productDescriptionTxt.setText(products.get(position).getDescription());
        holder.productPriceTxt.setText(String.valueOf(products.get(position).getPrice()));

        holder.productCardView.setOnClickListener(v -> {
            onCallback.onRowSelected(position);
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameTxt;
        private TextView productDescriptionTxt;
        private TextView productPriceTxt;
        private CardView productCardView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTxt = itemView.findViewById(R.id.productName);
            productDescriptionTxt = itemView.findViewById(R.id.productDescription);
            productPriceTxt = itemView.findViewById(R.id.productPrice);
            productCardView = itemView.findViewById(R.id.productCardView);
        }
    }

    public interface OnProductRowCallback {
        void onRowSelected(int position);
    }
}
