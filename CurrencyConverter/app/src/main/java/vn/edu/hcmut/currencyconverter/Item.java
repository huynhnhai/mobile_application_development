package vn.edu.hcmut.currencyconverter;

import java.io.Serializable;


public class Item implements Serializable {
    private int pathImg;
    private String Id;
    private Double price;

    public Item(int pathImg, String id, Double price) {
        this.pathImg = pathImg;
        Id = id;
        this.price = price;
    }

    public int getPathImg() {
        return pathImg;
    }

    public void setPathImg(int pathImg) {
        this.pathImg = pathImg;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
