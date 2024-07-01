package com.example.advancepizza;

public class Order {
    //
    private int orderID;
    private String userEmail;
    private int pizzaID;
    private String orderDate;
    private String orderTime;

    private double price;

    private String pizzaSize;

    public Order() {

    }

    public Order(int orderID, String userEmail, int pizzaID, String orderDate, String orderTime) {
        this.orderID = orderID;
        this.userEmail = userEmail;
        this.pizzaID = pizzaID;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
    }

    public Order(int orderID, String userEmail, int pizzaID, String orderDate, String orderTime, double price, String pizzaSize) {
        this.orderID = orderID;
        this.userEmail = userEmail;
        this.pizzaID = pizzaID;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.price = price;
        this.pizzaSize = pizzaSize;
    }

    public Order(int orderID, String userEmail, int pizzaID, String orderDate, String orderTime, String pizzaSize) {
        this.orderID = orderID;
        this.userEmail = userEmail;
        this.pizzaID = pizzaID;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.pizzaSize = pizzaSize;
    }

    public String getPizzaSize() {
        return pizzaSize;
    }

    public void setPizzaSize(String pizzaSize) {
        this.pizzaSize = pizzaSize;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getPizzaID() {
        return pizzaID;
    }

    public void setPizzaID(int pizzaID) {
        this.pizzaID = pizzaID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}