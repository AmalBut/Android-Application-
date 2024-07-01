package com.example.advancepizza;

import android.view.View;

public class Pizza {
    private int id;
    private String name;
    private String type;
    private String[] size;
    private int[] price;
    private int imgPizza;
    private int imgFavButton;

    private int visibleOrderButton = View.VISIBLE;
    private String date;
    private boolean isOnOffer;

    private int visibleDate=View.INVISIBLE;


    public Pizza() {

    }

    public Pizza(int id, String name, String type, String[] size, int[] price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
        this.price = price;
        this.imgPizza = imgPizza;
    }

    public boolean isOnOffer() {
        return isOnOffer;
    }

    public void setOnOffer(boolean onOffer) {
        isOnOffer = onOffer;
    }

    public int getVisibleOrderButton() {
        return visibleOrderButton;
    }

    public void setVisibleOrderButton(int visibleOrderButton) {
        this.visibleOrderButton = visibleOrderButton;
    }

    public String getDate() {
        return date;
    }

    public Pizza(int id, String name, String type, String[] size, int[] price, int imgPizza, int imgFavButton, int visibleOrderButton, String date, int visibleDate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
        this.price = price;
        this.imgPizza = imgPizza;
        this.imgFavButton = imgFavButton;
        this.visibleOrderButton = visibleOrderButton;
        this.date = date;
        this.visibleDate = visibleDate;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVisibleDate() {
        return visibleDate;
    }

    public void setVisibleDate(int visibleDate) {
        this.visibleDate = visibleDate;
    }

    public int getImgFavButton() {
        return imgFavButton;
    }

    public void setImgFavButton(int imgFavButton) {
        this.imgFavButton = imgFavButton;
    }

    public Pizza(int id, String name, String type, String[] size, int[] price, int imgPizza, int imgFavButton) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
        this.price = price;
        this.imgPizza = imgPizza;
        this.imgFavButton = imgFavButton;
    }

    public Pizza(int id, String type, String[] size, int[] price, int imgPizza) {
        this.id = id;

        this.type = type;
        this.size = size;
        this.price = price;
        this.imgPizza = imgPizza;
    }

    public Pizza(int id,String name, String type, String[] size, int[] price, int imgPizza) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
        this.price = price;
        this.imgPizza = imgPizza;
    }

    public Pizza(int id, String type, String[] size, int[] price) {
        this.type = type;
        this.size=size;
        this.price=price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getPrice() {
        return price;
    }

    public void setPrice(int[] price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

   public String[] getSize() {
        return size;
    }

    public void setSize(String[] size) {
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImgPizza() {
        return imgPizza;
    }

    public void setImgPizza(int imgPizza) {
        this.imgPizza = imgPizza;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
              //  ", size=" + Arrays.toString(size) +
               // ", price=" + Arrays.toString(price) +
                ", imgPizza=" + imgPizza +
                '}';
    }

    public static Pizza getPizzaByID(int id){

        for (Pizza pizza : HomeActivity.allPizzas) {
            if (pizza.id == id) {
                return pizza;
            }
        }
        return null; // Return null if no pizza with the given ID is found
    }
}