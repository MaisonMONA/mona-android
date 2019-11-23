package com.example.dajc.tabs;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

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
    public boolean logged;
    int users;

    /** Duration of wait **/
    //private final int SPLASH_DISPLAY_LENGTH = 5000; //splash screen will be shown for 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent mainIntent = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();

            }
        }, SPLASH_DISPLAY_LENGTH);
*/
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "oeuvre-database").fallbackToDestructiveMigration().build();

        /*try {
            new getlogged().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/

        if(users>0)
            logged=true;
        else logged = false;

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

            //PAGE = 14 pour linstant
            //TODO: args number of pages
            for(int page = 1; page <= 15; page++){
                StringBuilder result = new StringBuilder();
                URL url = null;
                try {
                    url = new URL("https://picasso.iro.umontreal.ca/~mona/api/artworks?page="+page);
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

                //parsing deja fait
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
                JSONObject oeuvres_data = new JSONObject(jsonStr);
                JSONArray oeuvres = oeuvres_data.getJSONArray("data");
                System.out.println("OEUVRE = "+oeuvres.length());

                // looping through All Contacts
                for (int i = 0; i < oeuvres.length(); i++) {

                    JSONObject c = oeuvres.getJSONObject(i);

                    String id = c.getString("id");

                    if (db.getOeuvreDao().verifyID(id).isEmpty()) {
                        //System.out.println("not in bd");

                        //titre
                        String title = c.getString("title");
                        if (title.equals("null")){
                            title = "sans nom";
                        }

                        //produced at
                        String produced_at = c.getString("produced_at");
                        if (produced_at.equals("null") || produced_at.equals(""))
                        {
                            produced_at = "NA";
                        }

                        //category
                        String category_fr = "NA";
                        String category_en = "NA";
                        if(!c.isNull("category")){
                            JSONObject category_object = c.getJSONObject("category");
                            category_fr = category_object.getString("fr");
                            category_en = category_object.getString("en");
                        }

                        //subcategory
                        String subcategory_fr = "NA";
                        String subcategory_en = "NA";
                        if(!c.isNull("subcategory")){
                            JSONObject subcategory_object = c.getJSONObject("subcategory");
                            subcategory_fr = subcategory_object.getString("fr");
                            subcategory_en = subcategory_object.getString("en");
                        }

                        /*
                        //dimension
                        String dimensions = c.getString("dimensions");
                        if (!dimensions.equals("[]")){
                            JSONArray dimension_array = c.getJSONArray("dimensions");
                            for (int k = 0; k < dimension_array.length(); k++){
                                if(k < dimension_array.length() - 1){
                                    dimensions += dimension_array.get(k) + "*";
                                } else {
                                    dimensions += dimension_array.get(k) + " cm";
                                }
                            }
                        } else {
                            dimensions = "NA";
                        }
                        Pendant que le serveur se regle*/
                        String dimensions = null;


                        //materials
                        String materials  = c.getString("materials");
                        if (!materials.equals("[]")){
                            JSONArray materials_array  = c.getJSONArray("materials");
                            materials = treatTechnique(materials_array);
                        } else{
                            materials = "NA";
                        }



                        //techniques
                        String techniques  = c.getString("techniques");
                        if (!techniques.equals("null")){
                            JSONArray techniques_array  = c.getJSONArray("techniques");
                            techniques = treatTechnique(techniques_array);
                        } else{
                            techniques = "NA";
                        }

                        //borough
                        String borough = c.getString("borough");

                        //location
                        JSONObject location_object = c.getJSONObject("location");
                        String lat_string = location_object.getString("lat");
                        String lng_string = location_object.getString("lng");
                        double lat = Double.parseDouble(lat_string);
                        double lng = Double.parseDouble(lng_string);



                        //A changer lorsque nous aurons artisteDAO

                        //On va uniquement chercher le premier artiste
                        JSONArray artists_array = c.getJSONArray("artists");
                        String artist_name = null;
                        Boolean artist_collective = null;
                        String artist_id = null;

                        if(artists_array.length() > 0) {
                            JSONObject temp_artist_info = (JSONObject) artists_array.get(0);
                            artist_id = temp_artist_info.getString("id");
                            artist_name = temp_artist_info.getString("name");
                            artist_collective = Boolean.parseBoolean(temp_artist_info.getString("collective"));
                        }

                        //Oeuvre uniquement en francais
                        OeuvreObject oeuvre = new OeuvreObject(title, id, category_fr, subcategory_fr, artist_name, produced_at, materials, lat, lng, 0, "", "", -1, borough, dimensions, techniques);

                        /*
                        JSONArray artists_array = c.getJSONArray("artists");
                        LinkedList<String[]> artists_list = new LinkedList();
                        for(int k = 0; k < artists_array.length(); k++){
                            JSONObject temp_artist_info = (JSONObject) artists_array.get(k);

                            String artist_id = temp_artist_info.getString("id");
                            String artist_name = temp_artist_info.getString("name");
                            Boolean artist_collective = Boolean.parseBoolean(temp_artist_info.getString("collective"));


                        }
                        */

                        /*
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
                        */
                        db.getOeuvreDao().insertAll(oeuvre);
                        //oeuvreList.add(oeuvre);

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
            for(int i = 0 ; i < s.length(); i++){

                JSONObject object = s.getJSONObject(i);
                String word = object.getString("fr");
                list += word;
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
