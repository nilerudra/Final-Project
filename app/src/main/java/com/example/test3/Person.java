package com.example.test3;

import android.graphics.Bitmap;
import android.media.Image;

public class Person {
    Bitmap image;
    String name;

    public Person(Bitmap image, String name) {
        this.image = image;
        this.name = name;
    }

    public Person() {

    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
