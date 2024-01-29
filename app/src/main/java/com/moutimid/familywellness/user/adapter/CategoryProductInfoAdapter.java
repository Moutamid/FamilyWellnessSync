package com.moutimid.familywellness.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.familywellness.R;
import com.moutimid.familywellness.user.model.CategoryProductInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryProductInfoAdapter extends RecyclerView.Adapter<CategoryProductInfoAdapter.ViewHolder> {
    private RecyclerViewClickListener listener;
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private String UserId;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private RelativeLayout PrContainer;
            private ImageView ProductImage;
            private TextView ProductName;
            private TextView ProductPrice;
            private TextView ProductExpiryDate;
            private ImageView PrFavoriteImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            PrContainer = (RelativeLayout)itemView.findViewById(R.id.PrContainer);
            ProductImage = (ImageView)itemView.findViewById(R.id.PrImage);
            ProductName = (TextView)itemView.findViewById(R.id.PrName);
            ProductPrice = (TextView)itemView.findViewById(R.id.PrPrice);
            ProductExpiryDate = (TextView)itemView.findViewById(R.id.PrExpiryDate);
            PrFavoriteImage = (ImageView)itemView.findViewById(R.id.PrFavoriteImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }


    private Context context;
    private List<CategoryProductInfo> ProductList;

    public CategoryProductInfoAdapter(Context context, List<CategoryProductInfo> ProductList, RecyclerViewClickListener listener){
        this.context = context;
        this.ProductList = ProductList;
        this.listener =listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.category_products_list,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CategoryProductInfo product = ProductList.get(position);

        Picasso.get().load(product.getProductImage()).into(holder.ProductImage);
        holder.ProductName.setText(product.getProductName());
        holder.ProductPrice.setText("Price: "+product.getProductPrice()+"  ");
//        holder.ProductExpiryDate.setText("Expiry Date: "+product.getProductExpiryDate());

//        if(product.getProductExpiryDate().equalsIgnoreCase("null")) holder.ProductExpiryDate.setVisibility(View.INVISIBLE);
//        else holder.ProductExpiryDate.setVisibility(View.VISIBLE);

        if(product.getIsFavorite())holder.PrFavoriteImage.setImageResource(R.drawable.ic_baseline_favorite_24);
        else holder.PrFavoriteImage.setImageResource(R.drawable.ic_baseline_favorite_shadow_24);


        //on clicking to favorite icon
        holder.PrFavoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                CurrentUser = mAuth.getCurrentUser();
                UserId = CurrentUser.getUid();

                if(product.getIsFavorite()){
                    holder.PrFavoriteImage.setImageResource(R.drawable.ic_baseline_favorite_shadow_24);
                    product.setFavorite(false);
                    //here delete isFavorite from firebase
                    DatabaseReference x= FirebaseDatabase.getInstance("https://childfr-35a43-default-rtdb.firebaseio.com/").getReference().child("FamilyWillness").child("favourites").child(UserId);
                    x.child(product.getProductName()).removeValue();
                }
                else{
                    holder.PrFavoriteImage.setImageResource(R.drawable.ic_baseline_favorite_24);
                    product.setFavorite(true);
                    //here save isFavorite in firebase
                    DatabaseReference x= FirebaseDatabase.getInstance("https://childfr-35a43-default-rtdb.firebaseio.com/").getReference().child("FamilyWillness").child("favourites").child(UserId).child(product.getProductName());
                    x.child("checked").setValue(true);
                    x.child("productimage").setValue(product.getProductImage());
                    x.child("productprice").setValue(""+product.getProductPrice()+"   ");
                    x.child("producttitle").setValue(product.getProductName());

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return ProductList.size();
    }


    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

}
