package com.saharia.notesell.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saharia.notesell.model.User;
import com.saharia.notesell.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedItem;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

      if (auth.getCurrentUser()!=null){
            Intent intent= new Intent(MainActivity.this, HomeActivity.class);
            startActivity( intent);
            finish();
        }

        binding.pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
            }
        });
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameTxt = binding.name.getText().toString();
                String emailTxt = binding.email.getText().toString();
                String passTxt = binding.password.getText().toString();
                String locationTxt = binding.location.getText().toString();
                //  dialog.show();
                if (!nameTxt.isEmpty() && !emailTxt.isEmpty() && !passTxt.isEmpty()) {

                    auth.createUserWithEmailAndPassword(emailTxt, passTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "Password should 6 charcters", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, "Please fill empty field", Toast.LENGTH_SHORT).show();
                }

                if (selectedItem != null) {
                    Log.d(TAG, "onClick: Error on creating ");

                    StorageReference reference = storage.getReference().child("Profiles").child(String.valueOf(System.currentTimeMillis()));
                    reference.putFile(selectedItem).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){

                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                         String imageUrl= uri.toString();



                                        String uid=auth.getUid();
                                          User user= new User(uid,nameTxt,locationTxt,imageUrl);
                                          database.getReference()
                                                .child("users")
                                                .child(uid)
                                                .setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        //  dialog.dismiss();
                                                        Intent intent= new Intent(MainActivity.this,HomeActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });



                                    }
                                });
                            }
                        }
                    });


                }
                else {
                    Log.d(TAG, "onClick: Null photo");
                }
            }
        });
    }




    public void gotoSignin(View view){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity( intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==45 && data !=null){
            if (data.getData() !=null){
                binding.pro.setImageURI(data.getData());

                selectedItem=data.getData();

            }

        }



    }
}