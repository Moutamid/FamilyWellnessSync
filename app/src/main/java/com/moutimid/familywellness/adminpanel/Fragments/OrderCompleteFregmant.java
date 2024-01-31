package com.moutimid.familywellness.adminpanel.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.moutimid.familywellness.adminpanel.Adapter.AdminOrderAdapter;
import com.moutimid.familywellness.adminpanel.Model.MyorderModel;

import java.util.ArrayList;
import java.util.Objects;

public class OrderCompleteFregmant extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    ArrayList<MyorderModel> orderItemList;
    AdminOrderAdapter adapter;
    private FirebaseAuth mAuth;
    private String CurrentUser;
    private DatabaseReference m, root;
    public static Activity fa;

    public OrderCompleteFregmant() {
        // Required empty public constructor
    }

    private RecyclerView OrderItemRecyclerView;

    public static OrderCompleteFregmant newInstance(String param1, String param2) {
        OrderCompleteFregmant fragment = new OrderCompleteFregmant();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_fregmant, container, false);
        fa = getActivity();
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();
        OrderItemRecyclerView = view.findViewById(R.id.orderrecycler);
        orderItemList = new ArrayList<MyorderModel>();
        adapter = new AdminOrderAdapter(getActivity(), orderItemList);
        OrderItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        OrderItemRecyclerView.setAdapter(adapter);
        DatabaseReference roott = FirebaseDatabase.getInstance("https://childfr-35a43-default-rtdb.firebaseio.com/").getReference().child("FamilyWillness");
        DatabaseReference x = roott.child("order");
        Dialog lodingbar = new Dialog(requireContext());
        lodingbar.setContentView(R.layout.loading);
        Objects.requireNonNull(lodingbar.getWindow()).setBackgroundDrawable(new ColorDrawable(UCharacter.JoiningType.TRANSPARENT));
        lodingbar.setCancelable(false);
        lodingbar.show();

        ValueEventListener valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot : snapshot1.getChildren()) {
                            if (dataSnapshot.hasChild("status")) {
                                if (dataSnapshot.child("status").getValue().toString().equals("complete")) {
                                    Log.d("datasnap", dataSnapshot + " ");
                                    String Date = dataSnapshot.child("Date").getValue().toString();
                                    int nums = ((int) (dataSnapshot.child("orderproducts").getChildrenCount()));
                                    String totalPrice = dataSnapshot.child("totalPrice").getValue().toString();
                                    String OrderCheck = dataSnapshot.child("IsChecked").getValue().toString();
                                    String phonenumber = dataSnapshot.child("phonenumber").getValue().toString();
                                    String address = dataSnapshot.child("address").getValue().toString();
                                    String email = dataSnapshot.child("email").getValue().toString();
                                    String name = dataSnapshot.child("name").getValue().toString();
                                    String token = dataSnapshot.child("token").getValue().toString();
                                    String key = dataSnapshot.child("key").getValue().toString();
                                    String uid = dataSnapshot.child("uid").getValue().toString();
//                                    String token="aba";
                                    String products = "";
                                    for (DataSnapshot data : dataSnapshot.child("orderproducts").getChildren()) {
                                        products += "        " + data.getKey() + "\n                 Price: " + data.child("productPrice").getValue().toString() + " $\n                Quantity: " + data.child("quantity").getValue().toString() + "\n";
                                    }
                                    orderItemList.add(new MyorderModel(dataSnapshot.getKey(), "" + Date, String.valueOf(nums), totalPrice + "  ", "   " + products, OrderCheck, address, email, phonenumber, name, token, uid, key, 4));
                                }
                            }
                        }
                    }
                } else {
                    orderItemList.clear();
                }
                adapter.notifyDataSetChanged();
                lodingbar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        x.addListenerForSingleValueEvent(valueEventListener1);

        return view;
    }
}