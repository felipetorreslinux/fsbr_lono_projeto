package com.lono.Models;

public class Gallery_Images {
    int id;
    String images;
    String extencion;

    public Gallery_Images(int id, String images, String extencion) {
        this.id = id;
        this.images = images;
        this.extencion = extencion;
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

    public String getExtencion() {
        return extencion;
    }

    public void setExtencion(String extencion) {
        this.extencion = extencion;
    }
}
