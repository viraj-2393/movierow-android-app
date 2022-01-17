package com.movies.movierow.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.movies.movierow.API.Movies;
import com.movies.movierow.Models.UserModel;
import com.movies.movierow.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {
    TextView signin;
    EditText name;
    EditText email;
    EditText password;
    EditText passwordConfirm;
    AppCompatButton signup;
    TextView app_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_signup);

        app_name = findViewById(R.id.movie_row_logo_name);
        signin = findViewById(R.id.signin_text);
        name = findViewById(R.id.signup_username);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        passwordConfirm = findViewById(R.id.signup_confirm_password);
        signup = findViewById(R.id.signup);

        String name_of_the_app = "<font color=#ffffff>movie</font><font color=#FF0000>ROW</font>";
        app_name.setText(Html.fromHtml(name_of_the_app,Html.FROM_HTML_MODE_LEGACY));

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name = name.getText().toString();
                String user_email = email.getText().toString();
                String user_password = password.getText().toString();
                String user_passwordConfirm = passwordConfirm.getText().toString();
                if(validate(user_name,user_email,user_password,user_passwordConfirm)){
                    signup_the_user(user_name,user_email,user_password,user_passwordConfirm);
                }
            }
        });



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }


    public void signup_the_user(String name,String email,String password,String passwordConfirm){
        Intent intent = new Intent(getApplicationContext(),VerifyOtp.class);
        intent.putExtra("fullname",name);
        intent.putExtra("email",email);
        intent.putExtra("password",password);
        intent.putExtra("passwordConfirm",passwordConfirm);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email",email);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://fast-sierra-40787.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Movies send_otp = retrofit.create(Movies.class);

        Call<Object> call = send_otp.sendOtp(jsonObject);
        final ProgressDialog dialog = ProgressDialog.show(SignupActivity.this, "",
                getResources().getString(R.string.loading), true);
        dialog.setCancelable(false);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.code() == 201) {
                    Toast.makeText(getApplicationContext(),"OTP sent to your Email Address.",Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"The user already exists! "+response.code(),Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validate(String user_name,String user_email,String user_password,String user_passwordConfirm){
        boolean validEmail = !isValidEmail(user_email);
        if(user_name.equals("") || !user_password.equals(user_passwordConfirm) || user_password.length() <= 8 || validEmail){
            if(user_name.equals("")){name.setError("Please enter your name!");}
            if(validEmail){email.setError("Please enter a valid email id!");}
            if(user_password.length() <= 8){password.setError("Password must be greater than 8 characters!");}
            if(user_passwordConfirm.length() <= 8){passwordConfirm.setError("Password must be greater than 8 characters!");}
            if(!user_password.equals(user_passwordConfirm)){passwordConfirm.setError("Password must be same!");}
            return false;
        }
        return true;
    }

    //email id validator
    public boolean isValidEmail(String target){
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    //-----------



}