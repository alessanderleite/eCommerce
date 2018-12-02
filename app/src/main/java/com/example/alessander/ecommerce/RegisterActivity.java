package com.example.alessander.ecommerce;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountButton;
    private EditText inputName, inputPhoneNumber, inputPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountButton = (Button) findViewById(R.id.register_btn);
        inputName = (EditText) findViewById(R.id.register_username_input);
        inputPassword = (EditText) findViewById(R.id.register_password_input);
        inputPhoneNumber = (EditText) findViewById(R.id.register_phone_number_input);
        loadingBar = new ProgressDialog(this);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAccount();
            }
        });
    }

    private void createAccount() {

        String name = inputName.getText().toString();
        String phone = inputPhoneNumber.getText().toString();
        String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(name)) {

            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone)) {

            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)) {

            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else {

            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            validatePhoneNumber(name, phone, password);
        }
    }

}
