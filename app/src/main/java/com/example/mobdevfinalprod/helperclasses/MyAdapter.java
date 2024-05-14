package com.example.mobdevfinalprod.helperclasses;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobdevfinalprod.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<Item> items;
    public MyAdapter(Context context, List<Item> items){
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = items.get(position);
        holder.exercise_name.setText(item.exerciseName);
        Picasso.get().load(item.getImage()).into(holder.image_view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.exercise_image);
        }
    }
}
