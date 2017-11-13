package com.example.tanyayuferova.bakingapp.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * Created by Tanya Yuferova on 11/10/2017.
 */

public class BindingAdaptersUtils {

    @BindingAdapter({"bind:imageUrl", "bind:error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        Picasso.with(view.getContext()).load(url).error(error).into(view);
        if(view.getDrawable() == null){
            view.setImageDrawable(error);
        }
    }

    @BindingAdapter("app:layout_constraintGuide_percent")
    public static void setConstraintGuidePercent(View view, float percent) {
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        lp.guidePercent = percent;
        view.setLayoutParams(lp);
    }

    @BindingAdapter("android:src")
    public static void setImageDrawable(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource){
        imageView.setImageResource(resource);
    }

    @BindingAdapter("bind:doubleValue")
    public static void setDoubleValue(TextView view, double value) {
        view.setText(new DecimalFormat("#.##").format(value));
    }
}
