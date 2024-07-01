package com.example.advancepizza;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.advancepizza.ui.call.CallFragment;

public class Call extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CallFragment.newInstance())
                    .commitNow();
        }
    }
}