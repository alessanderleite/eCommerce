package com.example.alessander.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alessander.ecommerce.model.Users;
import com.example.alessander.ecommerce.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText inputPhoneNumber, inputPassword;
    private Button loginButton;
    private ProgressDialog loadingBar;
    private TextView adminLink, notAdminLink;

    private String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_btn);
        inputPassword = (EditText) findViewById(R.id.login_password_input);
        inputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        adminLink = (TextView) findViewById(R.id.admin_panel_link);
        notAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginButton.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginButton.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }

    private void loginUser() {


        String phone = inputPhoneNumber.getText().toString();
        String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(phone)) {

            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)) {

            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else {

            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAccessToAccount(phone, password);
        }
    }

    private void allowAccessToAccount(final String phone, final String password) {

        if (chkBoxRememberMe.isChecked()) {

            Paper.book().write(Prevalent.userPhoneKey, phone);
            Paper.book().write(Prevalent.userPasswordKey, password);
        }

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(parentDbName).child(phone).exists()) {

                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone)) {

                        if (usersData.getPassword().equals(password)) {

                            if (parentDbName.equals("Admins")) {

                                Toast.makeText(LoginActivity.this,"logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Users")) {

                                Toast.makeText(LoginActivity.this,"Welcome Admin, your are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminAddNewProductActivity.class);
                                startActivity(intent);
                            }
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this,"Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {

                    Toast.makeText(LoginActivity.this,"Account with this " + phone + " number do not exists.",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
