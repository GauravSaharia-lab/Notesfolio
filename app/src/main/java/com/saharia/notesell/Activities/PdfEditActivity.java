package com.saharia.notesell.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saharia.notesell.R;
import com.saharia.notesell.databinding.ActivityMainBinding;
import com.saharia.notesell.databinding.ActivityPdfEditBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Executor;

public class PdfEditActivity extends AppCompatActivity {

    ActivityPdfEditBinding binding;
    private  String bookId;
   ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityPdfEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bookId=getIntent().getStringExtra("bookId");

        pd= new ProgressDialog(this);
        pd.setTitle("Please wait");

        pd.setCanceledOnTouchOutside(false);

           loadbookInfo();


           binding.update.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   validData();
               }
           });

    }
             private  String title="", description="";
    private void validData() {
               title=binding.titleEd.getText().toString().trim();
               description=binding.desEd.getText().toString().trim();

               if (TextUtils.isEmpty(title)){
                   Toast.makeText(this, "Enter Title....", Toast.LENGTH_SHORT).show();

               }
               else if (TextUtils.isEmpty(description)){

                   Toast.makeText(this, "Enter Description...", Toast.LENGTH_SHORT).show();
               }
               else {

                   updatePdf();
               }

    }

    private void updatePdf() {
        Log.d(TAG, "updatePdf: Starting updating info to db ");

        pd.setMessage("Updating book info...");
        pd.show();


        HashMap<String,Object>hashMap= new HashMap<>();
        hashMap.put("postTitile",title);
        hashMap.put("postDescription",description);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("post");
        databaseReference.child(bookId)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PdfEditActivity.this, "Book info Updated", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                Toast.makeText(PdfEditActivity.this, "Failed to update Book info", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadbookInfo() {
        Log.d(TAG, "loadbookInfo: loading book info");

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("post");
        reference.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String description="" +dataSnapshot.child("postDescription").getValue();
                        String title=""+dataSnapshot.child("postTitile").getValue();

                        binding.titleEd.setText(title);
                        binding.desEd.setText(description);

                        Log.d(TAG, "onDataChange: loading info");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}