package com.movies.movierow.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.movies.movierow.API.Movies;
import com.movies.movierow.MainActivity;
import com.movies.movierow.Models.UserModel;
import com.movies.movierow.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    TextView app_name;
    EditText email;
    EditText password;
    AppCompatButton signin;
    TextView signup;
    TextView forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_loign);

        app_name = findViewById(R.id.movie_row_logo_name);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup_text);
        forgot_password = findViewById(R.id.forgot_your_password);


        String name = "<font color=#ffffff>movie</font><font color=#FF0000>ROW</font>";
        app_name.setText(Html.fromHtml(name,Html.FROM_HTML_MODE_LEGACY));

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user_email = email.getText().toString();
                final String user_pass = password.getText().toString();
                if(user_email.equals("") || user_pass.equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter details correctly!",Toast.LENGTH_LONG).show();
                }
                else{
                    login(user_email,user_pass);
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SignupActivity.class));
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
            }
        });
    }

    private void login(String email,String password){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email",email);
        jsonObject.addProperty("password",password);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://fast-sierra-40787.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Movies user_login = retrofit.create(Movies.class);

        Call<UserModel> call = user_login.login(jsonObject);
        final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "",
                getResources().getString(R.string.loading), true);
        dialog.setCancelable(false);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.code() == 200) {
                    UserModel user = response.body();
                    SharedPreferences sharedPreferences = getSharedPreferences("User",MODE_PRIVATE);
                    SharedPreferences.Editor userEditor = sharedPreferences.edit();
                    userEditor.putString("token",user.getToken());
                    userEditor.putString("name",user.getUser().getFullname());
                    userEditor.putString("email",user.getUser().getEmail());
                    userEditor.apply();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Unable to Reach Server."+response.code(),Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}