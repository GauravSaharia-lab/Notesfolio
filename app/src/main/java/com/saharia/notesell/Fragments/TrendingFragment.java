package com.saharia.notesell.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saharia.notesell.Activities.DocumentActivity;
import com.saharia.notesell.Activities.QuestionActivity;
import com.saharia.notesell.PostAdapter;
import com.saharia.notesell.Qadapter;
import com.saharia.notesell.R;
import com.saharia.notesell.databinding.FragmentTrendingBinding;
import com.saharia.notesell.model.Addmodel;
import com.saharia.notesell.model.Qmodel;

import java.util.ArrayList;


public class TrendingFragment extends Fragment {

        FragmentTrendingBinding binding;
      // RecyclerView recyclerView;
        ArrayList<Qmodel> QList;
        FirebaseAuth auth;
        FirebaseDatabase database;

    public TrendingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            binding= FragmentTrendingBinding.inflate(inflater,container,false);


            database =FirebaseDatabase.getInstance();
            auth=FirebaseAuth.getInstance();

           binding.Askbutton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(getActivity(), QuestionActivity.class);

                   startActivity(intent);
               }
           });

        QList=new ArrayList<>();

        Qadapter adapter= new Qadapter(QList,getContext());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());

        binding.Qrec.setLayoutManager(linearLayoutManager);
        binding.Qrec.addItemDecoration(new DividerItemDecoration(binding.Qrec.getContext(),DividerItemDecoration.VERTICAL));
        binding.Qrec.setNestedScrollingEnabled(false);
        binding.Qrec.setAdapter(adapter);

        database.getReference().child("Qposts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                QList.clear();
               for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                   Qmodel qmodel= dataSnapshot1.getValue(Qmodel.class);
                   qmodel.setQpostID(dataSnapshot1.getKey());
                   QList.add(qmodel);


               }
               adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return  binding.getRoot();
    }


}