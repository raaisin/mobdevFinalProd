package com.example.mobdevfinalprod.helperclasses;

import static com.example.mobdevfinalprod.R.drawable.input_background;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mobdevfinalprod.R;
import com.google.firebase.annotations.concurrent.Background;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateUtils {
    public static List<Integer> getDatesForWeek(int startDay) {
        List<Integer> dates = new ArrayList<>();

        for(int i=0; i<7; i++) {
            dates.add(startDay + i);
        }
        return dates;
    }
    public static int getCurrentDay(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    public static void displayDates(View view){
        LinearLayout linearLayout = view.findViewById(R.id.date_layout);
        int currentDay = DateUtils.getCurrentDay();
        List<Integer> dates = DateUtils.getDatesForWeek(currentDay - (currentDay % 7));

        for (Integer date: dates) {
            TextView textView = new TextView(view.getContext());
            textView.setText(date.toString());
            textView.setText(String.valueOf(date));
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            textView.setTextColor(Color.WHITE);
            textView.setHeight(170);
            textView.setTextSize(15);
            if (date == currentDay) {
                textView.setBackgroundResource(R.drawable.square_button_main_color);
                ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"));
                textView.setBackgroundTintList(colorStateList);
                textView.setTextColor(view.getResources().getColor(R.color.main_color));
            }
            linearLayout.addView(textView);
        }
    }
}
