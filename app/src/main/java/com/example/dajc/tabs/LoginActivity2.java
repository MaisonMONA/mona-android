package com.example.dajc.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.*;

import com.example.dajc.tabs.login_helper.LoginUser;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class LoginActivity2 extends AppCompatActivity {
    private TextView mUsername;
    private TextView mPassword;
    private Button mLogin;
    private Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        mUsername = findViewById(R.id.nomusager);
        mPassword = findViewById(R.id.motdepasse);
        mLogin = findViewById(R.id.bouton_log);
        mRegister = findViewById(R.id.new_account);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    LoginUser loginUser = new LoginUser();
                    loginUser.execute(mUsername.getText().toString(), mPassword.getText().toString());
                    String response = loginUser.get();
                    System.out.println(response);
                } catch (ExecutionException e){
                    e.printStackTrace();
                } catch (InterruptedException e){
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
