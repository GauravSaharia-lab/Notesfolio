package com.saharia.notesell.Fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.saharia.notesell.Activities.GotoLibraryActivity;
import com.saharia.notesell.PostAdapter;
import com.saharia.notesell.R;
import com.saharia.notesell.model.Addmodel;
import com.saharia.notesell.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class userProfileFragment extends Fragment {

    RecyclerView itemrcv;
    EditText search;
    // FragmentHomeBinding binding;
    ArrayList<Addmodel> lList;

    FirebaseDatabase database2;

   // DatabaseReference databaseReference;
    FirebaseAuth auth;
    Button library;

    public userProfileFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_profile, container, false);

        database2=FirebaseDatabase.getInstance();
        auth= FirebaseAuth.getInstance();
        // PostAdapter postAdapter = new PostAdapter();
        itemrcv=view.findViewById(R.id.rec);
        search=view.findViewById(R.id.searchEt);
        library=view.findViewById(R.id.librarybtn);

        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GotoLibraryActivity.class);

                startActivity(intent);
            }
        });

       ImageView profile=view.findViewById(R.id.pro);
        TextView name=view.findViewById(R.id.namePro);
        TextView loaction=view.findViewById(R.id.locationPro);



        DatabaseReference reference;
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        reference= database.getReference().child("users").child(FirebaseAuth.getInstance().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                Picasso.get()
                        .load(user.getProfileImage())
                        .placeholder(R.drawable.profile)
                        .into(profile);

                name.setText(user.getName());
                loaction.setText(user.getLocation());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //recyclerview

        lList=new ArrayList<>();
        PostAdapter postAdapter= new PostAdapter(getContext(),lList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        itemrcv.setLayoutManager(linearLayoutManager);
        itemrcv.addItemDecoration(new DividerItemDecoration(itemrcv.getContext(),DividerItemDecoration.VERTICAL));
        itemrcv.setNestedScrollingEnabled(false);
        itemrcv.setAdapter(postAdapter);


       /* database2.getReference().child("post")

                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            Addmodel post= dataSnapshot.getValue(Addmodel.class);
                            //post.setPostID(dataSnapshot.getKey());
                            lList.add(post);

                        }
                        postAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/


        Query query= FirebaseDatabase.getInstance().getReference("post")
                .orderByChild("postedBy").equalTo(auth.getCurrentUser().getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                   lList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: Loading ");
                    Addmodel post = dataSnapshot.getValue(Addmodel.class);
                    //post.setPostID(dataSnapshot.getKey());
                    lList.add(post);

                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        //Search

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {

                    postAdapter.getFilter().filter(s);


                }catch (Exception e){


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        return view;



    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.profile,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.log:
                auth.signOut();
                break;
            case R.id.u_profile:
                Toast.makeText(getContext(), "dwd", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}