package com.moutimid.familywellness.adminpanel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.familywellness.R;
import com.moutimid.familywellness.adminpanel.Model.AdminProduct;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ProductViewHolder> {

    private Context context;
    private List<AdminProduct> adminProducts;
    private AdminProductAdapter.onItemClickListener itemListener;
    private AdminProductAdapter.onLongClickListener longListener;

    public interface onItemClickListener{
        void onItemClick(int pos);
    }
    public interface onLongClickListener{
        void onItemLongClick(int pos);
    }

    public void setOnItemClickListener(AdminProductAdapter.onItemClickListener listener)
    {
        itemListener = listener;
    }

    public void setOnLongClickListener(AdminProductAdapter.onLongClickListener listener)
    {
        longListener = listener;
    }

    public AdminProductAdapter(Context context, List<AdminProduct> adminProducts) {
        this.context = context;
        this.adminProducts = adminProducts;
    }

    public void addList(List<AdminProduct> list)
    {
        adminProducts.clear();
        Collections.copy(adminProducts , list);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_product_list , parent , false);

        return new ProductViewHolder(v , itemListener , longListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Picasso.get().load(adminProducts.get(position).getImage()).centerCrop().fit().into(holder.img);
        holder.name.setText(adminProducts.get(position).getName());
        holder.category.setText("Category: "+adminProducts.get(position).getCategory());
        holder.quantity.setText("Available Amounts: "+adminProducts.get(position).getQuantity());
        holder.price.setText("Price: "+adminProducts.get(position).getPrice()+"  ");

//        if(adminProducts.get(position).getdetails().equalsIgnoreCase("null"))holder.expire.setVisibility(View.GONE);
//        else holder.expire.setVisibility(View.VISIBLE);
//        holder.expire.setText("Expiry Date: "+adminProducts.get(position).getdetails());

    }

    @Override
    public int getItemCount() {
        return adminProducts.size();
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name , category , quantity , price ;
        RelativeLayout cardView;
        public ProductViewHolder(@NonNull View itemView, final AdminProductAdapter.onItemClickListener itemlistener , final AdminProductAdapter.onLongClickListener longClickListener) {
            super(itemView);
            img = itemView.findViewById(R.id.adapterProductImage);
            name = itemView.findViewById(R.id.AdapterProductName);
            category = itemView.findViewById(R.id.AdapterProductCategory);
            quantity = itemView.findViewById(R.id.AdapterProductQuantity);
            price = itemView.findViewById(R.id.AdapterProductPrice);
//            expire = itemView.findViewById(R.id.AdapterProductExpire);
            cardView = itemView.findViewById(R.id.PrContainer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemlistener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            itemlistener.onItemClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(longClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                            longClickListener.onItemLongClick(position);
                    }
                    return false;
                }
            });
        }
    }
}
