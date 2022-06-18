package com.saharia.notesell.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.saharia.notesell.Constants;
import com.saharia.notesell.R;
import com.saharia.notesell.databinding.ActivityDetailBinding;
import com.saharia.notesell.databinding.ActivityViewpdfBinding;

public class ViewpdfActivity extends AppCompatActivity {


     ActivityViewpdfBinding binding;

       String postId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpdf);

        binding =ActivityViewpdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent= getIntent();
        postId=intent.getStringExtra("postId");

            load();

    }

    private void load() {


        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("post");
        databaseReference.child(postId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String pdfurl=""+ snapshot.child("pdf").getValue();

                        loadpdffromurl(pdfurl);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadpdffromurl(String pdfurl) {

            StorageReference storageReference= FirebaseStorage.getInstance().getReferenceFromUrl(pdfurl);

            storageReference.getBytes(Constants.MAX_BYTES_PDF)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            binding.pb.setVisibility(View.GONE);
                            binding.pdfView.fromBytes(bytes)

                                    .swipeHorizontal(false)
                                    .onError(new OnErrorListener() {
                                        @Override
                                        public void onError(Throwable t) {
                                            Toast.makeText(ViewpdfActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).onPageError(new OnPageErrorListener() {
                                @Override
                                public void onPageError(int page, Throwable t) {
                                    Toast.makeText(ViewpdfActivity.this, "Error on page"+page+""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }).load();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }


}