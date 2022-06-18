package com.saharia.notesell;

import com.saharia.notesell.PostAdapter;
import com.saharia.notesell.model.Addmodel;

import java.util.ArrayList;

public class FilterCategory extends android.widget.Filter {

    public ArrayList<Addmodel> flist;
    public PostAdapter postAdapter;

    public FilterCategory(ArrayList<Addmodel> flist, PostAdapter postAdapter) {
        this.flist = flist;
        this.postAdapter = postAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults filterResults= new FilterResults();
        if (constraint!=null  && constraint.length()>0){

            constraint=constraint.toString().toUpperCase();
            ArrayList<Addmodel>filterModels=new ArrayList<>();
            for (int i=0;i<flist.size(); i++){

                if (flist.get(i).getPostTitile().toUpperCase().contains(constraint) ){

                    filterModels.add(flist.get(i));

                }
            }

            filterResults.count=filterModels.size();
            filterResults.values=filterModels;

        }else {
            filterResults.count=flist.size();
            filterResults.values=flist;

        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        postAdapter.list=(ArrayList<Addmodel>)results.values;
        postAdapter.notifyDataSetChanged();
    }
}