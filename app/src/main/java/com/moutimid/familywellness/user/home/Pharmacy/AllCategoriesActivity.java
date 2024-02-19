package com.moutimid.familywellness.user.home.Pharmacy;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.familywellness.R;
import com.moutimid.familywellness.user.adapter.CategoryproductAdapter;
import com.moutimid.familywellness.user.model.HorizontalCategoryModel;
import com.moutimid.familywellness.user.model.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllCategoriesActivity extends AppCompatActivity {
    private DatabaseReference m;
    EditText search;
    RecyclerView content_rcv;
    CategoryproductAdapter my_adapter;
    final List<HorizontalCategoryModel> lastmodels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);
        search = findViewById(R.id.search_bar);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());


            }
        });

        Retrieve_Fruits();

    }

    public void Retrieve_Fruits() {
        content_rcv = findViewById(R.id.product_layout_gridview);
        content_rcv.setLayoutManager(new LinearLayoutManager(AllCategoriesActivity.this, RecyclerView.VERTICAL, false));

        my_adapter = new CategoryproductAdapter(lastmodels, AllCategoriesActivity.this);
        m = FirebaseDatabase.getInstance("https://childfr-35a43-default-rtdb.firebaseio.com/").getReference().child("FamilyWillness").child("categories");
        ValueEventListener eventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    category my_user = ds.getValue(category.class);
                    lastmodels.add(new HorizontalCategoryModel(my_user.getImage(), ds.getKey().toString()));
                }
                Collections.sort(lastmodels, new Comparator<HorizontalCategoryModel>() {
                    @Override
                    public int compare(HorizontalCategoryModel o1, HorizontalCategoryModel o2) {
                        // Define the order of categories based on priorities
                        Map<String, Integer> categoryPriorities = new HashMap<>();
                        categoryPriorities.put("Vitamin & Nutrition", 1);
                        categoryPriorities.put("Personal Care", 2);
                        categoryPriorities.put("Medicines", 3);
                        categoryPriorities.put("Mother & Baby", 4);

                        // Compare products based on category priorities
                        return Integer.compare(categoryPriorities.get(o1.getProducttitle()), categoryPriorities.get(o2.getProducttitle()));
                    }
                });
                content_rcv.setAdapter(my_adapter);
                my_adapter.notifyDataSetChanged();  // Corrected line
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<HorizontalCategoryModel> filteredlist = new ArrayList<HorizontalCategoryModel>();

        // running a for loop to compare elements.
        for (HorizontalCategoryModel item : lastmodels) {
            if (item.getProducttitle().toLowerCase().contains(text.toLowerCase())) {

                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            content_rcv.setVisibility(View.GONE);
        } else {
            content_rcv.setVisibility(View.VISIBLE);
//            my_adapter.filterList(filteredlist);
            my_adapter = new CategoryproductAdapter(filteredlist, AllCategoriesActivity.this);
            content_rcv.setAdapter(my_adapter);
            my_adapter.notifyDataSetChanged();
        }
    }


    public void backPress(View view) {
        onBackPressed();
    }
}