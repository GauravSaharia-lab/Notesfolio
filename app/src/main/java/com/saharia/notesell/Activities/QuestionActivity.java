package com.saharia.notesell.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saharia.notesell.R;
import com.saharia.notesell.databinding.ActivityDocumentBinding;
import com.saharia.notesell.databinding.ActivityQuestionBinding;
import com.saharia.notesell.model.Qmodel;
import com.saharia.notesell.model.User;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class QuestionActivity extends AppCompatActivity {
    ActivityQuestionBinding binding;
    Uri uri;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    // ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

       binding.addImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent();
               intent.setAction(Intent.ACTION_GET_CONTENT);
               intent.setType("image/*");
               startActivityForResult(intent,10);
           }
       });

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
       // dialog= new ProgressDialog(getApplicationContext());


        binding.postDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String des=binding.postDescription.getText().toString();
                if (!des.isEmpty()){

                    binding.Postbutton.setEnabled(true);
                }else {
                    binding.Postbutton.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        database.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    User user = dataSnapshot.getValue(User.class);
                    Picasso.get()
                            .load(user.getProfileImage())
                            .placeholder(R.drawable.profile)

                            .into(binding.pro);
                    binding.textname.setText(user.getName());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        binding.Postbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //dialog.show();


                  final StorageReference reference= storage.getReference().child("Qpost")
                          .child(FirebaseAuth.getInstance().getUid())
                          .child(new Date().getTime()+ "");


                  reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                     reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                         @Override
                                         public void onSuccess(Uri uri) {
                                             Qmodel qmodel = new Qmodel();
                                             qmodel.setQpostImage(uri.toString());
                                             qmodel.setQpostedBy(FirebaseAuth.getInstance().getUid());
                                             qmodel.setQpostDescription(binding.postDescription.getText().toString());
                                             qmodel.setPostedAt(new Date().getTime());
                                             
                                             
                                             database.getReference().child("Qposts")
                                                     .push().setValue(qmodel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                 @Override
                                                 public void onSuccess(Void unused) {

                                                     Toast.makeText(getApplicationContext(), "Posted Sucessfully", Toast.LENGTH_SHORT).show();
                                                 }
                                             });

                                         }
                                     });
                      }
                  });

            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData()!=null){
            uri=data.getData();
            binding.postimageview.setImageURI(uri);
            binding.Postbutton.setEnabled(true);
            binding.postimageview.setVisibility(View.VISIBLE);
        }
    }
}