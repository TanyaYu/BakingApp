package com.example.tanyayuferova.bakingapp.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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
}
