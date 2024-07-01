package com.example.advancepizza;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddOfferActivity extends AppCompatActivity {
    Intent intent;

    EditText endDate ;
    String currentDate;

    SharedPrefManager sharedPrefManager;
    Button back_button;

    Cursor cursor;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pizza_special_offer);
        String[] options = {"Tandoori Chicken Pizza","BBQ Chicken Pizza","Pesto Chicken Pizza","Buffalo Chicken Pizza","Pepperoni","Vegetarian Pizza","Mushroom Truffle Pizza","Margarita","New York Style","Calzone","Seafood Pizza","Hawaiian","Neapolitan"};
        final Spinner pizzanameSpinner = (Spinner)
                findViewById(R.id.pizzaname_spinner);

        ArrayAdapter<String> objpizzasArr = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        pizzanameSpinner.setAdapter(objpizzasArr);

        String[] options2 = {"Small","Medium","Large"};
        final Spinner pizzasizeSpinner = (Spinner)
                findViewById(R.id.pizzasize_spinner);

        ArrayAdapter<String> objpizzasizesArr = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options2);
        pizzasizeSpinner.setAdapter(objpizzasizesArr);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = dateFormat.format(calendar.getTime());

        EditText discountamount = findViewById(R.id.discount);
        endDate = findViewById(R.id.textView_endDate);

        // pizzaname = findViewById(R.id.textView_name);

        Button addoffer_button = findViewById(R.id.addoffer);
        back_button = findViewById(R.id.back);
        sharedPrefManager =SharedPrefManager.getInstance(this);
        intent = new Intent(AddOfferActivity.this, HomeAdminActivity.class);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                finish();
            }
        });

        addoffer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelperCustomer dataBaseHelper = MainActivity.getDatabaseHelper();
                cursor = dataBaseHelper.getPizzaByName(pizzanameSpinner.getSelectedItem().toString());
                cursor.moveToFirst();
                SpecialOffer newOffer = new SpecialOffer();
                if (pizzanameSpinner.getSelectedItem().toString().isEmpty()){
                    Toast.makeText(AddOfferActivity.this, "Invalid Pizza Name!!!", Toast.LENGTH_SHORT).show();

                }
                else{
                    newOffer.setPizzaId(cursor.getInt(0));
                }
                if (pizzasizeSpinner.getSelectedItem().toString().isEmpty())
                    Toast.makeText(AddOfferActivity.this, "ENTER THE SIZEE!!!", Toast.LENGTH_SHORT).show();
                else newOffer.setPizzaSize(pizzasizeSpinner.getSelectedItem().toString());
                if( endDate.getText().toString().isEmpty())
                    endDate.setError("Date must not be empty");
                else if (!validateEndDate(endDate.getText().toString(), currentDate))
                    endDate.setError("Date must not be today's date or the date of previous days and with th efillowing format YYYY-MM-DD");
                else
                    newOffer.setEndDate(endDate.getText().toString());
                if (discountamount.getText().toString().isEmpty())
                    discountamount.setError("No Discount");
                else newOffer.setDiscount(discountamount.getText().toString());

                newOffer.setStartDate(currentDate);

                dataBaseHelper = MainActivity.getDatabaseHelper();
                dataBaseHelper.insertSpecialOffer(newOffer);

                if(validateEndDate(endDate.getText().toString(), currentDate)) {

                    Intent intent = new Intent(AddOfferActivity.this, HomeAdminActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        });


    }


    private boolean validateEndDate(String date, String currentDate) {
        String datePattern = "^\\d{4}-\\d{2}-\\d{2}$";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(datePattern);
        Matcher matcher = pattern.matcher(date);

        if ( date.isEmpty()){
            return false;
        }
        else if(!matcher.matches()){
            return false;
        }
        String[] dateParts = date.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);
        String[] dateParts2 = currentDate.split("-");
        int year2 = Integer.parseInt(dateParts2[0]);
        int month2 = Integer.parseInt(dateParts2[1]);
        int day2 = Integer.parseInt(dateParts2[2]);

        if (date.equals(currentDate)  ) {
            return false;
        }
        else if(year<year2){
            return false;
        }
        else if(month<month2&&year==year2){
            return false;
        }
        else if(day<day2&&month==month2&&year==year2){
            return false;

        }
        return true;
}

}
