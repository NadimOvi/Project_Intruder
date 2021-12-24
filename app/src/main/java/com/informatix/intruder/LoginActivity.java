package com.informatix.intruder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.informatix.intruder.Api.Service;
import com.informatix.intruder.Model.LoginModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText userIdText, userPasswordText;
    private Button userLoginButton;
    ProgressDialog progressDialog;
    String userID,password;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("loginInfo",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        initialize();
        sharedPreferences = getSharedPreferences("loginInfo",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(sharedPreferences.contains("userID") && sharedPreferences.contains("password")
                && sharedPreferences.contains("name")&& sharedPreferences.contains("device_id")&& sharedPreferences.contains("valid_till")){
           Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
            startActivity(intent);
            finish();
        }

        userLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginValidation();
            }
        });


    }
    private boolean isConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;

    }
    private void initialize() {
        userIdText = findViewById(R.id.userIdText);
        userPasswordText = findViewById(R.id.userPasswordText);
        userLoginButton = findViewById(R.id.userLoginButton);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
    }
    private void loginValidation() {
        userID= userIdText.getText().toString().trim();;
        password= userPasswordText.getText().toString().trim();;

        if (userID.isEmpty()) {
            userIdText.setError("Please provide User ID");
            userIdText.requestFocus();
        }else if(password.isEmpty()){
            userPasswordText.setError("Please provide password");
            userPasswordText.requestFocus();
        }else{
            loginPost();
        }
    }

    private void loginPost() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://us.infrmtx.com/iot/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        Service service = retrofit.create(Service.class);
        Call<LoginModel> call = service.getLogin(userID,password);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful()){
                    LoginModel loginModel = response.body();

                    String name = loginModel.getName();
                    Integer device_id = loginModel.getDevice_id();
                    String valid_till = loginModel.getValid_till();

                    editor.putString("userID",userID);
                    editor.putString("password",password);
                    editor.putString("name",name);
                    editor.putString("device_id", String.valueOf(device_id));
                    editor.putString("valid_till",valid_till);
                    /*editor.apply();*/
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Response Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

