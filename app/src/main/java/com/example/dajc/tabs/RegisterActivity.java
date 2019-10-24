package com.example.dajc.tabs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.*;

import com.example.dajc.tabs.login_helper.RegisterUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.concurrent.ExecutionException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class RegisterActivity extends AppCompatActivity {

    private TextView mNom_usager_reponse;
    private TextView mPassword_reponse;
    private TextView mPassword_confirmation_reponse;
    private Button mButton_log;
    private TextView mError_message;
    private RegisterUser registerUser;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNom_usager_reponse = findViewById(R.id.new_nomusager);
        mPassword_reponse = findViewById(R.id.new_motdepasse);
        mPassword_confirmation_reponse = findViewById(R.id.new_motdepasse_confirmation);
        mButton_log = findViewById(R.id.bouton_log);
        mError_message = findViewById(R.id.error_message);


        mButton_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                try{
                    RegisterUser registerUser = new RegisterUser();
                    registerUser.execute(mNom_usager_reponse.getText().toString(), mPassword_reponse.getText().toString(), mPassword_confirmation_reponse.getText().toString());
                    String response = registerUser.get();
                    System.out.println(response);

                    JSONObject reader = new JSONObject(response);
                    if(reader.has("token")){
                        Intent intent = new Intent(getBaseContext(), LoginActivity2.class);
                        intent.putExtra(EXTRA_MESSAGE, "Yess!");
                        startActivity(intent);
                    }
                    if(reader.has("errors")){
                        JSONObject errors = reader.getJSONObject("errors");
                        if(errors.has("username")){
                            mError_message.setText("Le nom d'usager n'est pas disponible");
                            mError_message.setVisibility(View.VISIBLE);
                        } else if(errors.has("password")){
                            JSONArray password = errors.getJSONArray("password");
                            String words = "";
                            for (int i = 0; i < password.length(); i++){
                                words += (password.get(i) + "\n");
                            }
                            mError_message.setText(words);
                            mError_message.setVisibility(View.VISIBLE);
                        }

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



    }



}
