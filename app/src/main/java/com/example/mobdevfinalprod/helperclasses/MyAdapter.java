package com.example.mobdevfinalprod.helperclasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobdevfinalprod.DiscoverPage;
import com.example.mobdevfinalprod.InDepthExerciseView;
import com.example.mobdevfinalprod.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<String> items;
    private String username;
    public MyAdapter(String username, Context context, List<String> items) {
        this.context = context;
        this.items = items;
        this.username = username;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.exercise_button,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String exercise = items.get(position);
        holder.exercise_name_container.setText(exercise);
        int pos = position;
        holder.exercise_name_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscoverPage.data.put("exerciseName"+ DiscoverPage.counter++,exercise);
                items.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, items.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
