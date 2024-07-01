package com.example.advancepizza;

public class SpecialOffer {
    private int id;
    private int pizzaId;
    private String pizzaSize;
    private String startDate;
    private String endDate;
    private String discount;

    public SpecialOffer() {
    }

    public SpecialOffer(int id, int pizzaId, String pizzaSize, String startDate, String endDate, String discount) {
        this.id = id;
        this.pizzaId = pizzaId;
        this.pizzaSize = pizzaSize;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discount = discount;
    }

    public String getPizzaSize() {
        return pizzaSize;
    }

    public void setPizzaSize(String pizzaSize) {
        this.pizzaSize = pizzaSize;
    }

    public SpecialOffer(int id, int pizzaId, String StartDate, String EndDate, String Discount) {
        this.id = id;
        this.pizzaId = pizzaId;
        this.startDate = StartDate;
        this.endDate = EndDate;
        this.discount = Discount;
    }
    //
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
