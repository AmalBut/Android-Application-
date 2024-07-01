package com.example.advancepizza;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

public class SignupAdminActivity extends AppCompatActivity {
    Intent intent;
    EditText pessword_text2;
    FragmentActivity activity;
    EditText confirm_pass;
    EditText firstname;
    EditText lastname;
    EditText editTextPhone;
    EditText email_text2;
    SharedPrefManager sharedPrefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup_admin_activity);

        String[] options = {"Male", "Female"};
        final Spinner genderSpinner = (Spinner)
                findViewById(R.id.gender_spinner);
        ArrayAdapter<String> objGenderArr = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        genderSpinner.setAdapter(objGenderArr);

        TextView textCreateAccount = findViewById(R.id.textCreateAccount);
        email_text2 = findViewById(R.id.email_text2);
        confirm_pass = findViewById(R.id.confirm_pass);
        firstname = findViewById(R.id.editTextText2);
        editTextPhone = findViewById(R.id.editTextPhone);
        lastname = findViewById(R.id.editTextText);
        pessword_text2 = findViewById(R.id.pessword_text2);
        Button signup_button = findViewById(R.id.signup_button);
        Button back_button = findViewById(R.id.button_back);
        sharedPrefManager =SharedPrefManager.getInstance(this);
        intent = new Intent(SignupAdminActivity.this, HomeAdminActivity.class);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                finish();
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String confirmPassText = confirm_pass.getText().toString();
                String passwordText2Text = pessword_text2.getText().toString();

                if (validateAllFields()) {
                    if (confirmPassText.equals(passwordText2Text)) {

                                User newAdmin = new User();
                                if (email_text2.getText().toString().isEmpty())
                                    newAdmin.setEmail("No Email");
                                else
                                    newAdmin.setEmail(email_text2.getText().toString());
                                if (lastname.getText().toString().isEmpty())
                                    newAdmin.setLastName("No Name");
                                else newAdmin.setLastName(lastname.getText().toString());
                                if (firstname.getText().toString().isEmpty())
                                    newAdmin.setFirstName("No Name");
                                else newAdmin.setFirstName(firstname.getText().toString());
                                if (pessword_text2.getText().toString().isEmpty())
                                    newAdmin.setPassword("No Password");
                                else
                                    newAdmin.setPassword(Hash.hashPassword(pessword_text2.getText().toString()));
                                if (editTextPhone.getText().toString().isEmpty())
                                    newAdmin.setPhoneNumber("0");
                                else
                                    newAdmin.setPhoneNumber(editTextPhone.getText().toString());
                                newAdmin.setGender(genderSpinner.getSelectedItem().toString());
                                newAdmin.setPermission("Admin");

                                DataBaseHelperCustomer dataBaseHelper = MainActivity.getDatabaseHelper();
                                dataBaseHelper.insertCustomer(newAdmin);


                            Intent intent = new Intent(SignupAdminActivity.this, HomeAdminActivity.class);
                            startActivity(intent);
                            finish();
                        }


                     else {
                        Toast.makeText(SignupAdminActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }

    private boolean validateAllFields() {
        boolean isValid = true;

        if (!validateEmail(email_text2)) isValid = false;
        if (!validatePassword(pessword_text2)) isValid = false;
        if (!validatePassword(confirm_pass)) isValid = false;
        if (!validateName(firstname)) isValid = false;
        if (!validateName(lastname)) isValid = false;
        if (!validatePhone(editTextPhone)) isValid = false;
        return isValid;
    }

    private boolean validateEmail(EditText editText) {
        DataBaseHelperCustomer dataBaseHelper = MainActivity.getDatabaseHelper();
        String email = editText.getText().toString();

        if(dataBaseHelper.checkEmail(email)){
            editText.setError("Email is already exist");
            return false;
        }
        else if (!email.endsWith("@gmail.com")){
            editText.setError("Email must be an email and ends with @gmail.com");
            return false;

        }
        return true;

    }

    private boolean validatePassword(EditText editText) {
        String password = editText.getText().toString();

        // Check length and content using a single regex
        if (password.length() < 8 || !password.matches("^(?=.*[A-Za-z])(?=.*\\d).+$")) {
            editText.setError("Password must be at least 8 characters long and contain at least one letter and one number");
            return false;
        }

        return true;
    }

    private boolean validateName(EditText editText) {
        String password = editText.getText().toString();

        // Check length and content using a single regex
        if (password.length() < 3 ) {
            editText.setError("Name must be at least 3 characters long");
            return false;
        }

        return true;
    }

    private boolean validatePhone(EditText editText) {
        String phone = editText.getText().toString();
        if (phone.length() != 10 || !phone.startsWith("05")) {
            editText.setError("Phone number must be exactly 10 digits long and start with 05");
            return false;
        }
        return true;

    }

}
