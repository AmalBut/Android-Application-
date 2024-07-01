package com.example.advancepizza;

import androidx.appcompat.app.AppCompatActivity;

        import android.os.Bundle;

        import com.example.advancepizza.ui.profile.ProfileFragment;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
       /* if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ProfileFragment.newInstance())
                    .commitNow();
        }*/
    }
}