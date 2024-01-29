package com.moutimid.familywellness.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.familywellness.R;
import com.moutimid.familywellness.user.home.Pharmacy.CategoryActivity;
import com.moutimid.familywellness.user.model.HorizontalCategoryModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryproductAdapter extends RecyclerView.Adapter<CategoryproductAdapter.GalleryPhotosViewHolder> {

    List<HorizontalCategoryModel> HorizontalCategoryModelList;
    ImageView productImage;
    TextView producttitle;
    public static String title_name;
    Context context;

    public CategoryproductAdapter(List<HorizontalCategoryModel> HorizontalCategoryModelList, Context context) {
        this.HorizontalCategoryModelList = HorizontalCategoryModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public GalleryPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.category_item, parent, false);
        return new GalleryPhotosViewHolder(view);
    }

    //    public void filterList(ArrayList<ResturantModel> filterlist) {
//
//        productModels = filterlist;
//        notifyDataSetChanged();
//    }
    @Override
    public void onBindViewHolder(@NonNull GalleryPhotosViewHolder holder, final int position) {
        HorizontalCategoryModel resturantModel = HorizontalCategoryModelList.get(position);
        Picasso.get().load(HorizontalCategoryModelList.get(position).getProductimage()).into(productImage);
        producttitle.setText(HorizontalCategoryModelList.get(position).getProducttitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title_name = HorizontalCategoryModelList.get(position).getProducttitle();
//                Toast.makeText(context, "name"+HorizontalCategoryModelList.get(position).getProducttitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("Category Name", HorizontalCategoryModelList.get(position).getProducttitle());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return HorizontalCategoryModelList.size();
    }

    public class GalleryPhotosViewHolder extends RecyclerView.ViewHolder {


        public GalleryPhotosViewHolder(@NonNull View view) {
            super(view);
            productImage = view.findViewById(R.id.item_image);
            producttitle = view.findViewById(R.id.item_title);

        }
    }
}
