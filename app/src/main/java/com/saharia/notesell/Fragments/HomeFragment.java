package com.saharia.notesell.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saharia.notesell.AdapterPdfUser;
import com.saharia.notesell.PostAdapter;
import com.saharia.notesell.R;
import com.saharia.notesell.model.Addmodel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    EditText search2;
    ArrayList<Addmodel> userList;

    FirebaseDatabase database3;

    // DatabaseReference databaseReference;
    FirebaseAuth auth;

    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);


        database3=FirebaseDatabase.getInstance();
        auth= FirebaseAuth.getInstance();
        // PostAdapter postAdapter = new PostAdapter();
        recyclerView=view.findViewById(R.id.rec);
        search2=view.findViewById(R.id.searchEt);



       //recyclerview
        userList=new ArrayList<>();
        AdapterPdfUser adapterPdfUser= new AdapterPdfUser(getContext(),userList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapterPdfUser);

        database3.getReference().child("post")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            Addmodel post= dataSnapshot.getValue(Addmodel.class);
                            //post.setPostID(dataSnapshot.getKey());
                            userList.add(post);

                        }
                        adapterPdfUser.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





        search2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {

                    adapterPdfUser.getFilter().filter(s);


                }catch (Exception e){

                     // e.getMessage();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });









        return view;
    }
}