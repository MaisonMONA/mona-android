package com.example.dajc.tabs.login_helper;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginUser extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody.Builder()
                .add("username", params[0])
                .add("password", params[1])
                .build();
        Request request = new Request.Builder()
                .url("https://picasso.iro.umontreal.ca/~mona/api/login")
                .post(formBody)
                .build();
        try{
            Response response = client.newCall(request).execute();
            return response.body().string();

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

    }
}
