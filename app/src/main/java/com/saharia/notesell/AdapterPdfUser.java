package com.saharia.notesell;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.saharia.notesell.Activities.DetailActivity;
import com.saharia.notesell.Activities.Filters.Filterhome;
import com.saharia.notesell.databinding.RoeUserBinding;
import com.saharia.notesell.databinding.RowXmlBinding;
import com.saharia.notesell.model.Addmodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.stream.Stream;

public class AdapterPdfUser extends RecyclerView.Adapter<AdapterPdfUser.HolderPdfIser> implements Filterable {


    private Context context;
    public ArrayList<Addmodel>pdfArraylist, filterlist;
    private Filterhome filterhome;

//  private   RoeUserBinding binding;

   private static final String TAG="ADAPTER_USER_TAG";

    public AdapterPdfUser(Context context, ArrayList<Addmodel> pdfArraylist) {
        this.context = context;
        this.pdfArraylist = pdfArraylist;
        this.filterlist = pdfArraylist;
    }

    @NonNull
    @Override
    public HolderPdfIser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.roe_user,parent,false);

        return new HolderPdfIser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPdfIser holder, int position) {

            Addmodel addmodel= pdfArraylist.get(position);
        //date from myapplication class
        long timestamp=addmodel.getPostedAt();

        String title= addmodel.getPostTitile();
        String des= addmodel.getPostDescription();
        String url= addmodel.getPostImage();
        String pdfId=addmodel.getPostID();

        String formateDate=MyApplication.formatTimestamp(timestamp);
        holder.binding.date.setText(formateDate);

        holder.binding.userTitle.setText(title);
        holder.binding.descriptionUser.setText(des);
        holder.binding.viewsUser.setText(addmodel.getViewsCount()+"");
        Picasso.get().load(url)
                .placeholder(R.drawable.pdf)
                .into(holder.binding.userviewImg);

          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent3= new Intent(context, DetailActivity.class);
                  intent3.putExtra("bookId",pdfId);

                  //  intent.putExtra("detail",list.get(position));

                  context.startActivity(intent3);
              }
          });

    }

    @Override
    public int getItemCount() {
        return pdfArraylist.size();
    }



    @Override
    public  Filter getFilter() {

        if (filterhome==null){

            filterhome=new Filterhome(filterlist,this);
        }

        return filterhome;
    }



    public  class HolderPdfIser extends RecyclerView.ViewHolder{

                 RoeUserBinding binding;
        public HolderPdfIser(@NonNull View itemView) {
            super(itemView);
            binding= RoeUserBinding.bind(itemView);


        }
    }

}
