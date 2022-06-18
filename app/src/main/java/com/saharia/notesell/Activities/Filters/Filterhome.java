package com.saharia.notesell.Activities.Filters;

import android.widget.Filter;

import com.saharia.notesell.AdapterPdfUser;
import com.saharia.notesell.model.Addmodel;

import java.util.ArrayList;

public class Filterhome extends Filter {
    //arraylist in which we want to search
    ArrayList<Addmodel>flist2;
    //adapter in which filter needs to be implemented
    AdapterPdfUser adapterPdfUser;
    //constructor


    public Filterhome(ArrayList<Addmodel> flist2, AdapterPdfUser adapterPdfUser) {
        this.flist2 = flist2;
        this.adapterPdfUser = adapterPdfUser;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //value to be searched not null
        if (constraint!=null  && constraint.length()>0){

            constraint=constraint.toString().toUpperCase();
            ArrayList<Addmodel>filterModels=new ArrayList<>();
            for (int i=0;i<flist2.size(); i++){

                if (flist2.get(i).getPostTitile().toUpperCase().contains(constraint) ){

                    filterModels.add(flist2.get(i));

                }
            }



            results.count=filterModels.size();
            results.values=filterModels;

        }
        else {
           results.count=flist2.size();
           results.values=flist2;

        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapterPdfUser.pdfArraylist=(ArrayList<Addmodel>)results.values;
        adapterPdfUser.notifyDataSetChanged();
    }
}
