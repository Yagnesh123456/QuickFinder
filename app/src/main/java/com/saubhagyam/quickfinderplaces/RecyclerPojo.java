package com.saubhagyam.quickfinderplaces;

/**
 * Created by yagnesh on 15/03/18.
 */

public class RecyclerPojo {

    String name;
    int image;

    public RecyclerPojo(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }
}