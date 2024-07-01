package com.example.advancepizza;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.Log;


public class MainActivity extends AppCompatActivity {
    private static DataBaseHelperCustomer dataBaseHelper;

    Intent intent;

    TextView textView;

    Button getstarted_button;
    private static final String TOAST_TEXT = "Failed To Connect To Server:(";

    //pizza.setImageResource(R.drawable.pizza);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        setProgress(false);
        dataBaseHelper = new DataBaseHelperCustomer(this);
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        dataBaseHelper.clearDatabase(db);
        getstarted_button = findViewById(R.id.getstarted);
        textView = findViewById(R.id.textView3);
        ImageView pizza = (ImageView)findViewById(R.id.imageView);
        intent = new Intent(MainActivity.this,LoginRegistrationAct.class);


        getstarted_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask(MainActivity.this );
                connectionAsyncTask.execute("\n" + "\n" + "https://mocki.io/v1/f4b7ceac-0338-4528-bd3b-ac963de4fd87");
            }
            });

    }

    public void setProgress(boolean progress) {
        ProgressBar progressBar = (ProgressBar)
                findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public void readList(List<Pizza> pizzas) {


      /*  for (int i = 0; i < 5; i++) {
            textView.setText(pizzas.get(i).toString());
        }*/

                //Log.d("TAG", PizzaJasonParser.pizzas.toString());

                if (pizzas.isEmpty()){

                    Toast toast =Toast.makeText(MainActivity.this,
                            TOAST_TEXT,Toast.LENGTH_SHORT);
                    toast.show();

                }

                else{

                   startActivity(intent);
                    finish();

                }
    }

    public static DataBaseHelperCustomer getDatabaseHelper() {
        return dataBaseHelper;
    }



}