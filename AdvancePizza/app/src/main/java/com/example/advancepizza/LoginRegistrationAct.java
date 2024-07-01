package com.example.advancepizza;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class LoginRegistrationAct extends AppCompatActivity {

    public static List<Pizza> Chicken = new ArrayList<>();
    public static List<Pizza> Beef = new ArrayList<>();
    public static List<Pizza> Veggies = new ArrayList<>();
    public static List<Pizza> Cheese = new ArrayList<>();
    public static List<Pizza> Seafood = new ArrayList<>();
    public static List<Pizza> Specialty = new ArrayList<>();

    public static List<Pizza> allPizzas = new ArrayList<>();
    TextView fullname, fname, lname,email, phone, gender;

    DataBaseHelperCustomer dataBaseHelper;

    Intent intent1,intent2;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_registration);

        dataBaseHelper = new DataBaseHelperCustomer(this);

        Button signin_button = findViewById(R.id.signin);
        textView =findViewById(R.id.textView4);
        Button signup_button = findViewById(R.id.signup);


       intent1 = new Intent(LoginRegistrationAct.this, LoginActivity.class);
        intent2 = new Intent(LoginRegistrationAct.this, SignupActivity.class);
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent1);
                finish();
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(intent2);
                finish();
            }
        });

    }



}