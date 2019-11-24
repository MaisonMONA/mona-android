package com.example.dajc.tabs;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetPlaces extends AsyncTask<String, String, String> {

    protected String doInBackground(String... params){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://172.20.35.199:8000/api/places")
                .build();

        try {

            Response response = client.newCall(request).execute();

            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }
}
