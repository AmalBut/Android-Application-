package com.example.advancepizza;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PizzaJasonParser {

    public static List<Pizza> PIZZAS=new ArrayList<>();

    static List<String> sizes = Arrays.asList("Small", "Medium", "Large");

    static Random random = new Random();

    public static List<Pizza> getObjectFromJason(String json) {
        Map<String, List<String>> pizzas = new HashMap<>();

        pizzas.put("Chicken",Arrays.asList("Tandoori Chicken Pizza","BBQ Chicken Pizza","Pesto Chicken Pizza","Buffalo Chicken Pizza"));
        pizzas.put("Beef",Arrays.asList("Pepperoni"));
        pizzas.put("Veggies",Arrays.asList("Vegetarian Pizza","Mushroom Truffle Pizza"));
        pizzas.put("Cheese",Arrays.asList("Margarita","New York Style","Calzone"));
        pizzas.put("Seafood",Arrays.asList("Seafood Pizza"));
        pizzas.put("Specialty",Arrays.asList("Hawaiian","Neapolitan"));

        String[] sizes= {"S","M","L"};
        int id = 0;

        try {

          //  Pizza PIZZA = new Pizza();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray typesArray = jsonObject.getJSONArray("types");
            JSONArray pricesArray = jsonObject.getJSONArray("prices");
            for (int i = 0; i < typesArray.length(); i++) {
                String name = typesArray.getString(i);
                JSONArray priceArray = pricesArray.getJSONArray(i);
                int[] prices=new int[3];
                for (int j = 0; j < priceArray.length(); j++) {
                    prices[j]=priceArray.getInt(j);
                }
                int img=0;

                String type = findPizzaType(pizzas, name);

                Pizza pizza = new Pizza(id,name,type,sizes,prices,img);
                pizza.setImgFavButton(R.drawable.ic_favorite_border);
                id++;
                //PIZZAS.add(pizza);


                ////////////////////////////////////////////////////////
                for (Map.Entry<String, List<String>> entry : pizzas.entrySet()) {
                    if (entry.getValue().contains(name)) {
                        pizza.setType(entry.getKey());
                        switch (name) {
                            case "Margarita":
                                pizza.setImgPizza(R.drawable.margherita);
                                pizza.setType("Cheese");
                                break;
                            case "Neapolitan":
                                pizza.setImgPizza(R.drawable.neapolitanpizza);
                                pizza.setType("Specialty");
                                break;
                            case "Hawaiian":
                                pizza.setImgPizza(R.drawable.hawaiian);
                                pizza.setType("Specialty");
                                break;
                            case "Pepperoni":
                                pizza.setImgPizza(R.drawable.pepperoni);
                                pizza.setType("Beef");
                                break;
                            case "New York Style":
                                pizza.setImgPizza(R.drawable.newyork);
                                pizza.setType("Cheese");
                                break;
                            case "Calzone":
                                pizza.setImgPizza(R.drawable.calzone);
                                pizza.setType("Cheese");
                                break;
                            case "Tandoori Chicken Pizza":
                                pizza.setImgPizza(R.drawable.tandoorichicken);
                                pizza.setType("Chicken");
                                break;
                            case "BBQ Chicken Pizza":
                                pizza.setImgPizza(R.drawable.bbq);
                                pizza.setType("Chicken");
                                break;
                            case "Seafood Pizza":
                                pizza.setImgPizza(R.drawable.seafood);
                                pizza.setType("Seafood");
                                break;
                            case "Vegetarian Pizza":
                                pizza.setImgPizza(R.drawable.veggitarian);
                                pizza.setType("Veggies");
                                break;
                            case "Buffalo Chicken Pizza":
                                pizza.setImgPizza(R.drawable.buffalochicken);
                                pizza.setType("Chicken");
                                break;
                            case "Mushroom Truffle Pizza":
                                pizza.setImgPizza(R.drawable.mushroomtruffle);
                                pizza.setType("Veggies");
                                break;
                            case "Pesto Chicken Pizza":
                                pizza.setImgPizza(R.drawable.chickenpesto);
                                pizza.setType("Chicken");
                                break;
                        }
                    }
                }
                PIZZAS.add(pizza);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return PIZZAS;
    }

    public static String findPizzaType(Map<String, List<String>> pizzas, String pizzaName) {
        for (Map.Entry<String, List<String>> entry : pizzas.entrySet()) {
            String type = entry.getKey();
            List<String> pizzaList = entry.getValue();
            if (pizzaList.contains(pizzaName)) {
                return type;
            }
        }
        return null; // Return null if the pizza name is not found in any list
    }
}