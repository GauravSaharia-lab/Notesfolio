package com.saharia.notesell;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saharia.notesell.Activities.CommentActivity;
import com.saharia.notesell.databinding.BookmarkedRowPdfBinding;
import com.saharia.notesell.databinding.DashbordBinding;
import com.saharia.notesell.model.Addmodel;
import com.saharia.notesell.model.Qmodel;
import com.saharia.notesell.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Qadapter extends RecyclerView.Adapter<Qadapter.viewHolder> {

    ArrayList<Qmodel>list;
    Context context;

    public Qadapter(ArrayList<Qmodel> list, Context context) {
        this.list = list;
        this.context = context;
    }




    @NonNull
    @Override
    public Qadapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.dashbord,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Qadapter.viewHolder holder, int position) {
           Qmodel qmodel= list.get(position);
        Picasso.get().load(qmodel.getQpostImage())
                .placeholder(R.drawable.pdf)
                .into(holder.binding.Qpostmage);

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(qmodel.getQpostedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user= dataSnapshot.getValue(User.class);
                Picasso.get()
                        .load(user.getProfileImage())
                        .placeholder(R.drawable.profile)
                        .into(holder.binding.profileImageDB);
                holder.binding.usernameDB.setText(user.getName());
                holder.binding.comment.setText(qmodel.getCommentCount()+"");
                holder.binding.like.setText(qmodel.getPostLike()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FirebaseDatabase.getInstance().getReference()
                .child("Qposts")
                .child(qmodel.getQpostID())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if (dataSnapshot.exists()){
                           holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hearton,0,0,0);
                       }else {

                           holder.binding.like.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   FirebaseDatabase.getInstance().getReference("Qposts")
                                           .child(qmodel.getQpostID())
                                           .child("likes")
                                           .child(FirebaseAuth.getInstance().getUid())
                                           .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void unused) {
                                           FirebaseDatabase.getInstance().getReference()
                                                   .child("Qposts")
                                                   .child(qmodel.getQpostID())
                                                   .child("postLike")
                                                   .setValue(qmodel.getPostLike()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void unused) {
                                                   holder.binding.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hearton,0,0,0);
                                               }
                                           });
                                       }
                                   });

                               }
                           });
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

           holder.binding.comment.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent= new Intent(context, CommentActivity.class);
                   intent.putExtra("postId",qmodel.getQpostID());
                   intent.putExtra("postedby",qmodel.getQpostedBy());
                   intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                   context.startActivity(intent);
               }
           });
    }

    @Override
    public int getItemCount() {
        return  list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        DashbordBinding binding;
        public viewHolder(View Itemview){

            super(Itemview);
            binding=DashbordBinding.bind(Itemview);
        }


    }
}
