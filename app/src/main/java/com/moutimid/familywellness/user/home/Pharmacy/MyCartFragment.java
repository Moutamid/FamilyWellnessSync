package com.moutimid.familywellness.user.home.Pharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.familywellness.R;
import com.moutimid.familywellness.user.adapter.CartAdapter;
import com.moutimid.familywellness.user.model.CartItemModel;

import java.util.ArrayList;

public class MyCartFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ArrayList<CartItemModel> cartItemModelList;
    private LinearLayout Cart_container, total_layout;
    private TextView NoItem;
    public static TextView totalprice;
    CartAdapter cartAdapter;
    private FirebaseAuth mAuth;
    private String CurrentUser;
    private DatabaseReference m , root;
    public  float totalpriceVal =0;


    public MyCartFragment() {
        // Required empty public constructor
    }

    private RecyclerView CartItemRecyclerView;
    private Button Cartconfirm ,Cartclear;

    public static MyCartFragment newInstance(String param1, String param2) {
        MyCartFragment fragment = new MyCartFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth=FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);
        CartItemRecyclerView = view.findViewById(R.id.cart_recycle);
        Cartconfirm = view.findViewById(R.id.cart_confirmbtn);
        Cartclear =  view.findViewById(R.id.cart_clearbtn);
        totalprice = view.findViewById(R.id.totalprice);
        Cart_container = view.findViewById(R.id.Cart_container);
        NoItem = view.findViewById(R.id.NoItem);
        total_layout = view.findViewById(R.id.total_layout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        CartItemRecyclerView.setLayoutManager(layoutManager);
        cartItemModelList = new ArrayList<CartItemModel>();

        root = FirebaseDatabase.getInstance("https://childfr-35a43-default-rtdb.firebaseio.com/").getReference().child("FamilyWillness");
        m = root.child("cart");
        cartAdapter = new CartAdapter(cartItemModelList);

        CartItemRecyclerView.setAdapter(cartAdapter);


        ValueEventListener valueEventListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.child(CurrentUser).getChildren()) {

                        if (!dataSnapshot.getKey().equals("totalPrice")) {
                            Log.d("kk", dataSnapshot.getKey());
                            String cartItemTitle = dataSnapshot.getKey();
                            String cartItemImage = dataSnapshot.child("productImage").getValue(String.class).toString();
                            String cartItemPrice = dataSnapshot.child("productPrice").getValue(String.class).toString();
                            String quantity = dataSnapshot.child("quantity").getValue(String.class).toString();
                            cartItemModelList.add(new CartItemModel(0, cartItemImage, cartItemTitle, 0, Float.parseFloat(cartItemPrice), 0, Integer.parseInt(quantity), 0, 0));
                        }

                    }
                    accountTotalPrice();
                    cartAdapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);

        Cartconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CartCheckActivity.class));
            }
        });

        Cartclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartItemModelList.clear();
                root.child("cart").child(CurrentUser).removeValue();
                cartAdapter.notifyDataSetChanged();
                Cart_container.setVisibility(View.GONE);
                NoItem.setVisibility(View.VISIBLE);
                total_layout.setVisibility(View.GONE);
            }
        });


        cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener(){
            @Override
            public void UpdateTotalPrice(String str) {

                totalprice.setText(str);
            }

            @Override
            public void onDeleteClick(int position) {
                String x  = cartItemModelList.get(position).getProducttitle();
                m.child(CurrentUser).child(x).removeValue();
                cartItemModelList.remove(position);
                cartAdapter.notifyItemRemoved(position);
            }
        });


        DatabaseReference root = FirebaseDatabase.getInstance("https://childfr-35a43-default-rtdb.firebaseio.com/").getReference().child("FamilyWillness");
        DatabaseReference x = root.child("cart").child(CurrentUser);
        ValueEventListener valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() <= 1) {
                    if (snapshot.exists()) {
                        Cart_container.setVisibility(View.GONE);
                        NoItem.setVisibility(View.VISIBLE);
                        total_layout.setVisibility(View.GONE);
                    } else {
                        Cart_container.setVisibility(View.VISIBLE);
                        NoItem.setVisibility(View.GONE);
                        total_layout.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        x.addListenerForSingleValueEvent(valueEventListener1);

        return  view;
    }





    public void accountTotalPrice(){
        totalpriceVal = 0;
        m = root.child("cart");
        ValueEventListener valueEventListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.child(CurrentUser).getChildren()) {

                        if (!dataSnapshot.getKey().equals("totalPrice")) {

                            String cartItemPrice = dataSnapshot.child("productPrice").getValue(String.class).toString();
                            String quantity = dataSnapshot.child("quantity").getValue(String.class).toString();
                            totalpriceVal += Float.parseFloat(  cartItemPrice) * Float.parseFloat(quantity );
                        }

                    }
                    root.child("cart").child(CurrentUser).child("totalPrice").setValue(String.valueOf(totalpriceVal));
                    String formattedNumber = String.format("%.2f", totalpriceVal);
                    totalprice.setText(String.valueOf(formattedNumber)+" ");
                    cartAdapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);

    }



}