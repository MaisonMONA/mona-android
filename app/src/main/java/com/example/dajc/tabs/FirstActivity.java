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
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

/**
 * Created by LenaMK on 17/06/2016.
 */
public class FirstActivity extends Activity {//implements View.OnClickListener{

    public static ArrayList<OeuvreObject> getOeuvreList() {
        return oeuvreList;
    }
    static ArrayList<OeuvreObject> oeuvreList = new ArrayList<OeuvreObject>();
    public static ArrayList<BadgeObject> getBadgeList() {
        return badgeList;
    }
    static ArrayList<BadgeObject> badgeList = new ArrayList<BadgeObject>();
    public static AppDatabase db;
    public static AppDatabase getDb(){return db;}
    public boolean logged = true;
    int users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "oeuvre-database").fallbackToDestructiveMigration().build();

        /*try {
            new getlogged().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
/*
        if(users>0)
            logged=true;
        else logged = false;
*/
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
                new GetOeuvres().execute();
/*
            }
            else{
                System.out.println("No network and no data");
            }
            */
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(oeuvreList.size()>0&&logged){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        else if(oeuvreList.size()>0){
            System.out.println("nani1");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);}

    }

    private class GetOeuvres extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(),"Loading data",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            createList(null);
                createBadges();



            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            if(logged) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("OeuvreList", oeuvreList);
                startActivity(intent);
            }
            else{
                System.out.println("nani2");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("OeuvreList", oeuvreList);
                startActivity(intent);}
        }
    }
    private class getlogged extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {

            users = FirstActivity.getDb().getUserDao().numberofuser();
            return 1;
        }
    }

    private class UpdateOeuvres extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           Toast.makeText(getApplicationContext(),"Loading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            //String url = "http://donnees.ville.montreal.qc.ca/dataset/2980db3a-9eb4-4c0e-b7c6-a6584cb769c9/resource/18705524-c8a6-49a0-bca7-92f493e6d329/download/oeuvresdonneesouvertes.json";
            //String url ="www-etud.iro.umontreal.ca/~beaurevg/ift3150/server/?request=loadJson";
            //String jsonStr = sh.makeServiceCall(url);
            StringBuilder result = new StringBuilder();
            URL url = null;
            try {
                url = new URL("http://www-etud.iro.umontreal.ca/~beaurevg/ift3150/server/?request=loadJson1");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                conn.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            BufferedReader rd = null;
            try {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String line;
            try {
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                rd.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String jsonStr = result.toString();

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                createList(jsonStr);
                createBadges();

/*
                try {
                    Writer output = null;
                    File file = new File(getApplicationContext().getFilesDir() + "OeuvresData.json");
                    output = new BufferedWriter(new FileWriter(file));
                    output.write(jsonStr);
                    output.close();
                } catch (Exception e) {
                    Log.e(TAG, "File Saving error: " + e.getMessage());
                }*/
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
            if(logged){

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("List", oeuvreList);
                startActivity(intent);}
            else{
                System.out.println("nani3");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("OeuvreList", oeuvreList);
                startActivity(intent);}
            /*String param = "?Authorization=" + "allo";
            System.out.println("J<ao communnique au serveur");
            URL obj = null;
            try {
                obj = new URL( "http://www-etud.iro.umontreal.ca/~beaurevg/ift3150/server/?request=addComment&IDOeuvre=1&comment=test3&username=AndroidTesteur&password=1111");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn;
            try {
                 conn = (HttpURLConnection) obj.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader rd = null;
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    protected void createBadges(){
        badgeList = BadgeObject.getBadges();
        for (final BadgeObject badge : badgeList) {
            if(db.getBadgeDao().verifyID(badge.Id).isEmpty())
                db.getBadgeDao().insertAll(badge);
        }
        if(logged)
            logged=true;

    }

    protected void createList(String jsonStr) {
        oeuvreList.clear();
        if (jsonStr != null) {
            try {
                // Getting JSON Array node
                JSONArray oeuvres = new JSONArray(jsonStr);
                System.out.println(jsonStr.length());

                // looping through All Contacts
                for (int i = 0; i < oeuvres.length(); i++) {
                    JSONObject c = oeuvres.getJSONObject(i);
                    String id =c.getString("id");
                    if (db.getOeuvreDao().verifyID(id).isEmpty()) {
                        //System.out.println("not in bd");
                        String titre = c.getString("Titre");
                        if (titre.equals("null")&&Integer.parseInt(id)>999){
                            titre= "Non Titré (murale peinte)";
                        }
                        else if(titre.equals("null")){
                            titre= "Non Titré";
                        }
                        String date = c.getString("Date");
                        if (date.equals("null")||date.equals(""))
                        {
                            date="Date inconnue";
                        }
                        else {
                            date = treatDate(date);
                        }
                        //Calendar date = ((Calendar) c.getDouble("DateFinProduction"));
                        String materiaux  = c.getString("Materiaux");
                        if (!materiaux .equals("null")){
                        JSONArray materiauxArray = c.getJSONArray("Materiaux");
                        materiaux = treatTechnique(materiauxArray);}
                        else{
                            materiaux="";
                        }
                        /*if (materiaux.equals("")){
                            JSONArray techniqueArray = c.getJSONArray("Technique");
                            materiaux = treatTechnique(techniqueArray);}*/
                        //Rajouter le colet quartier dans le fichier JSON
                        String categorie = c.getString("Categorie");
                        String sousCategorie = c.getString("SousCategorie");
                        String quartier = c.getString("Arrondissement");
                        String dimension = c.getString("Dimension");
                        if (dimension.equals("null")){
                            dimension="";
                        }
                        String technique = c.getString("Technique");
                        if (!technique.equals("null")){
                        JSONArray techniqueArray = c.getJSONArray("Technique");
                        technique = treatTechnique(techniqueArray);}
                        else{
                            technique="";
                        }
                        JSONArray artisteArray = c.getJSONArray("Artiste");
                        JSONObject artisteinfo = artisteArray.getJSONObject(0);
                        String artiste_nom = artisteinfo.getString("Nom");
                        String artiste_id = artisteinfo.getString("ID");
                        String artiste_prenom = artisteinfo.getString("Prenom");
                        String artiste_collectif = artisteinfo.getString("NomCollectif");
                        double locX = c.getDouble("Latitude");
                        double locY = c.getDouble("Longitude");
                        ArtisteObject artiste;
                        OeuvreObject oeuvre;
                        if (artiste_collectif.equals("null")) {
                            artiste = new ArtisteObject(artiste_nom, artiste_prenom, artiste_id, false);
                            if (artiste_nom.equals("null")&&artiste_prenom.equals("null"))
                            {
                                oeuvre = new OeuvreObject(titre, id, categorie, sousCategorie,"Artiste inconnu", date, materiaux, locX, locY, 0, "", "", -1, quartier, dimension, technique);
                            }
                            else if (artiste_nom.equals("null")){
                                oeuvre = new OeuvreObject(titre, id,categorie, sousCategorie, artiste_prenom, date, materiaux, locX, locY, 0, "", "", -1, quartier, dimension, technique);

                            }
                            else if (artiste_prenom.equals("null")){
                                oeuvre = new OeuvreObject(titre, id,categorie, sousCategorie, artiste_nom, date, materiaux, locX, locY, 0, "", "", -1, quartier, dimension, technique);

                            }
                            else {
                                oeuvre = new OeuvreObject(titre, id,categorie, sousCategorie, artiste_prenom + " " + artiste_nom, date, materiaux, locX, locY, 0, "", "", -1, quartier, dimension, technique);
                            }
                        } else {
                            artiste = new ArtisteObject(artiste_collectif, artiste_prenom, artiste_id, true);
                                oeuvre = new OeuvreObject(titre, id,categorie, sousCategorie, artiste_collectif, date, materiaux, locX, locY, 0, "", "", -1, quartier, dimension, technique);

                        }
                        //OeuvreObject oeuvre = new OeuvreObject(titre, id, artiste_prenom+ " "+artiste_nom, date, materiaux, locX, locY, 0, "", "", -1, quartier, dimension, technique);
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
    public String treatArtiste(JSONArray s) throws JSONException {
        String list = "";
        for(int i = 0 ; i < s.length() ; i++){
            if (i<s.length()-1){
                list+=s.getJSONObject(i).getString("Nom")+", ";
            }
            else {
                list += s.getJSONObject(i).getString("Nom");
            }
        }
        return list;
    }
    public String treatTechnique(JSONArray s) throws JSONException {
            String list = "";
            for(int i = 0 ; i < s.length() ; i++){
                if (i<s.length()-1){
                    list+=s.getJSONObject(i).getString("Nom")+", ";
                }
                else {
                    list += s.getJSONObject(i).getString("Nom");
                }
            }
            return list;
    }
    public String treatDate(String s)
    {
        if (!s.equals("null")) {
            String[] temp = s.split("\\(");
            s = temp[1];
            temp = s.split("-");
            long doub;
            if (temp.length == 2) {
                doub = Long.parseLong(temp[0]);
            } else {
                doub = Long.parseLong(temp[1]);
                doub = doub * -1;
            }
            String date = new java.text.SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date(doub));
            return date;
        }
        return "erreur de date";
    }
}
