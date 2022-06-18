package com.saharia.notesell.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saharia.notesell.Activities.DocumentActivity;
import com.saharia.notesell.R;
import com.saharia.notesell.databinding.FragmentAddBinding;
import com.saharia.notesell.model.Addmodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddFragment extends Fragment {
    FragmentAddBinding binding;
    Uri uri;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog pd;
    DatabaseReference databaseReference;
    String currentuid;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        pd = new ProgressDialog(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(getLayoutInflater());

        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle("Setting thumbnail");
        pd.setMessage("Please Wait");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);


        binding.pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

        binding.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validatedata();


            }
        });


        return binding.getRoot();
    }

    private String titile = "", description = "", category = "";

    private void validatedata() {
        titile = binding.titileEt.getText().toString();
        description = binding.des.getText().toString();

        if (TextUtils.isEmpty(titile)) {

            Toast.makeText(getContext(), "Enter Titile", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(getContext(), "Enter description", Toast.LENGTH_SHORT).show();
        } else {

            uploadtoDb();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 10 && resultCode == RESULT_OK && data.getData() != null) {

            uri = data.getData();
            binding.postimg.setImageURI(uri);

                final String randomkey=database.getReference().push().getKey();


        }

    }

    private void uploadtoDb() {

        pd.show();
        long timestamp=System.currentTimeMillis();
        String key=database.getReference().child("post")
                .push().getKey();
        currentuid=key;

        final StorageReference reference= storage.getReference().child("posts")
                .child(FirebaseAuth.getInstance().getUid())

                .child(new Date().getTime()+"");

        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Addmodel post= new Addmodel();
                        post.setPostImage(uri.toString());
                        post.setPdf("");
                        post.setPostCategory("");
                        post.setPostPrice("");
                        post.setPostDescription(binding.des.getText().toString());
                        post.setPostID(key);
                        post.setPostedBy(FirebaseAuth.getInstance().getUid());
                        post.setPostTitile(binding.titileEt.getText().toString());
                        post.setPostedAt(timestamp);
                        post.setViewsCount(0);

                        database.getReference().child("post")
                                .child(key)
                                .setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                pd.dismiss();
                                Toast.makeText(getContext(), "Posted sucessfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getActivity(), DocumentActivity.class);
                                intent.putExtra("currentuid", currentuid);
                                startActivity(intent);


                            }
                        });







                    }
                });



            }
        });

    }



}

