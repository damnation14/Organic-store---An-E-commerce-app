package com.example.organicstoreapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organicstoreapp.Interface.ItemClickListener;
import com.example.organicstoreapp.R;


public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textProductName,textProductDescription,textProductprice,txtProductStatus;
    public ImageView imageview;
    public ItemClickListener listener;


    public ItemViewHolder(@NonNull View itemView) {

        super(itemView);
        imageview=itemView.findViewById(R.id.product_SellerImage);
        textProductName=itemView.findViewById(R.id.product_SellerNametv);
        textProductDescription=itemView.findViewById(R.id.product_SellerDesc);
        textProductprice=itemView.findViewById(R.id.product_SellerPrice);
        txtProductStatus=itemView.findViewById(R.id.product_sellerState);

    }
    public void setItemClicklistener(ItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);

    }
}