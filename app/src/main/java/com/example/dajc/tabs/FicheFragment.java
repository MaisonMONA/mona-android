package com.example.dajc.tabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static java.lang.Integer.parseInt;

/**
 * Created by DAJC on 2016-04-17.
 */
public class
FicheFragment extends Fragment implements View.OnClickListener {

    static final int REQUEST_IMAGE_PICTURE = 1;
    OeuvreObject object;
    int numOeuvre;
    int etat_o;
    ArrayList<OeuvreObject> oeuvreList= new ArrayList<>();
    TextView title;
    TextView author;
    TextView date;
    TextView infos;
    TextView date_ajout;
    ImageView photo;
    ImageButton fav_b;
    ImageButton map_b;
    ImageButton cam_b;
    String lastUpdate;
    EditText userC;
    RatingBar ratingBar;
    String titre_o;
    String tech_nbr;
    String cat_nbr;
    String quart_nbr ;
    String mat_nbr ;
    String dimension_o ;
    String uri_photo ;
    String date_oeuvre;
    String o_artistes;
    String dimension;
    String tech_oeuvre ;
    String cat_oeuvre ;
    String quart_oeuvre;
    String mat_oeuvre;
    String user_comment;
    int user_rating;
    GeoPoint startPoint;
    MapView map;
    GeoPoint myLocation;
    IMapController mapController;
    ItemizedIconOverlay<OverlayItem> overlayL;
    int idDuJour;

    String username;
    String password;
    SharedPreferences changes;



    public FicheFragment (){
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        Random rand = new Random(dayOfYear);
        //int idDuJour = rand.nextInt(FirstActivity.oeuvreList.size());
        //numOeuvre = Integer.parseInt(FirstActivity.oeuvreList.get(idDuJour).getId());
        oeuvreList = FirstActivity.getOeuvreList();
        View v;

        if(false || oeuvreList.get(idDuJour).getURI().equals("")){
            v = inflater.inflate(R.layout.fiche_noimg2_test, container, false);
            fav_b = (ImageButton) v.findViewById(R.id.button_fav);
            fav_b.setOnClickListener(this);


        }
        else{
            v = inflater.inflate(R.layout.fiche_img2, container, false);
            photo = (ImageView) v.findViewById(R.id.photo);
            date_ajout = (TextView) v.findViewById(R.id.tv_date);
            userC = (EditText) v.findViewById(R.id.user_comment);
            ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
            ratingBar.setOnClickListener(this);
            //changes = PreferenceManager.getDefaultSharedPreferences(getActivity());

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {
                    /*SharedPreferences.Editor editor = changes.edit() ;
                    editor.putBoolean("rating", true);
                    editor.commit();*/
                    object.setNote((int) rating);
                    new ServerUpdate("note",""+rating,"addNote").execute();
                    new updateOeuvre().execute(object);

                }
            });


            userC.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    /*SharedPreferences.Editor editor = changes.edit() ;
                    editor.putBoolean("comment", true);
                    editor.commit();*/
                    object.setCommentaire(String.valueOf(s));
                    new ServerUpdate("comment",""+String.valueOf(s),"addComment").execute();
                    new updateOeuvre().execute(object);
                }

            });
        }
        map = (MapView) v.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //set default zoom buttons and ability to zoom with fingers
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);

        //set map default view point
        mapController = map.getController();
        mapController.setZoom(15);
        title = (TextView) v.findViewById(R.id.titre);
        author = (TextView) v.findViewById(R.id.artiste);
        date = (TextView) v.findViewById(R.id.date);
        infos = (TextView) v.findViewById(R.id.info);
        map_b = (ImageButton) v.findViewById(R.id.button_map);
        cam_b = (ImageButton) v.findViewById(R.id.button_cam);
        cam_b.setOnClickListener(this);
        map_b.setOnClickListener(this);
        new getOeuvre().execute();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        new getOeuvre().execute();
        Log.d("settings", " today = " + idDuJour);
    }
    public void finishUI(){
        if (uri_photo.equals("")) {
            //Picasso.with(getContext()).load(uri_photo).resize(500, 888).into(photo);
            //photo.setVisibility(View.VISIBLE);
        }
        else {
            Bitmap bmImg = BitmapFactory.decodeFile(uri_photo);
            System.out.println("uri "+ uri_photo);
            System.out.println("etat "+ object.getEtat());
            photo.setImageBitmap(bmImg);
        }

        System.out.println("etat_o = " +etat_o);
        //si l'oeuvre est dans les favoris, le bouton n'est "pas actif" donc blanc
        //etat_o egal 1 sur favori, 2 si dans la gallerie
        if (etat_o == 1) {
            fav_b.setBackgroundResource(R.mipmap.ic_favorite_active);
        }
        else if (etat_o==0){
            fav_b.setBackgroundResource(R.mipmap.ic_favorite_passive);
        }
        //si l'oeuvre est dans la galerie, on ne peut pas prendre de photo ou l'ajouter aux favoris
        //par contre, on affiche la date à laquelle la photo a été prise
        if (etat_o == 2) {

            cam_b.setVisibility(View.VISIBLE);
            date_ajout.setVisibility(View.VISIBLE);
            userC.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);

            //date de la photo de l'utilisateur
            String date_photo = object.getDatedePhoto();
            date_ajout.setText("photo du " + date_photo);

            //clickListener pour agrandir la photo
            photo.setOnClickListener(this);

            if (!user_comment.equals("")) {
                userC.setText(user_comment);
            }
            if (user_rating != -1) {
                ratingBar.setRating((float) user_rating);
            }

        } else {
        }
    }

    public void loadFiche() {
        //get content for the Fiche
        titre_o = object.getTitre();
        tech_nbr = object.getTitre();
        cat_nbr = object.getTitre();
        quart_nbr = object.getTitre();
        mat_nbr = object.getTitre();
        dimension_o =object.getDimension();
        uri_photo = object.getURI();
        date_oeuvre = object.getDatedeCreation();
        user_comment = object.getCommentaire();
        user_rating = object.getNote();
        etat_o = object.getEtat();
        //set title
        title.setText(titre_o);
        //String o_artistes = oeuvreList.get(numOeuvre).getArtiste().getNom();
        String o_artistes = object.getArtiste();
        author.setText(o_artistes);
        date.setText(date_oeuvre);
        //dimensions de l'oeuvre
        String dimension = dimension_o;
        String tech_oeuvre = object.getTechnique();
        String cat_oeuvre = object.getTitre();
        String quart_oeuvre = object.getQuartier();
        String mat_oeuvre = object.getMateriaux();
        String information = "Quartier: " + quart_oeuvre + "\n" + "Dimensions: " + dimension + "\n"
                +  "Technique: " + tech_oeuvre + "\n"
                + "Matériau: " + mat_oeuvre;
        infos.setText(information);
        if(object.getURI().equals("")){
            ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
            String idItem = object.getId();
            double art_lati = object.getLocationX();
            double art_longi = object.getLocationY();
            OverlayItem myOverlayItem = new OverlayItem(idItem, titre_o, object.getArtiste(), new GeoPoint(art_lati, art_longi));
            Drawable artMarker;
            if(object.getEtat()==2)
                artMarker = getResources().getDrawable(R.drawable.ic_pingold);
            else if(object.getEtat()==1)
                artMarker = getResources().getDrawable(R.drawable.ic_pingreen);
            else
                artMarker = getResources().getDrawable(R.drawable.ic_pinblue);
            myOverlayItem.setMarker(artMarker);
            items.add(myOverlayItem);
            ItemizedIconOverlay.OnItemGestureListener iOverlay = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                //ItemizedIconOverlay iOverlay = new ItemizedIconOverlay(overlay, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                @Override
                public boolean onItemSingleTapUp(int index, OverlayItem item) {
                    //show the state
                    return false;
                }

                @Override
                public boolean onItemLongPress(final int index, OverlayItem item) {
                    return false;
                }

            };
            ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items, iOverlay, getContext());
            mOverlay.setFocusItemsOnTap(true);
            map.getOverlays().add(mOverlay);
            startPoint = new GeoPoint(art_lati, art_longi);
            mapController.setCenter(startPoint);
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println(requestCode == REQUEST_IMAGE_PICTURE && resultCode !=0);
        if(requestCode == REQUEST_IMAGE_PICTURE && resultCode !=0){

            Bundle extras = data.getExtras();
            //oeuvreList = FirstActivity.oeuvreList;
            Bitmap imageBitmap = (Bitmap)extras.get("data");
            //new getOeuvre().execute();
            /*if(imageBitmap != null) {
                photo.setImageBitmap(imageBitmap);
            }
*/

            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            try {
                File f=File.createTempFile(
                        "MTL_ART_" + numOeuvre + "_", //prefix
                        ".jpg",        //suffix
                        storageDir      //directory
                );
                String currentPath = f.getAbsolutePath();
                Log.d("photo", "image : " + currentPath);
                FileOutputStream fout= new FileOutputStream(currentPath);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);

                String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());

                //Log.d("photo", "dateTime stamp is "+timeStamp);
