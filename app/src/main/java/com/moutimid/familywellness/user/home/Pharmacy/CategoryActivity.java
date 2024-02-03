package com.moutimid.familywellness.user.home.Pharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.familywellness.R;
import com.moutimid.familywellness.user.adapter.CategoryProductInfoAdapter;
import com.moutimid.familywellness.user.adapter.CategoryproductAdapter;
import com.moutimid.familywellness.user.model.CategoryProductInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<CategoryProductInfo> CategoryProducts;
    private CategoryProductInfoAdapter adapter;
    TextView PageTitle;
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private String UserId;
    private CategoryProductInfoAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        PageTitle=findViewById(R.id.PageTitle);
        PageTitle.setText(CategoryproductAdapter.title_name);
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        UserId = CurrentUser.getUid();
        onClickAnyProduct();
    }

    @Override
    protected void onStart() {
        super.onStart();

        setCategoryData();

    }

    private void onClickAnyProduct() {
        listener = new CategoryProductInfoAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                CategoryProductInfo product = CategoryProducts.get(position);

                Intent intent = new Intent(CategoryActivity.this, ProductInfoActivity.class);
                intent.putExtra("Product Name", product.getProductName());
                intent.putExtra("Product Price", product.getProductPrice());
                intent.putExtra("Product Price", product.getProductPrice());
                intent.putExtra("Product Image", product.getProductImage());
                intent.putExtra("Product Quantity", product.getProductQuatinty());
                intent.putExtra("Product Category", product.getProductCategory());
                intent.putExtra("Product ExpiryDate", product.getProductExpiryDate());
                intent.putExtra("Product IsFavorite", String.valueOf(product.getIsFavorite()));
                intent.putExtra("Is Offered", "no");

                startActivity(intent);
            }
        };
    }


    private void setCategoryData() {
        //toolbar

        recyclerView = (RecyclerView) findViewById(R.id.CategoryRecycler);
        CategoryProducts = new ArrayList<>();

        adapter = new CategoryProductInfoAdapter(CategoryActivity.this, CategoryProducts, listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));
        recyclerView.setAdapter(adapter);
        getProductsData();


    }

    private void getProductsData() {
        DatabaseReference root = FirebaseDatabase.getInstance("https://childfr-35a43-default-rtdb.firebaseio.com/").getReference().child("FamilyWillness");
        DatabaseReference m = root.child("product").child(CategoryproductAdapter.title_name);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        final String ProductName = dataSnapshot.getKey().toString();
                        final String ProductPrice = dataSnapshot.child("price").getValue().toString();
                        final String ProductImage = dataSnapshot.child("image").getValue().toString();
                        final String ProductExpiryDate = dataSnapshot.child("details").getValue().toString();
                        final String ProductCategory = dataSnapshot.child("category").getValue().toString();
                        final String ProductQuatinty = dataSnapshot.child("quantity").getValue().toString();
                   CategoryProducts.add(new CategoryProductInfo(ProductImage, ProductName, ProductPrice, ProductExpiryDate, false, ProductCategory, ProductQuatinty));
                    }
                    Collections.sort(CategoryProducts, new Comparator<CategoryProductInfo>() {
                        @Override
                        public int compare(CategoryProductInfo o1, CategoryProductInfo o2) {
                            // Define the order of categories based on priorities
                            Map<String, Integer> categoryPriorities = new HashMap<>();
                            categoryPriorities.put("Vitamin & Nutrition", 1);
                            categoryPriorities.put("Personal Care", 2);
                            categoryPriorities.put("Medicines", 3);
                            categoryPriorities.put("Mother & Baby", 4);

                            // Compare products based on category priorities
                            return Integer.compare(categoryPriorities.get(o1.getProductCategory()), categoryPriorities.get(o2.getProductCategory()));
                        }
                    });

                    // Update your original list with the sorted list
                    CategoryProducts.clear();
                    CategoryProducts.addAll(CategoryProducts);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }

    public void backPress(View view) {
        onBackPressed();
    }
}