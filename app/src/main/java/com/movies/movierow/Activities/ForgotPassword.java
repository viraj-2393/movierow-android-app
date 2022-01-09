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
import android.widget.LinearLayout;
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
import retrofit2.http.FormUrlEncoded;

public class ForgotPassword extends AppCompatActivity {
    TextView app_name;
    OtpEditText otpEditText;
    AppCompatButton resetPassword,send_otp;
    EditText email,pass,cnf_pass;
    LinearLayout getOtp,resetPasswordContainer;
    String user_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_forgot_password);

        app_name = findViewById(R.id.movie_row_logo_name);
        otpEditText = findViewById(R.id.check_if_otp_correct);
        resetPassword = findViewById(R.id.reset_password);
        email = findViewById(R.id.check_if_email_exists);
        send_otp = findViewById(R.id.send_reset_password_otp);
        getOtp = findViewById(R.id.get_reset_password_otp_container);
        resetPasswordContainer = findViewById(R.id.send_reset_password_otp_container);
        pass = findViewById(R.id.reset_password_raw);
        cnf_pass = findViewById(R.id.reset_confirm_password);

        String name = "<font color=#ffffff>movie</font><font color=#FF0000>ROW</font>";
        app_name.setText(Html.fromHtml(name,Html.FROM_HTML_MODE_LEGACY));

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_email = email.getText().toString();
                if(isValidEmail(user_email)){
                    sendResetPassword(user_email);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please enter a valid email address!",Toast.LENGTH_LONG).show();
                }
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = otpEditText.getText().toString();
                String password = pass.getText().toString();
                String confirmPassword = cnf_pass.getText().toString();
                if(validateInput(otp,password,confirmPassword)){
                    resetUserPassword(user_email,password,confirmPassword,otp);
                }

            }
        });
    }
    private void sendResetPassword(String email){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email",email);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://fast-sierra-40787.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Movies reset_otp = retrofit.create(Movies.class);

        Call<Object> call = reset_otp.sendResetPasswordOtp(jsonObject);
        final ProgressDialog dialog = ProgressDialog.show(ForgotPassword.this, "",
                getResources().getString(R.string.loading), true);
        dialog.setCancelable(false);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.code() == 201) {
                    getOtp.setVisibility(View.GONE);
                    resetPasswordContainer.setVisibility(View.VISIBLE);


                }
                else{
                    Toast.makeText(getApplicationContext(),"Unable to Reach Server."+response.code(),Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetUserPassword(String email,String password,String passwordConfirm,String otp){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email",email);
        jsonObject.addProperty("password",password);
        jsonObject.addProperty("passwordConfirm",passwordConfirm);
        jsonObject.addProperty("otp",otp);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://fast-sierra-40787.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Movies reset_user_password = retrofit.create(Movies.class);

        Call<Object> call = reset_user_password.resetUserPass(jsonObject);
        final ProgressDialog dialog = ProgressDialog.show(ForgotPassword.this, "",
                getResources().getString(R.string.loading), true);
        dialog.setCancelable(false);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.code() == 200) {
                    Toast.makeText(getApplicationContext(),"Password changed successfully!",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Something went wrong! "+response.code(),Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput(String otp,String password,String confirmPassword){
        if(otp.length() < 6 || password.equals("") || confirmPassword.equals("")){
            return false;
        }else {
            if(pass.length() <= 8){pass.setError("Password must be greater than 8 characters!"); return false;}
            if(cnf_pass.length() <= 8){cnf_pass.setError("Password must be greater than 8 characters!"); return false;}
            if(!password.equals(confirmPassword)){cnf_pass.setError("Password must be same!"); return false;}
            return true;
        }

    }

    //email id validator
    public boolean isValidEmail(String target){
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    //-----------
}