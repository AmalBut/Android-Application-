package com.example.advancepizza;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.advancepizza.ui.pizzamenu.PizzaMenuFragment;

public class PizzaMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pizzamenu);
       /* if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PizzaMenuFragment.newInstance())
                    .commitNow();
        }*/
    }
}