/*
                dbh.ajoutePhoto(numOeuvre, currentPath, timeStamp);
                dbh.changeEtat(numOeuvre, dbh.ETAT_GALERIE);
*/              System.out.println("etat_o set to 2");
                object.setDatedePhoto(timeStamp);
                object.setURI(currentPath);
                object.setEtat(2);
                new updateOeuvre().execute(object);
                /*Intent refresh = new Intent(getContext(), MainActivity.class);
                startActivity(refresh);//Start the same Activity
                getActivity().finish();*/
                /*MainActivity.oeuvreDuJour=false;
                /*Fragment currentFragment = getFragmentManager().findFragmentByTag("FicheFragment");
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.detach(currentFragment);
                fragmentTransaction.attach(currentFragment);
                fragmentTransaction.commit();*/
                /*FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(this).attach(new FicheFragment()).commit();*/
                FragmentManager fm = getFragmentManager();

                if (fm != null) {
                    // Perform the FragmentTransaction to load in the list tab content.
                    // Using FragmentTransaction#replace will destroy any Fragments
                    // currently inside R.id.fragment_content and add the new Fragment
                    // in its place.
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.pager, new FicheFragment());
                    ft.commit();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.button_fav:

                if (etat_o==0){

                    //méthode qui change l'état dans la base de données
                    object.setEtat(1);
                    new updateOeuvre().execute(object);
                    Toast.makeText(getActivity(), "Oeuvre ajoutée aux favoris", Toast.LENGTH_SHORT).show();
                }
                else if (etat_o==1){

                    object.setEtat(0);
                    new updateOeuvre().execute(object);
                    Toast.makeText(getActivity(), "Oeuvre retirée des favoris", Toast.LENGTH_SHORT).show();

                }
                else{
                    Log.d("listViewCursorAdaptor", "on ne peut pas changer l'état: " + etat_o);

                }
                new getOeuvre().execute();
                /*MainActivity.oeuvreDuJour=false;
                Intent refresh = new Intent(getContext(), MainActivity.class);
                refresh.putExtra("List", oeuvreList);
                refresh.putExtra("numOeuvre",numOeuvre);
                startActivity(refresh);//Start the same Activity
                getActivity().finish();
*/
                break;
            case R.id.button_map:

                /*intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("numOeuvre", numOeuvre);
                intent.putExtra("List", oeuvreList);
                startActivity(intent);*/
                //MainActivity.MapFrag(0);
                break;

            case R.id.button_cam:float range = 75;//metres
                if(FichePopUpFragment.inRange(MainActivity.lati,MainActivity.longi,object.getLocationX(),object.getLocationY(),range)){
                if (!MainActivity.Permission.checkPermissionForCamera()) {
                    MainActivity.Permission.requestPermissionForCamera();
                }
                else {
                    if (!MainActivity.Permission.checkPermissionForExternalStorage()) {
                        MainActivity.Permission.requestPermissionForExternalStorage();
                    }
                    else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        /*File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                        if (!mediaStorageDir.exists()) {
                            mediaStorageDir.mkdirs();
                        }

                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                Locale.getDefault()).format(new Date());
                        try {
                            File mediaFile = File.createTempFile(
                                    "IMG_" + timeStamp,
                                    ".jpg",
                                    mediaStorageDir
                            );
                            //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));*/
                            MainActivity.oeuvreDuJour=false;
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_PICTURE);
                            break;
                         /*catch (IOException e) {
                            e.printStackTrace();
                        }*/
                    }
                }}
                else Toast.makeText(getActivity(),
                            "vous devez vous rapprocher",
                            Toast.LENGTH_SHORT).show();
                    break;


