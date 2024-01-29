package com.moutimid.familywellness.adminpanel.Model;

public class CategoryModel {
    private String name, image;

    public CategoryModel(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public CategoryModel(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
