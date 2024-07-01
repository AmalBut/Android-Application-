package com.example.advancepizza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    Intent intent1,intent2;
    SharedPrefManager sharedPrefManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText passwordedittext, emailedittext;
    CheckBox remember;
    DataBaseHelperCustomer databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signin_activity);


        Button back_button = findViewById(R.id.back_log);
        Button signup_button = findViewById(R.id.signup_bt);
        Button signin_button = findViewById(R.id.signin_bt);
        passwordedittext = findViewById(R.id.password_text);
         emailedittext = findViewById(R.id.email_text);
         remember=findViewById(R.id.remeber_box);
        intent1 = new Intent(LoginActivity.this,LoginRegistrationAct.class);
        intent2 = new Intent(LoginActivity.this, SignupActivity.class);
        sharedPrefManager=SharedPrefManager.getInstance( this);

        back_button.setOnClickListener(new View.OnClickListener() {
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

        sharedPreferences=getSharedPreferences("Login", MODE_PRIVATE);
        loadPreferences();
        String rememberemail="";
        signin_button.setOnClickListener(new View. OnClickListener(){

            @Override
            public void onClick(View view) {
                String email = emailedittext.getText().toString();
                String password = passwordedittext.getText().toString();


                databaseHelper = new DataBaseHelperCustomer(LoginActivity.this);
                Cursor cursor = databaseHelper.getUserByEmail(email);
                //intent
                boolean rememberMe = remember.isChecked();

                if(email.equals("")||password.equals(""))
                    Toast.makeText(LoginActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else {
                    if (cursor.getCount() == 0) {
                        Toast.makeText(LoginActivity.this, "Email is not found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        cursor.moveToFirst();
                        Boolean checkCredentials = databaseHelper.checkEmailPassword(email, Hash.hashPassword(password));
                     if (checkCredentials) {
                        Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                        if (rememberMe) {
                            sharedPrefManager.writeString("email", email);
                        }
                        Cursor user = databaseHelper.getUserByEmail(email);
                        user.moveToFirst();
                        if((user.getString(6)).equals("Customer")) {
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else if((user.getString(6)).equals("Admin")) {
                            Intent intent = new Intent(LoginActivity.this, HomeAdminActivity.class);
                            startActivity(intent);
                            finish();
                        }

                     } else {
                        Toast.makeText(LoginActivity.this, "Login FAILED!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                }

                User.currentUser = cursor;
            }
        });

        rememberemail=sharedPrefManager.readString("email","");
        emailedittext.setText(rememberemail);

    }

    private void loadPreferences() {
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);



        emailedittext.setText(email);
        passwordedittext.setText(password);
        remember.setChecked(rememberMe);
    }


}