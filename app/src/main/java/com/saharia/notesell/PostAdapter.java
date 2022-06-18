package com.saharia.notesell;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saharia.notesell.Activities.DetailActivity;
import com.saharia.notesell.Activities.PdfEditActivity;
import com.saharia.notesell.databinding.RowXmlBinding;
import com.saharia.notesell.model.Addmodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHoldwe>implements Filterable {

    public Context context;
    public ArrayList<Addmodel> list,flist;

    private FilterCategory filterCategory;

    private ProgressDialog progressDialog;
    public PostAdapter(Context context, ArrayList<Addmodel> list) {
        this.context = context;
        this.list = list;
        this.flist = list;


        progressDialog= new ProgressDialog(context);
        progressDialog.setTitle("Plaease Wait");
        progressDialog.setCancelable(false);


    }

    public PostAdapter() {
    }

    @NonNull
    @Override
    public viewHoldwe onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_xml,parent,false);

        return new viewHoldwe(view);
    }


    @Override
    public void onBindViewHolder(@NonNull viewHoldwe holder, @SuppressLint("RecyclerView") int position) {

         Addmodel model=list.get(position);
         String pdfId=model.getPostID();


        long timestamp2=model.getPostedAt();


        Picasso.get().load(model.getPostImage())
                .placeholder(R.drawable.pdf)
                .into(holder.binding.postimg);
        holder.binding.titileTv.setText(model.getPostTitile());
        String formateDate=MyApplication.formatTimestamp(timestamp2);


        holder.binding.formtdate.setText(formateDate);

        holder.binding.descriptionTv.setText(model.getPostDescription());

        holder.binding.views.setText(model.getViewsCount()+"");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Addmodel model= new Addmodel();

                Intent intent2= new Intent(context, DetailActivity.class);
                intent2.putExtra("bookId",pdfId);

              //  intent.putExtra("detail",list.get(position));

                context.startActivity(intent2);
            }
        });

        holder.binding.moreBtn.setOnClickListener(new View.OnClickListener() {

           ;
            @Override
            public void onClick(View v) {

                moreoptionsDialog(model,holder);

            }
        });

    }

    private void moreoptionsDialog(Addmodel model, viewHoldwe holder) {
        String bookid=model.getPostID();
        String bookUrl=model.getPdf();
        String bookTitile=model.getPostTitile();

        String[]options={"Edit","Delete"};
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Choose Options")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){

                   Intent intent= new Intent(context, PdfEditActivity.class);
                   intent.putExtra("bookId",bookid);
                   context.startActivity(intent);

                        }else if (which==1){
                            MyApplication.deletebook(context,""+bookid,""+bookUrl,""+bookTitile);
                       //deletebook(model,holder);
                        }
                    }
                }).show();

    }



    @Override
    public int getItemCount() {

        return list.size();
    }

    @Override
    public Filter getFilter() {

        if (filterCategory==null){

            filterCategory=new FilterCategory(flist,this);
        }
        return filterCategory;
    }


    public class  viewHoldwe extends RecyclerView.ViewHolder{

         RowXmlBinding binding;
        public viewHoldwe(@NonNull View itemView) {
            super(itemView);
            binding=RowXmlBinding.bind(itemView);
        }
    }


}
