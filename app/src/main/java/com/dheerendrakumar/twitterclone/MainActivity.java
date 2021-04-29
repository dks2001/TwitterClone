package com.dheerendrakumar.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText username;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ParseUser.getCurrentUser() != null) {
            //ParseUser.getCurrentUser().logOut();
            transitionToSocialMediaActivity();
        }

        setTitle("Sign Up");

        email = findViewById(R.id.edtSignupEmail);
        username = findViewById(R.id.edtSignupUsername);
        password = findViewById(R.id.edtSignupPassword);

        findViewById(R.id.loginTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });




        findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rootLayoutTapped(v);

                if(email.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Email Id is required !!", Toast.LENGTH_SHORT).show();
                }
                else if(username.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Username is required !!" , Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Password is required !!" , Toast.LENGTH_SHORT).show();
                }
                else
                {

                    ParseUser appUser = new ParseUser();
                    appUser.setEmail(email.getText().toString());
                    appUser.setUsername(username.getText().toString());
                    appUser.setPassword(password.getText().toString());

                    ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Signing up " + username.getText().toString());
                    progressDialog.show();

                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {
                                transitionToSocialMediaActivity();
                                Toast.makeText(MainActivity.this, "Successfully Sign up", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(MainActivity.this, "Sign up failed Try Again !!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.i("error", e.getMessage());
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });

    }


    public void rootLayoutTapped(View view) {

        try {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void transitionToSocialMediaActivity() {

        Intent intent = new Intent(MainActivity.this,SocialMediaActivity.class);
        startActivity(intent);
        finish();
    }
}