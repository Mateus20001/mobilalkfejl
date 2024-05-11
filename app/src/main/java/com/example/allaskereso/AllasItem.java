package com.example.allaskereso;

public class AllasItem {
    String name;
    String leiras;
    private int imgResource;

    public AllasItem() {

    }

    public AllasItem(String name, String leiras, int imgResource) {
        this.name = name;
        this.leiras = leiras;
        this.imgResource = imgResource;
    }

    public String getName() {
        return name;
    }

    public String getLeiras() {
        return leiras;
    }

    public int getImgResource() {
        return this.imgResource;
    }
}
