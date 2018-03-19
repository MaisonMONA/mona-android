package com.example.dajc.tabs;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dajc.tabs.WebAPI.RunAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by LenaMK on 17/06/2016.
 */
public class FirstActivity extends Activity {//implements View.OnClickListener{

    public static ArrayList<OeuvreObject> getOeuvreList() {
        return oeuvreList;
    }

    /**
    Button info;
    Button launch;
**/ static ArrayList<OeuvreObject> oeuvreList = new ArrayList<OeuvreObject>();
    public static AppDatabase db;
    public static AppDatabase getDb(){return db;}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.first_activity);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "oeuvre-database").build();

        //TODO faire les quatres cas... pour le moment seul trois cas fonctionnent
        if (isNetworkAvailable()) {
            /*if (fileExist((getApplicationContext().getFilesDir() + "OeuvresData.json")))
            {
                new GetOeuvres().execute();
            }
            else{*/
                new UpdateOeuvres().execute();
            //}
        }
        else{
            /*if (fileExist((getApplicationContext().getFilesDir() + "OeuvresData.json")))
            {*/
                System.out.println("No network");
                createList(getFile());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("List", oeuvreList);
                startActivity(intent);
/*
            }
            else{
                System.out.println("No network and no data");
            }
            */
        }



    }
    private class GetOeuvres extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(),"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://donnees.ville.montreal.qc.ca/dataset/2980db3a-9eb4-4c0e-b7c6-a6584cb769c9/resource/18705524-c8a6-49a0-bca7-92f493e6d329/download/oeuvresdonneesouvertes.json";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                createList(jsonStr);

                try {
                    Writer output = null;
                    File file = new File(getApplicationContext().getFilesDir() + "OeuvresData.json");
                    output = new BufferedWriter(new FileWriter(file));
                    output.write(jsonStr);
                    output.close();
                } catch (Exception e) {
                    Log.e(TAG, "File Saving error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("OeuvreList", oeuvreList);
            startActivity(intent);
        }
    }
    private class UpdateOeuvres extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(),"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://donnees.ville.montreal.qc.ca/dataset/2980db3a-9eb4-4c0e-b7c6-a6584cb769c9/resource/18705524-c8a6-49a0-bca7-92f493e6d329/download/oeuvresdonneesouvertes.json";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                createList(jsonStr);

                try {
                    Writer output = null;
                    File file = new File(getApplicationContext().getFilesDir() + "OeuvresData.json");
                    output = new BufferedWriter(new FileWriter(file));
                    output.write(jsonStr);
                    output.close();
                } catch (Exception e) {
                    Log.e(TAG, "File Saving error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("List", oeuvreList);
            startActivity(intent);
        }
    }
    public boolean fileExist(String fname){
/*
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
        */
        return false;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private String getFile()
    {
//Get the text file
        File file = new File(getApplicationContext().getFilesDir() + "OeuvresData.json");

//Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            System.out.println(text+ "ok");
            br.close();
        }
        catch (IOException e) {
            Log.e(TAG, "Couldn't get data from file.");//You'll need to add proper error handling here
            System.out.println("Failed");
        }

        return text.toString();
    }

    protected void createList(String jsonStr) {
        if (jsonStr != null) {
            oeuvreList.clear();
            try {
                // Getting JSON Array node
                JSONArray oeuvres = new JSONArray(jsonStr);
                System.out.println(jsonStr.length());

                // looping through All Contacts
                for (int i = 0; i < oeuvres.length(); i++) {

                    String id= String.valueOf(i);
                    if (db.getOeuvreDao().verifyID(id).isEmpty()) {
                        //System.out.println("not in bd");
                        JSONObject c = oeuvres.getJSONObject(i);
                                String titre = c.getString("Titre");
                        String date = c.getString("DateFinProduction");
                        String materiaux = c.getString("Materiaux");
                        //String id =c.getString("id");
                        //Rajouter le colet quartier dans le fichier JSON
                        String quartier = c.getString("Arrondissement");
                        String dimension = c.getString("DimensionsGenerales");
                        String technique = c.getString("Technique");
                        JSONArray artisteArray = c.getJSONArray("Artistes");
                        JSONObject artisteinfo = artisteArray.getJSONObject(0);
                        String artiste_nom = artisteinfo.getString("Nom");
                        String artiste_id = artisteinfo.getString("Prenom");
                        String artiste_prenom = artisteinfo.getString("NoInterne");
                        String artiste_collectif = artisteinfo.getString("NomCollectif");
                        double locX = c.getDouble("CoordonneeLatitude");
                        double locY = c.getDouble("CoordonneeLongitude");
                        ArtisteObject artiste;
                        if (artiste_collectif.equals("null")) {
                            artiste = new ArtisteObject(artiste_nom, artiste_prenom, artiste_id, false);
                        } else {
                            artiste = new ArtisteObject(artiste_collectif, artiste_prenom, artiste_id, true);
                        }

                        OeuvreObject oeuvre = new OeuvreObject(titre, id, artiste_nom, date, materiaux, locX, locY, 0, "", "", -1, quartier, dimension, technique);
                        //artiste.addOeuvres(oeuvre);
                        db.getOeuvreDao().insertAll(oeuvre);
                    }
                    else{
                        //System.out.println("Already in bd");
                    }
                    // adding contact to contact list
                }
                oeuvreList.addAll(db.getOeuvreDao().getAllOeuvre());
                //oeuvreList.add(oeuvre);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            oeuvreList.addAll(db.getOeuvreDao().getAllOeuvre());
        }
    }

}
