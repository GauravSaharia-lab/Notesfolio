package com.saharia.notesell.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saharia.notesell.CommentAdapter;
import com.saharia.notesell.R;
import com.saharia.notesell.databinding.ActivityCommentBinding;
import com.saharia.notesell.model.Comment;
import com.saharia.notesell.model.Qmodel;
import com.saharia.notesell.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {
    ActivityCommentBinding binding;
    Intent intent;
    String postID;
    String postedBy;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<Comment>list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent=getIntent();

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        postID=intent.getStringExtra("postId");
        postedBy=intent.getStringExtra("postedby");

       /* Toast.makeText(this, "Post ID" + postID, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Post By" + postedBy, Toast.LENGTH_SHORT).show();*/

        database.getReference()
                .child("Qposts")
                .child(postID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Qmodel qmodel=dataSnapshot.getValue(Qmodel.class);
                Picasso.get()
                        .load(qmodel.getQpostImage())
                        .placeholder(R.drawable.thumbnail)
                        .into(binding.commentImage);

                binding.commentDes.setText(qmodel.getQpostDescription());
                binding.like.setText(qmodel.getPostLike()+"");
                binding.comment.setText(qmodel.getCommentCount()+"");



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        database.getReference()
                .child("users")
                .child(postedBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                Picasso.get()
                        .load(user.getProfileImage())
                        .placeholder(R.drawable.profile)
                        .into(binding.pro);
                binding.textView7.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

      binding.commentPostBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Comment comment= new Comment();
              comment.setCommentBody(binding.commentET.getText().toString());
              comment.setCommentTime(new Date().getTime());
              comment.setCommentedBY(FirebaseAuth.getInstance().getUid());
               database.getReference()
                       .child("Qposts")
                       .child(postID)
                       .child("comments")
                       .push()
                       .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void unused) {
                              database.getReference()
                                      .child("Qposts")
                                      .child(postID)
                                      .child("CommentCount")
                                      .addListenerForSingleValueEvent(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                              int CommentCount=0;
                                              if (dataSnapshot.exists()){
                                                  CommentCount=dataSnapshot.getValue(Integer.class);
                                              }
                                              database.getReference()
                                                      .child("Qposts")
                                                      .child(postID)
                                                      .child("CommentCount")
                                                      .setValue(CommentCount+1)
                                                      .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                          @Override
                                                          public void onSuccess(Void unused) {
                                                              binding.commentET.setText(" ");
                                                              Toast.makeText(CommentActivity.this, "Commented", Toast.LENGTH_SHORT).show();
                                                          }
                                                      });
                                          }

                                          @Override
                                          public void onCancelled(@NonNull DatabaseError databaseError) {

                                          }
                                      });
                   }
               });
          }
      });

        CommentAdapter commentAdapter=new CommentAdapter(this,list);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        binding.commentRv.setLayoutManager(linearLayoutManager);
        binding.commentRv.setAdapter(commentAdapter);

        database.getReference()
                .child("Qposts")
                .child(postID)
                .child("comments")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                            Comment comment=dataSnapshot1.getValue(Comment.class);
                            list.add(comment);
                            commentAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

}