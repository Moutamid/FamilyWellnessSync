package com.moutimid.familywellness.adminpanel.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.moutamid.familywellness.R;
import com.moutimid.familywellness.adminpanel.Adapter.AdminCatogryAdapter;
import com.moutimid.familywellness.adminpanel.AddCatogryActivity;
import com.moutimid.familywellness.adminpanel.EditCatogryActivity;
import com.moutimid.familywellness.adminpanel.Model.CategoryModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private RecyclerView SalesMenRecycler;
    private AdminCatogryAdapter adapter;
    private Button SalesFloatingActionButton;
    private List<CategoryModel> adminCatogryList;
    private DatabaseReference mDataBaseRef;
    private ProgressBar bar;

    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category, container, false);

        SalesMenRecycler = (RecyclerView) view.findViewById(R.id.SalesMenRecycler);
        SalesFloatingActionButton = (Button) view.findViewById(R.id.SalesFloatingBtnId);

        mDataBaseRef = FirebaseDatabase.getInstance("https://childfr-35a43-default-rtdb.firebaseio.com/").getReference().child("FamilyWillness").child("categories");
        bar = view.findViewById(R.id.CatogryProgressBar);
        Dialog lodingbar = new Dialog(requireContext());
        lodingbar.setContentView(R.layout.loading);
        Objects.requireNonNull(lodingbar.getWindow()).setBackgroundDrawable(new ColorDrawable(UCharacter.JoiningType.TRANSPARENT));
        lodingbar.setCancelable(false);
        lodingbar.show();
        adminCatogryList = new ArrayList<>();

        bar.setVisibility(View.VISIBLE);
        adapter = new AdminCatogryAdapter(getActivity(), adminCatogryList);
        SalesMenRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        SalesMenRecycler.setAdapter(adapter);

        mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adminCatogryList.clear();

                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    adminCatogryList.add(new CategoryModel(snapshot1.getKey() ,
                            snapshot1.child("image").getValue(String.class)));
                }
                adapter.notifyDataSetChanged();
                bar.setVisibility(View.INVISIBLE);
                lodingbar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter.setOnItemClickListener(new AdminCatogryAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent i = new Intent(getActivity() , EditCatogryActivity.class);
                Bundle b = new Bundle();
                b.putString("image" , adminCatogryList.get(pos).getImage());
                b.putString("name" , adminCatogryList.get(pos).getName());
                i.putExtras(b);
                startActivity(i);
            }
        });

        adapter.setOnLongClickListener(new AdminCatogryAdapter.onLongClickListener() {
            @Override
            public void onItemLongClick(final int pos) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity()).setTitle("Confirmation").setMessage("Are You Sure You Want To Delete ?!").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference reference = mDataBaseRef.child(adminCatogryList.get(pos).getName());
                        reference.removeValue();
                        StorageReference a = FirebaseStorage.getInstance("gs://childfr-35a43.appspot.com").getReference().child("FamilyWillness").child("categories").child(adminCatogryList.get(pos).getName() + ".jpg");
                        a.delete();
                        StorageReference b = FirebaseStorage.getInstance("gs://childfr-35a43.appspot.com").getReference().child("FamilyWillness").child("categories").child(adminCatogryList.get(pos).getName());
                        b.delete();
                        StorageReference c = FirebaseStorage.getInstance("gs://childfr-35a43.appspot.com").getReference().child("FamilyWillness").child("categories").child(adminCatogryList.get(pos).getName() + "qr.jpg");
                        c.delete();
                        StorageReference d = FirebaseStorage.getInstance("gs://childfr-35a43.appspot.com").getReference().child("FamilyWillness").child("categories").child(adminCatogryList.get(pos).getName() + "qr");
                        d.delete();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setIcon(android.R.drawable.ic_dialog_alert);
                dialog.show();


            }
        });

        //on clicking to adding button
        SalesFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //here add button
                startActivity(new Intent(getActivity(), AddCatogryActivity.class));

            }
        });

        return view;
    }
}