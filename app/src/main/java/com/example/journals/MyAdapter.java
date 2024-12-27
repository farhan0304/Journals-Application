package com.example.journals;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<JournalModel> list;


    public MyAdapter(Context context, ArrayList<JournalModel> list) {
        this.context = context;
        this.list = list;
    }

    public void addJournals(JournalModel journalModel){
        list.add(journalModel);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.journal_list_item,parent,false);

        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        JournalModel journalModel = list.get(position);
        holder.textView1.setText(journalModel.getTitle());
        holder.textView2.setText(journalModel.getContent());
        String imageUrl = journalModel.getPost();
        Glide.with(context)
                .load(imageUrl)
                .into(holder.imageView);

        String timeUpload = DateUtils.getRelativeTimeSpanString(
                journalModel.getTime().getSeconds()*1000
        ).toString();
        holder.textView3.setText(timeUpload);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView1;
        TextView textView2;
        TextView textView3;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.poster);
            textView1 = itemView.findViewById(R.id.title);
            textView2 = itemView.findViewById(R.id.cont);
            textView3 = itemView.findViewById(R.id.uploadtime);
        }
    }
}
