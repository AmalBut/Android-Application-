package com.example.advancepizza;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.LinearGradient;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
public class ConnectionAsyncTask extends AsyncTask<String, String,
        String> {
    Activity activity;
    private AsyncTaskCallback callback;

    public ConnectionAsyncTask(Activity activity, AsyncTaskCallback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    public ConnectionAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ((MainActivity) activity).setProgress(true);

    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return HttpManager.getData(params[0]);
        } catch (Exception e) {
            return null;
        }
    }



    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ((MainActivity) activity).setProgress(false);

        if (s == null) {
            Toast.makeText(activity, "unsuccessful connection", Toast.LENGTH_LONG).show();
         //   callback.onTaskComplete(false);
        } else {
            Toast.makeText(activity, "successful connection", Toast.LENGTH_LONG).show();
            List<Pizza> pizzas = PizzaJasonParser.getObjectFromJason(s);
            ((MainActivity) activity).readList(pizzas);
           // callback.onTaskComplete(true);

            DataBaseHelperCustomer dataBaseHelper = ((MainActivity)activity).getDatabaseHelper();


        for (int i = 0; i < pizzas.size(); i++) {
            Pizza pizza= pizzas.get(i);

            // check if the car is already in the database
            Cursor cursor = dataBaseHelper.getPizzaByID(pizza.getId());
            if (cursor.getCount() > 0)
                continue;

            dataBaseHelper.insertPizza(pizza);

           /* if (i / (double) pizzas.size() <= 0.22) {
                insertSpecialOffer(pizza);
            }*/
        }

            Cursor cursor = dataBaseHelper.getAllPizzas();
            while (cursor.moveToNext()) {
                Pizza pizza = new Pizza();
                pizza.setId(cursor.getInt(0));
                pizza.setName(cursor.getString(1));
                pizza.setType(cursor.getString(2));
                pizza.setPrice(PizzaJasonParser.PIZZAS.get(pizza.getId()).getPrice());
             //   pizza.setSize(stringToArray(cursor.getString(4)));
                pizza.setImgFavButton(R.drawable.ic_favorite_border);
                pizza.setImgPizza(cursor.getInt(3));


                //addPizzaToCategory(pizza);
                HomeActivity.allPizzas.add(pizza);

            }
    }
    }


}