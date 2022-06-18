package com.saharia.notesell.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saharia.notesell.AdapterPdfFavourite;
import com.saharia.notesell.LibraryAdapter;
import com.saharia.notesell.R;
import com.saharia.notesell.databinding.ActivityCommentBinding;
import com.saharia.notesell.databinding.ActivityGotoLibraryBinding;
import com.saharia.notesell.model.Addmodel;

import java.util.ArrayList;

public class GotoLibraryActivity extends AppCompatActivity {

    ActivityGotoLibraryBinding binding;
    ArrayList<Addmodel> librarylist;
    LibraryAdapter libraryAdapter;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityGotoLibraryBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());



        //  View view= inflater.inflate(R.layout.fragment_favourite, container, false);

        auth = FirebaseAuth.getInstance();
        librarylist = new ArrayList<>();
        libraryAdapter = new LibraryAdapter(getApplicationContext(), librarylist);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        binding.libraryRv.setLayoutManager(linearLayoutManager);
        binding.libraryRv.setAdapter(libraryAdapter);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(auth.getUid()).child("Library")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        librarylist.clear();
                        for (DataSnapshot ds:dataSnapshot.getChildren()){
                           String bookId = ""+ds.child("bookId").getValue();
                            Addmodel addmodel= new Addmodel();
                            addmodel.setPostID(bookId);
                            librarylist.add(addmodel);

                        }
                        libraryAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }


}