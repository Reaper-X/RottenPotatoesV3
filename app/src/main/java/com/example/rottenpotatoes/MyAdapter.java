package com.example.rottenpotatoes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.amplify.generated.graphql.ListMoviesQuery;

import java.util.ArrayList;
import java.util.List;

import static android.view.LayoutInflater.from;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ListMoviesQuery.Item> mData = new ArrayList<>();;
    private LayoutInflater mInflater;

    //data is passed into the construct.
    MyAdapter(Context context){
        this.mInflater = from(context);
    }

    //inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    //binds the data to TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.bindData(mData.get(position));
    }

    //total number of rows
    @Override
    public int getItemCount(){
        return mData.size();
    }

    //resets the list with a new set of data
    public void setItems(List<ListMoviesQuery.Item> items){
        mData = items;
    }

    //stores and recycles views as they are scrolled off-screen
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_title;
        TextView txt_genre;
        TextView txt_director;
        TextView num_year;

        ViewHolder(View itemView){
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_genre = itemView.findViewById(R.id.txt_genre);
            txt_director = itemView.findViewById(R.id.txt_director);
            num_year = itemView.findViewById(R.id.num_year);
        }

        void bindData(ListMoviesQuery.Item item){
            txt_title.setText(item.title());
            txt_genre.setText(item.genre());
            txt_director.setText(item.Director());
            num_year.setText(item.Year());
        }
    }
}
