package com.saharia.notesell;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saharia.notesell.Activities.DetailActivity;
import com.saharia.notesell.databinding.BookmarkedRowPdfBinding;
import com.saharia.notesell.model.Addmodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterPdfFavourite extends RecyclerView.Adapter<AdapterPdfFavourite.HolderFavourite>{


    private Context context;
    private ArrayList<Addmodel>pdfArrayList;




    public AdapterPdfFavourite(Context context, ArrayList<Addmodel> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
    }

    @NonNull
    @Override
    public HolderFavourite onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bookmarked_row_pdf,parent,false);

     return new  HolderFavourite(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderFavourite holder, int position) {

        Addmodel addmodel= pdfArrayList.get(position);
        loadbookDetails(addmodel,holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DetailActivity.class);
                intent.putExtra("bookId",addmodel.getPostID());
                context.startActivity(intent);
            }
        });

        holder.binding.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  MyApplication.removefromFav(context,addmodel.getPostID());
            }
        });

    }

    private void loadbookDetails(Addmodel addmodel, HolderFavourite holder) {

        String bookId=addmodel.getPostID();
      //  Log.d(TAG, "loadbookDetails: ");

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("post");
        reference.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String title="" + dataSnapshot.child("postTitile").getValue();
                        String  description=""+dataSnapshot.child("postDescription").getValue();
                        String url=""+dataSnapshot.child("postImage").getValue();
                        String uid=""+dataSnapshot.child("postedBy").getValue();


                        addmodel.setPostTitile(title);
                        addmodel.setPostDescription(description);
                        addmodel.setPostedBy(uid);
                        addmodel.setPostImage(url);

                        holder.binding.FavTitileTv.setText(title);
                       holder.binding.FavDescriptionTv.setText(description);
                        Picasso.get()
                                .load(url)
                                .placeholder(R.drawable.pdf)
                                .into(holder.binding.favImg);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }


    class HolderFavourite extends RecyclerView.ViewHolder{
          BookmarkedRowPdfBinding binding;

        public HolderFavourite(@NonNull View itemView) {
            super(itemView);

            binding=BookmarkedRowPdfBinding.bind(itemView);
        }
    }
}
