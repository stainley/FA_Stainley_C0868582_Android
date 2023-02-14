package com.stainley.fa.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stainley.fa.android.R;
import com.stainley.fa.android.model.Product;

import java.util.List;

public class ProductRVAdapter extends RecyclerView.Adapter<ProductRVAdapter.ProductViewHolder> {

    private final List<Product> products;

    public ProductRVAdapter(List<Product> products) {
        this.products = products;
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
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameTxt;
        private TextView productDescriptionTxt;
        private TextView productPriceTxt;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTxt = itemView.findViewById(R.id.productName);
            productDescriptionTxt = itemView.findViewById(R.id.productDescription);
            productPriceTxt = itemView.findViewById(R.id.productPrice);
        }
    }
}
