package com.example.advancepizza;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advancepizza.databinding.ActivityHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    private static DataBaseHelperCustomer dataBaseHelper;

    public static List<Pizza> Chicken = new ArrayList<>();
    public static List<Pizza> Beef = new ArrayList<>();
    public static List<Pizza> Veggies = new ArrayList<>();
    public static List<Pizza> Cheese = new ArrayList<>();
    public static List<Pizza> Seafood = new ArrayList<>();
    public static List<Pizza> Specialty = new ArrayList<>();

    public static List<Pizza> orderPizzas = new ArrayList<>();
    public static List<Pizza> favPizzas = new ArrayList<>();

    public static List<Pizza> allPizzas = new ArrayList<>();

    public static NavigationView navigationView;


    ImageView profImg;
    Button edit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBaseHelper =new DataBaseHelperCustomer(this);


        navigationView = findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
         navigationView = binding.navView;




        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.yourorders, R.id.pizzamenu,R.id.logout,R.id.profile,R.id.specialoffers,R.id.fav,R.id.callfind)
                .setOpenableLayout(drawer)
                .build();



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Cursor currentUser=User.currentUser;

            View headerView = HomeActivity.navigationView.getHeaderView(0);
            TextView navUsername= (TextView) headerView.findViewById(R.id.view_name);
            TextView navEmail= (TextView) headerView.findViewById(R.id.view_email);
            navUsername.setText(currentUser.getString(1) +" " +currentUser.getString(2));
            navEmail.setText(currentUser.getString(0));

        if(currentUser.getBlob(7)!=null) {
            ImageView img = (ImageView) headerView.findViewById(R.id.profileView);
            byte[] blobImage = currentUser.getBlob(7);
            Bitmap bitmap = BitmapFactory.decodeByteArray(blobImage, 0, blobImage.length);
            int newWidth = 200;
            int newHeight = 200;
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
            // Set the resized bitmap to the ImageView
            img.setImageBitmap(resizedBitmap);
        }

    }


    public static DataBaseHelperCustomer getDatabaseHelper() {
        return dataBaseHelper;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}