package com.example.mobdevfinalprod.helperclasses;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mobdevfinalprod.InDepthExerciseView;
import com.example.mobdevfinalprod.R;

import java.util.ArrayList;
import java.util.List;

public class ViewUtil {
    public static List<ImageView> getAllImageViews(ViewGroup root) {
        List<ImageView> imageViews = new ArrayList<>();
        findImageViews(root, imageViews);
        return imageViews;
    }
    private static void findImageViews(ViewGroup parent, List<ImageView> imageViews) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof ImageView) {
                imageViews.add((ImageView) child);
            } else if (child instanceof ViewGroup) {
                findImageViews((ViewGroup) child, imageViews);
            }
        }
    }
    private static List<ImageView> getInitialImages(List<ImageView> imageViews1, List<ImageView> imageViews2, List<ImageView> imageViews3) {
        List<ImageView> allImageViews = new ArrayList<>();
        allImageViews.addAll(imageViews1);
        allImageViews.addAll(imageViews2);
        allImageViews.addAll(imageViews3);
        return allImageViews;
    }

    public static void setImageViewsClickable(View view, int id1, int id2, int id3) {

        LinearLayout root1 = view.findViewById(R.id.first_linearLayout);
        LinearLayout root2 = view.findViewById(R.id.second_linearLayout);
        LinearLayout root3 = view.findViewById(R.id.third_linearLayout);

        List<ImageView> allImageViews1 = ViewUtil.getAllImageViews(root1);
        List<ImageView> allImageViews2 = ViewUtil.getAllImageViews(root2);
        List<ImageView> allImageViews3 = ViewUtil.getAllImageViews(root3);

        List<ImageView> imageViews = getInitialImages(allImageViews1,allImageViews2,allImageViews3);

        for (ImageView imageView : imageViews) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String exerciseName = v.getContentDescription().toString();
                    Intent intent = new Intent(v.getContext(), InDepthExerciseView.class);
                    intent.putExtra("exerciseName", exerciseName);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
    public static void hideKeyboard(Context context) {
        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
