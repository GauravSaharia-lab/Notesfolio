package com.saharia.notesell;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saharia.notesell.Activities.DocumentActivity;
import com.saharia.notesell.model.Addmodel;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MyApplication extends Application {


    @Override
    public void onCreate() {

        super.onCreate();
    }

        public static final String formatTimestamp(long timestap){

            Calendar calendar= Calendar.getInstance(Locale.ENGLISH);
            calendar.setTimeInMillis(timestap);
            String date= DateFormat.format("dd/MM/yyyy",calendar).toString();
            return date;

        }


    public static void deletebook(Context context, String bookid, String bookUrl, String bookTitile) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait");

        progressDialog.setMessage("Deleting " + bookTitile + " ... ");
        progressDialog.show();

        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(bookUrl);
        reference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess:  Now Deleted from Storage ");

                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("post");
                        reference1.child(bookid)
                                .removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        Log.d(TAG, "onFailure:  Deleted from db Too");
                                        Toast.makeText(context, "Book deleted Sucessfull", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Log.d(TAG, "onFailure: Cannot  Deleted from db Too");
                                Toast.makeText(context, "Failed to delete Sucessfull", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

    }


    public static void addtoFac(Context context, String bookId) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {

            Toast.makeText(context, "You are not logged in ", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("bookId", "" + bookId);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
            databaseReference.child(firebaseAuth.getUid()).child("Favourites").child(bookId)
                    .setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(context, "Added to your favourite list... ", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to add to favourite due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }

    public static void removefromFav(Context context, String bookId) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {

            Toast.makeText(context, "You are not logged in ", Toast.LENGTH_SHORT).show();
        } else {


            DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("users");
            databaseReference3.child(firebaseAuth.getUid()).child("Favourites").child(bookId)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(context, "Remove from your favourite list... ", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to remove from favourite due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }


    }
    public static  void incrementviewscount(String bookId){


        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("post");
        reference.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String viewsCount= ""+dataSnapshot.child("viewsCount").getValue();
                        if (viewsCount.equals("views")){
                            viewsCount="0";
                        }
                        long newviewscopunt=Long.parseLong(viewsCount)+1;

                        HashMap<String,Object>hashMap=new HashMap<>();
                        hashMap.put("viewsCount",newviewscopunt);
                        DatabaseReference reference1= FirebaseDatabase.getInstance().getReference("post");
                        reference1.child(bookId)
                                .updateChildren(hashMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