/*
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra("List", oeuvreList);
                startActivityForResult(intent, REQUEST_IMAGE_PICTURE);}}
                break;
*/
            case R.id.photo:
                intent = new Intent (getActivity(), PhotoActivity.class);
                intent.putExtra("numOeuvre", numOeuvre);
                startActivity(intent);
                break;

            case R.id.ratingBar:
                Toast.makeText(getActivity(),
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
    public class getOeuvre extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("Isnt null anymore"+ numOeuvre);
            //TODO: etudier le path de FirstActivity.getDb().getOeuvreDao().verifyID(String.valueOf(numOeuvre))
            //Ligne suivante a ete enlever/mit en commentaire car elle ne va chercher rien
            //oeuvreList = new ArrayList<OeuvreObject>(FirstActivity.getDb().getOeuvreDao().verifyID(String.valueOf(numOeuvre)));
            object = oeuvreList.get(0);
            ArrayList<userObject> userobjects = new ArrayList<userObject>(FirstActivity.getDb().getUserDao().getUser());
//            username = userobjects.get(0).getUser();
//            password = userobjects.get(0).getPw();
           // System.out.println("ID object = " + object.getId());
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            loadFiche();
            finishUI();
        }
    }


    public class ServerUpdate extends AsyncTask<Void, Void, Boolean> {

        String Attribute;
        String Value;
        String Request;

        ServerUpdate(String attribute,String value,String request) {
            Attribute = attribute;
            Value = value;
            Request = request;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            if(comm()){
                return true;}
            else return false;}



        protected boolean comm() {
            boolean success = false;
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            //String url = "http://donnees.ville.montreal.qc.ca/dataset/2980db3a-9eb4-4c0e-b7c6-a6584cb769c9/resource/18705524-c8a6-49a0-bca7-92f493e6d329/download/oeuvresdonneesouvertes.json";
            //String url ="www-etud.iro.umontreal.ca/~beaurevg/ift3150/server/?request=loadJson";
            //String jsonStr = sh.makeServiceCall(url);
            StringBuilder result = new StringBuilder();
            URL url = null;
            try {
                url = new URL(
                        "http://www-etud.iro.umontreal.ca/~beaurevg/ift3150/server/?request="+Request+"&username="+
                                username+
                                "&password="+password+"&"+
                                Attribute+"="+Value+"&"+
                                "IDOeuvre="+numOeuvre);
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

            //Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try{
                    JSONArray oeuvres = new JSONArray("["+jsonStr+"]");

                    JSONObject c = oeuvres.getJSONObject(0);
                    success =c.getBoolean("successful");
                    System.out.println("nani"+success);

                }catch (JSONException e) {
                    e.printStackTrace();
                }
/*
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
                //Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });*/
            }

            return success;
        }
    }


}
