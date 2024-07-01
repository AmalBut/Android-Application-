package com.example.advancepizza;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.advancepizza.ui.allorderes.AllOrdersFragment;
import com.example.advancepizza.ui.calculation.CalculationFragment;
import com.example.advancepizza.ui.profile.ProfileFragment;
import com.google.android.material.navigation.NavigationView;

public class HomeAdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;//
    public Toolbar toolbar;

    Intent intent2, intent3;

    public static NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        navigationView = findViewById(R.id.nav_view_admin);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername= (TextView) headerView.findViewById(R.id.view_name);
        TextView navEmail= (TextView) headerView.findViewById(R.id.view_email);
        navUsername.setText(User.currentUser.getString(0) +" " +User.currentUser.getString(1));
        navEmail.setText(User.currentUser.getString(3));

        // Get the Menu from the NavigationView
        Menu menu = navigationView.getMenu();

        toolbar=findViewById(R.id.toolbarAdmin);
        toolbar.setTitle("PROFILE");
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.drawer_layout_admin);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin,new ProfileFragment()).commit();
        navigationView.setCheckedItem(R.id.navAdmin_profile);
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        intent2 = new Intent(this, SignupAdminActivity.class);
        intent3 = new Intent(this, AddOfferActivity.class);


        if (item.getItemId()==R.id.navAdmin_profile){
            toolbar.setTitle("PROFILE");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin,new ProfileFragment()).commit();
        }
        if (item.getItemId()==R.id.navAdmin_viewAllOrders){
            toolbar.setTitle("VIEW ALL ORDERS");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin,new AllOrdersFragment()).commit();

        }
        if (item.getItemId()==R.id.navAdmin_calculate) {
            toolbar.setTitle("CALCULATIONS");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new CalculationFragment()).commit();
        }
        if (item.getItemId()==R.id.navAdmin_addAdmin){
            toolbar.setTitle("ADD ADMIN");
                    startActivity(intent2);
                    finish();
        }
        if (item.getItemId()==R.id.navAdmin_addSpecialOffer){
            toolbar.setTitle("ADD OFFER");
            startActivity(intent3);
            finish();

        }

        if (item.getItemId()==R.id.navAdmin_logout){

            StringBuilder details = new StringBuilder();
            details.append("Are you sure you want to logout?");

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeAdminActivity.this);
            builder.setTitle("Confirm Logout")
                    .setMessage(details.toString())
                    .setNegativeButton("cancel",null)
                    .setPositiveButton("sure", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(HomeAdminActivity.this, LoginActivity.class);
                            HomeAdminActivity.this.startActivity(intent);
                            finish();                        }
                    })
                    .create()
                    .show();


        }



        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Cursor currentUser=User.currentUser;

        View headerView = HomeAdminActivity.navigationView.getHeaderView(0);
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


}