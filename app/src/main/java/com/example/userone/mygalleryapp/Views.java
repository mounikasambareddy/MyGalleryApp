package com.example.userone.mygalleryapp;

import android.graphics.Bitmap;

/**
 * Created by Userone on 2/1/2017.
 */

public class Views {

    //private variables
    int _id;
    String Comments ;
    String  imageType;
    Bitmap imageBytes;
    String dateString;
    // Empty constructor
    public Views(Bitmap bitmap,String Comments, String imageType,String DateString)
    {
        this.Comments=Comments;
        this.imageBytes = bitmap;
        this.imageType=imageType;
        this.dateString=DateString;

    }
    public Views()
    {

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageType() {
        return imageType;
    }

    public Bitmap getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(Bitmap imageBytes) {
        this.imageBytes = imageBytes;
    }
}

