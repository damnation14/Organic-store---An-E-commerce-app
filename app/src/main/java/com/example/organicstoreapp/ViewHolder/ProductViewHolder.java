package com.example.organicstoreapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organicstoreapp.Interface.ItemClickListener;
import com.example.organicstoreapp.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textProductName,textProductDescription,textProductprice;
    public ImageView imageview;
    public ItemClickListener listener;


    public ProductViewHolder(@NonNull View itemView) {

        super(itemView);
        imageview=itemView.findViewById(R.id.productimageIV);
        textProductName=itemView.findViewById(R.id.productnametv);
        textProductDescription=itemView.findViewById(R.id.productdescriptiontv);
        textProductprice=itemView.findViewById(R.id.productpricetv);

    }
    public void setItemClicklistener(ItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);

    }
}
