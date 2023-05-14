package com.iteso.image_editing;

public enum Format {
    JPG("jpg"), PNG("png"), BMP("bmp");

    private String name;

    private Format(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
