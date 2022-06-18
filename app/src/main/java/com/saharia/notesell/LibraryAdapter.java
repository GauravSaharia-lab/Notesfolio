package com.saharia.notesell;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saharia.notesell.Activities.DetailActivity;
import com.saharia.notesell.Activities.LibraryViewActivity;
import com.saharia.notesell.databinding.BookmarkedRowPdfBinding;
import com.saharia.notesell.databinding.LibraryRowBinding;
import com.saharia.notesell.model.Addmodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.HolderLibrary>{
    private Context context;
    private ArrayList<Addmodel> libArrayList;


    public LibraryAdapter(Context context, ArrayList<Addmodel> libArrayList) {
        this.context = context;
        this.libArrayList = libArrayList;
    }

    @NonNull
    @Override
    public HolderLibrary onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.library_row,parent,false);

        return new HolderLibrary(view);


    }

    @Override
    public void onBindViewHolder(@NonNull HolderLibrary holder, int position) {

        Addmodel addmodel= libArrayList.get(position);
        loadbookDetails(addmodel,holder);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(context,LibraryViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("bookId",addmodel.getPostID());
               context.startActivity(intent);
            }
        });

    }

    private void loadbookDetails(Addmodel addmodel, HolderLibrary holder) {

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

                        holder.binding.libraryUserTitle.setText(title);
                        holder.binding.liDescriptionUser.setText(description);
                        Picasso.get()
                                .load(url)
                                .placeholder(R.drawable.pdf)
                                .into(holder.binding.libraryImage);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return libArrayList.size();
    }

    class HolderLibrary extends RecyclerView.ViewHolder{

        LibraryRowBinding binding;
        public HolderLibrary(@NonNull View itemView) {
            super(itemView);
            binding=LibraryRowBinding.bind(itemView);
        }
    }
}
