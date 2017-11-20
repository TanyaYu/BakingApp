package com.example.tanyayuferova.bakingapp.entity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

import com.example.tanyayuferova.bakingapp.R;

/**
 * Created by Tanya Yuferova on 11/9/2017.
 */

public class Ingredient implements Parcelable {

    private double quantity;
    private String measure;
    private String name;
    private boolean checked;

    public Ingredient() {

    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Drawable getMeasureDrawableResource(Context context) {
        String[] measureTypes = context.getResources().getStringArray(R.array.measure_types);
        if(measureTypes[0].equals(measure)) { //G
            return context.getResources().getDrawable(R.drawable.ic_g);
        } else if(measureTypes[1].equals(measure)) { //TSP
            return context.getResources().getDrawable(R.drawable.ic_teaspoon);
        } else if(measureTypes[2].equals(measure)) { //TBLSP
            return context.getResources().getDrawable(R.drawable.ic_spoon);
        } else if(measureTypes[3].equals(measure)) { //UNIT
            return null;
        } else if(measureTypes[4].equals(measure)) { //CUP
            return context.getResources().getDrawable(R.drawable.ic_measure_cup);
        }
        return null;
    }

    @DrawableRes
    public int getMeasureIdResource(Context context) {
        String[] measureTypes = context.getResources().getStringArray(R.array.measure_types);
        if(measureTypes[0].equals(measure)) { //G
            return R.drawable.ic_g;
        } else if(measureTypes[1].equals(measure)) { //TSP
            return R.drawable.ic_teaspoon;
        } else if(measureTypes[2].equals(measure)) { //TBLSP
            return R.drawable.ic_spoon;
        } else if(measureTypes[3].equals(measure)) { //UNIT
            return 0;
        } else if(measureTypes[4].equals(measure)) { //CUP
            return R.drawable.ic_measure_cup;
        }
        return 0;
    }

    public String getMeasureDescription(Context context) {
        String[] measureTypes = context.getResources().getStringArray(R.array.measure_types);
        if(measureTypes[0].equals(measure)) { //G
            return context.getResources().getString(R.string.g_measure);
        } else if(measureTypes[1].equals(measure)) { //TSP
            return context.getResources().getString(R.string.tsp_measure);
        } else if(measureTypes[2].equals(measure)) { //TBLSP
            return context.getResources().getString(R.string.tblsp_measure);
        } else if(measureTypes[3].equals(measure)) { //UNIT
            return context.getResources().getString(R.string.unit_measure);
        } else if(measureTypes[4].equals(measure)) { //CUP
            return context.getResources().getString(R.string.cup_measure);
        }
        return null;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        String[] strings = new String[2];
        strings[0] = measure;
        strings[1] = name;
        parcel.writeStringArray(strings);
        parcel.writeDouble(quantity);
        parcel.writeInt(checked ? 1 : 0);
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    private Ingredient(Parcel parcel) {
        String[] strings = new String[2];
        parcel.readStringArray(strings);
        measure = strings[0];
        name = strings[1];

        quantity = parcel.readDouble();
        checked = parcel.readInt() == 1;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Ingredient)
            return this.getName().equals(((Ingredient) obj).getName());
        return super.equals(obj);
    }
}
