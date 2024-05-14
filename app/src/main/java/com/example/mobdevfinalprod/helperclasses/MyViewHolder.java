package com.example.mobdevfinalprod.helperclasses;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobdevfinalprod.R;

public class MyViewHolder extends RecyclerView.ViewHolder{

    protected ImageView image_view;
    protected TextView exercise_name;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        image_view = itemView.findViewById(R.id.exercise_image);
        exercise_name = itemView.findViewById(R.id.exercise_name);
    }
}
