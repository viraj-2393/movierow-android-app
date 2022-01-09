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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.movies.movierow.API.Movies;
import com.movies.movierow.MainActivity;
import com.movies.movierow.Models.UserModel;
import com.movies.movierow.R;
import com.movies.movierow.Utils.OtpEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerifyOtp extends AppCompatActivity {
    TextView app_name;
    OtpEditText otpEditText;
    AppCompatButton verify_otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_verify_otp);

        app_name = findViewById(R.id.movie_row_logo_name);
        otpEditText = findViewById(R.id.otp);
        verify_otp = findViewById(R.id.verify_otp);

        String name = "<font color=#ffffff>movie</font><font color=#FF0000>ROW</font>";
        app_name.setText(Html.fromHtml(name,Html.FROM_HTML_MODE_LEGACY));

        verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = otpEditText.getText().toString();
                if(otp.length() < 6){
                    Toast.makeText(getApplicationContext(), "Please enter OTP!", Toast.LENGTH_SHORT).show();
                }
                else{
                    signup_the_user(otp);
                }

            }
        });
    }

    public void signup_the_user(String otp){
        String name = getIntent().getStringExtra("fullname");
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");
        String passwordConfirm = getIntent().getStringExtra("passwordConfirm");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fullname",name);
        jsonObject.addProperty("email",email);
        jsonObject.addProperty("password",password);
        jsonObject.addProperty("passwordConfirm",passwordConfirm);
        jsonObject.addProperty("otp",otp);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://fast-sierra-40787.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Movies user_signup = retrofit.create(Movies.class);

        Call<UserModel> call = user_signup.register(jsonObject);
        final ProgressDialog dialog = ProgressDialog.show(VerifyOtp.this, "",
                getResources().getString(R.string.loading), true);
        dialog.setCancelable(false);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.code() == 201) {
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
                    Toast.makeText(getApplicationContext(),"Unable to Reach Server. "+response.code(),Toast.LENGTH_LONG).show();
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