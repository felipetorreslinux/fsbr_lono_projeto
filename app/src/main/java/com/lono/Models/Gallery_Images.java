package com.lono.Models;

public class Gallery_Images {
    int id;
    String images;
    String name;

    public Gallery_Images(int id, String images, String name) {
        this.id = id;
        this.images = images;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
