package com.saharia.notesell.Activities;

import  androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saharia.notesell.MyApplication;
import com.saharia.notesell.R;
import com.saharia.notesell.databinding.ActivityDetailBinding;
import com.saharia.notesell.databinding.ActivityMainBinding;
import com.saharia.notesell.model.Addmodel;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;
   String bookId;
   FirebaseDatabase database;
   boolean isInMyfav= false;
   FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());

       Intent intent=getIntent();
        bookId=intent.getStringExtra("bookId");

    firebaseAuth=FirebaseAuth.getInstance();

    if (firebaseAuth.getCurrentUser()!=null){

        checkisFav();
    }

       loadbookDetails();
    MyApplication.incrementviewscount(bookId);

       binding.readBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent1 = new Intent(getApplicationContext(), ViewpdfActivity.class);
               // intent.putExtra("kaw",model.getPdf());
               intent1.putExtra("postId", bookId);
               startActivity(intent1);

           }
       });

       binding.buyId.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent6= new Intent(DetailActivity.this,PaymentActivity.class);
               intent6.putExtra("amountid",bookId);

               startActivity(intent6);
           }
       });

       binding.addBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
             if (firebaseAuth.getCurrentUser()==null){

                 Toast.makeText(DetailActivity.this, "You are not logged in ", Toast.LENGTH_SHORT).show();
             }
             else {

                 if (isInMyfav){

                     MyApplication.removefromFav(DetailActivity.this,bookId);
                 }
                 else {

                       MyApplication.addtoFac(DetailActivity.this,bookId);

                 }
             }
           }
       });


    }

    private void loadbookDetails() {

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("post");

        reference.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       String title="" + dataSnapshot.child("postTitile").getValue();
                       String  description=""+dataSnapshot.child("postDescription").getValue();
                       String  price=""+dataSnapshot.child("postPrice").getValue();
                      String url=""+dataSnapshot.child("postImage").getValue();
                  String viewscount="" +dataSnapshot.child("viewsCount").getValue();
                      binding.titileDT.setText(title);
                      binding.desDT.setText(description);
                      binding.price.setText(price);
               binding.viewsTv.setText(viewscount);

                        Picasso.get()
                                .load(url)
                                .placeholder(R.drawable.pdf)
                                .into(binding.pdfimg);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void checkisFav() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(firebaseAuth.getUid()).child("Favourites").child(bookId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        isInMyfav = dataSnapshot.exists();
                        if (isInMyfav){

                          binding.addBtn.setText("Remove Favourite ");
                        }else {

                          binding.addBtn.setText("Add to Fav ");
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }



}