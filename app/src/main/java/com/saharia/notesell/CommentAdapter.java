package com.saharia.notesell;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saharia.notesell.databinding.CommentSampleBinding;
import com.saharia.notesell.databinding.ContentSplashBinding;
import com.saharia.notesell.model.Comment;
import com.saharia.notesell.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.viewholder> {
    Context context;
    ArrayList<Comment>clist;

    public CommentAdapter(Context context, ArrayList<Comment> clist) {
        this.context = context;
        this.clist = clist;
    }



    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.comment_sample,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Comment comment=clist.get(position);
        //String text1 = TimeAgo.using(comment.getCommentTime());
       // holder.binding.time.setText(text1);

        //holder.binding.comment.setText(comment.getCommentBody());


        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(comment.getCommentedBY()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                Picasso.get()
                        .load(user.getProfileImage())
                        .placeholder(R.drawable.profile)
                        .into(holder.binding.profileImageDB);

                holder.binding.comment.setText(Html.fromHtml("<b>"+user.getName()+"</b>"+"  " +comment.getCommentBody()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return clist.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{

      CommentSampleBinding binding;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            binding=CommentSampleBinding.bind(itemView);

        }
    }
}
