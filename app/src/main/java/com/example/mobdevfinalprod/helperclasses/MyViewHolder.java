package com.example.mobdevfinalprod.helperclasses;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobdevfinalprod.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView exercise_name_container;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        exercise_name_container = itemView.findViewById(R.id.exercise_name);
    }
}
