package com.example.tanyayuferova.bakingapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tanya Yuferova on 11/9/2017.
 */

public class Ingredient implements Parcelable {

    private double quantity;
    private String measure;
    private String name;

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


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        String[] strings = new String[2];
        strings[0] = measure;
        strings[1] = name;
        parcel.writeStringArray(strings);

        parcel.writeDouble(quantity);
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
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Ingredient)
            return this.getName().equals(((Ingredient) obj).getName());
        return super.equals(obj);
    }
}
