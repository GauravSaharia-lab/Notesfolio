package com.saharia.notesell.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saharia.notesell.AdapterPdfFavourite;
import com.saharia.notesell.R;
import com.saharia.notesell.model.Addmodel;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment {

  ArrayList<Addmodel>pdfL;
  AdapterPdfFavourite adapterPdfFavourite;
  RecyclerView recyclerView;
  FirebaseAuth auth;


    public FavouriteFragment() {
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
        View view= inflater.inflate(R.layout.fragment_favourite, container, false);
         recyclerView=view.findViewById(R.id.library2);
        auth= FirebaseAuth.getInstance();
         pdfL=new ArrayList<>();
        adapterPdfFavourite= new AdapterPdfFavourite(getContext(),pdfL);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapterPdfFavourite);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.child(auth.getUid()).child("Favourites")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              pdfL.clear();
                              for (DataSnapshot ds:dataSnapshot.getChildren()){
                              String bookId = ""+ds.child("bookId").getValue();
                              Addmodel addmodel= new Addmodel();
                              addmodel.setPostID(bookId);
                              pdfL.add(addmodel);

                              }
                            adapterPdfFavourite.notifyDataSetChanged();

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return view;
    }


}