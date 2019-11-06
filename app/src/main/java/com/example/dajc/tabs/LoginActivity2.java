package com.example.dajc.tabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.*;

import com.example.dajc.tabs.login_helper.LoginUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class LoginActivity2 extends AppCompatActivity {
    private TextView mUsername;
    private TextView mPassword;
    private Button mLogin;
    private Button mRegister;
    private TextView mError_Message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        mUsername = findViewById(R.id.nomusager);
        mPassword = findViewById(R.id.motdepasse);
        mLogin = findViewById(R.id.bouton_log);
        mRegister = findViewById(R.id.new_account);
        mError_Message = findViewById(R.id.error_message_login);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    LoginUser loginUser = new LoginUser();
                    loginUser.execute(mUsername.getText().toString(), mPassword.getText().toString());
                    String response = loginUser.get();
                    System.out.println(response);

                    JSONObject reader = new JSONObject(response);

                    if(reader.has("token")){
                        String token = reader.getString("token");
                        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                        preferences.edit().putString("TOKEN",token).apply();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra(EXTRA_MESSAGE, "Yess!");
                        startActivity(intent);
                    } else {
                        mError_Message.setText("Veuillez verifier votre nom d'utilisateur \n ou mot de passe");
                        mError_Message.setVisibility(View.VISIBLE);
                    }


                } catch (ExecutionException e){
                    e.printStackTrace();
                } catch (InterruptedException e){
                    e.printStackTrace();
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent myIntent = new Intent(LoginActivity2.this, RegisterActivity.class);
                startActivity(myIntent);
            }
        });


    }

}
