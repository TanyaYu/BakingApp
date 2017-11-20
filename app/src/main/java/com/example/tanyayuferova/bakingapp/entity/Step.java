package com.example.tanyayuferova.bakingapp.entity;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.tanyayuferova.bakingapp.R;
import com.example.tanyayuferova.bakingapp.utils.NetworkUtils;

/**
 * Created by Tanya Yuferova on 11/9/2017.
 */

public class Step implements Parcelable, Comparable<Step> {

    private int id;
    private String description;
    private String shortDescription;
    private String videoURL;
    private String thumbnailURL;
    private String imageURL;

    public Step() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getStepTitle(Context context) {
        if(id == 0)
            return getShortDescription();
        return context.getString(R.string.step, id);
    }

    public String getVideoResource() {
        if(getVideoURL() != null && !getVideoURL().isEmpty())
            return getVideoURL();
        if(getThumbnailURL() != null && !getThumbnailURL().isEmpty() && NetworkUtils.isVideoFile(getThumbnailURL()))
            return getThumbnailURL();
        return null;
    }

    public String getImageResource() {
        if(getThumbnailURL() != null && !getThumbnailURL().isEmpty() && NetworkUtils.isImageFile(getThumbnailURL()))
            return getThumbnailURL();
        return null;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        String[] strings = new String[5];
        strings[0] = description;
        strings[1] = videoURL;
        strings[2] = thumbnailURL;
        strings[3] = shortDescription;
        strings[4] = imageURL;
        parcel.writeStringArray(strings);

        parcel.writeInt(id);
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    private Step(Parcel parcel) {
        String[] strings = new String[5];
        parcel.readStringArray(strings);
        description = strings[0];
        videoURL = strings[1];
        thumbnailURL = strings[2];
        shortDescription = strings[3];
        imageURL = strings[4];

        id = parcel.readInt();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Step)
            return this.getId() == ((Step) obj).getId();
        return super.equals(obj);
    }

    @Override
    public int compareTo(@NonNull Step o) {
        if(this.getId() > o.getId())
            return 1;
        if(this.getId() < o.getId())
            return -1;
        return 0;
    }
}
