package com.example.tanyayuferova.bakingapp.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanya Yuferova on 11/9/2017.
 */

public class Recipe implements Parcelable, Comparable<Recipe> {

    private int id;
    private String name;
    private int servings;
    private String image;
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Step> steps = new ArrayList<>();

    public Recipe() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image==null || image.isEmpty() ? null : image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        String[] strings = new String[2];
        strings[0] = name;
        strings[1] = image;
        parcel.writeStringArray(strings);

        int[] integers = new int[2];
        integers[0] = id;
        integers[1] = servings;
        parcel.writeIntArray(integers);

        parcel.writeList(ingredients);
        parcel.writeList(steps);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    private Recipe(Parcel parcel) {
        String[] strings = new String[2];
        parcel.readStringArray(strings);
        name = strings[0];
        image = strings[1];

        int[] integers = new int[2];
        parcel.readIntArray(integers);
        id = integers[0];
        servings = integers[1];

        ingredients = new ArrayList<>();
        parcel.readList(ingredients, Ingredient.class.getClassLoader());

        steps = new ArrayList<>();
        parcel.readList(steps, Step.class.getClassLoader());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Recipe)
            return this.getId() == ((Recipe) obj).getId();
        return super.equals(obj);
    }

    @Override
    public int compareTo(@NonNull Recipe item) {
        if(this.getId() > item.getId())
            return 1;
        if(this.getId() < item.getId())
            return  -1;
        return 0;
    }
}
