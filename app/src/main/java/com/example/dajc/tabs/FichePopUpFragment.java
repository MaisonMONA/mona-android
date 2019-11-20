package com.example.dajc.tabs;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class FichePopUpFragment extends DialogFragment implements View.OnClickListener{
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
    ImageButton close;
    String lastUpdate;
    EditText userC;
    RatingBar ratingBar;
    String titre_o;
    String tech_nbr;
    String cat_nbr;
    String quart_nbr;
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
    //ResourceProxy mResourceProxy;
    IMapController mapController;
    ItemizedIconOverlay<OverlayItem> overlayL;
    String username;
    String password;

    SharedPreferences changes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v;
        if (MainActivity.withImg==true) {
            v = inflater.inflate(R.layout.fiche_popup2, container, true);
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
                    //new ServerUpdate("comment",""+String.valueOf(s),"addComment").execute();
                    new updateOeuvre().execute(object);
                }
            });
        }
        else {
            v = inflater.inflate(R.layout.fiche_popup_noimg2, container, true);
            fav_b = (ImageButton) v.findViewById(R.id.button_fav);
            fav_b.setOnClickListener(this);

        }
        map = (MapView) v.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //set default zoom buttons and ability to zoom with fingers
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);

        //set map default view point
        mapController = map.getController();
        mapController.setZoom(15);
        oeuvreList = FirstActivity.getOeuvreList();
        numOeuvre = MainActivity.numOeuvre;
        System.out.println("OnCreateView");
        title = (TextView) v.findViewById(R.id.titre);
        author = (TextView) v.findViewById(R.id.artiste);
        date = (TextView) v.findViewById(R.id.date);
        infos = (TextView) v.findViewById(R.id.info);
        map_b = (ImageButton) v.findViewById(R.id.button_map);
        cam_b = (ImageButton) v.findViewById(R.id.button_cam);
        close = (ImageButton) v.findViewById(R.id.closebtn);
        //System.out.println(oeuvreList.get(2).getTitre());
        close.setOnClickListener(this);
        cam_b.setOnClickListener(this);
        map_b.setOnClickListener(this);
        new getOeuvre().execute();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();

    }

    public void finishUI(){
        //image de l'oeuvre ou par défaut
        if (uri_photo.equals("")) {
            //Picasso.with(getContext()).load(uri_photo).resize(500, 888).into(photo);
            //photo.setVisibility(View.VISIBLE);
        } else {
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
        cam_b.setMinimumWidth(cam_b.getHeight());
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

    @TargetApi(Build.VERSION_CODES.M)
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
        if(MainActivity.withImg==false || MainActivity.withImg==true){
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
                artMarker = getResources().getDrawable(R.drawable.ic_pinblue);            myOverlayItem.setMarker(artMarker);
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
            //mResourceProxy = new DefaultResourceProxyImpl(getContext());
            ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items, iOverlay, getContext());
            mOverlay.setFocusItemsOnTap(true);
            map.getOverlays().add(mOverlay);
            startPoint = new GeoPoint(art_lati, art_longi);
            mapController.setCenter(startPoint);
        }


    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap)extras.get("data");
        System.out.println(requestCode == REQUEST_IMAGE_PICTURE && resultCode !=0);
        if(requestCode == REQUEST_IMAGE_PICTURE && resultCode !=0){

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
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);

                String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());

                //Log.d("photo", "dateTime stamp is "+timeStamp);
/*
                dbh.ajoutePhoto(numOeuvre, currentPath, timeStamp);
                dbh.changeEtat(numOeuvre, dbh.ETAT_GALERIE);
*/              System.out.println("etat_o set to 2");
                object.setDatedePhoto(timeStamp);
                object.setURI(currentPath);
                object.setEtat(2);

                new ServerUpdatePicture(f).execute();
                new updateOeuvre().execute(object);
                MainActivity.withImg=true;
                if(MainActivity.caller.equals("List")){
                ListViewFragment frag =  (ListViewFragment) getTargetFragment();
                if(frag != null){
                    frag.refreshDialog();
                }}
                else if(MainActivity.caller.equals("Map")){
                    MapFragment frag = (MapFragment) getTargetFragment();
                    if(frag != null){
                        frag.refreshDialog();
                    }}
                else if(MainActivity.caller.equals("Gallery")){
                    GalleryFragment frag = (GalleryFragment) getTargetFragment();
                    if(frag != null){
                        frag.refreshDialog();
                    }}
            //new getOeuvre().execute();
                /*Intent refresh = new Intent(getContext(), MainActivity.class);
                startActivity(refresh);//Start the same Activity
                getActivity().finish();*/
                /*MainActivity.oeuvreDuJour=false;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(this).attach(this).commit();*/
                this.dismiss();


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
/*
                intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("numOeuvre", numOeuvre);
                intent.putExtra("List", oeuvreList);
                startActivity(intent);*/
                //MainActivity.MapFrag(0);
                this.dismiss();
                break;

            case R.id.button_cam:
                float range = 75;//metres
                if(inRange(MainActivity.lati,MainActivity.longi,object.getLocationX(),object.getLocationY(),range)){
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
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_PICTURE);
                    }
                }
                }
                        else Toast.makeText(getActivity(),
                            "vous devez vous rapprocher",
                            Toast.LENGTH_SHORT).show();
                        break;
                         /*catch (IOException e) {
                            e.printStackTrace();
                        }*/


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
                break;
            case R.id.closebtn:
                this.dismiss();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
/*if (MainActivity.withImg==true) {
    Boolean changesComment;
    Boolean changesRating;

    changesComment = changes.getBoolean("comment", false);
    changesRating = changes.getBoolean("rating", false);

    if (changesComment == true) {
        String comment = String.valueOf(userC.getText());
        object.setCommentaire(comment);

        SharedPreferences.Editor editor = changes.edit();
        editor.putBoolean("comment", false);
        editor.commit();
    }

    if (changesRating == true) {
        int rating = (int) ratingBar.getRating();

        object.setNote(rating);
        SharedPreferences.Editor editor = changes.edit();
        editor.putBoolean("rating", false);
        editor.commit();
    }
}*/
    }

    @Override
    public void onPause() {
        super.onPause();
       /* if (MainActivity.withImg==true) {
            Boolean changesComment;
            Boolean changesRating;

            changesComment = changes.getBoolean("comment", false);
            changesRating = changes.getBoolean("rating", false);

            if (changesComment == true) {
                String comment = String.valueOf(userC.getText());
                object.setCommentaire(comment);

                SharedPreferences.Editor editor = changes.edit();
                editor.putBoolean("comment", false);
                editor.commit();
            }

            if (changesRating == true) {
                int rating = (int) ratingBar.getRating();

                object.setNote(rating);
                SharedPreferences.Editor editor = changes.edit();
                editor.putBoolean("rating", false);
                editor.commit();
            }
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
       /* if (MainActivity.withImg==true) {
            Boolean changesComment;
            Boolean changesRating;

            changesComment = changes.getBoolean("comment", false);
            changesRating = changes.getBoolean("rating", false);

            if (changesComment == true) {
                String comment = String.valueOf(userC.getText());
                object.setCommentaire(comment);

                SharedPreferences.Editor editor = changes.edit();
                editor.putBoolean("comment", false);
                editor.commit();
            }

            if (changesRating == true) {
                int rating = (int) ratingBar.getRating();

                object.setNote(rating);
                SharedPreferences.Editor editor = changes.edit();
                editor.putBoolean("rating", false);
                editor.commit();
            }
        }*/
    }
    public class getOeuvre extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            oeuvreList =new ArrayList<OeuvreObject>(FirstActivity.getDb().getOeuvreDao().verifyID(String.valueOf(numOeuvre)));
            ArrayList<userObject> userobjects =new ArrayList<userObject>(FirstActivity.getDb().getUserDao().getUser());
//            username = userobjects.get(0).getUser();
//            password = userobjects.get(0).getPw();
            object=oeuvreList.get(0);
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
    }public class ServerUpdatePicture extends AsyncTask<Void, Void, Boolean> {

        String Attribute;
        String Value;
        String Request;

        String attachmentName = "bitmap";
        String attachmentFileName = "bitmap.bmp";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";
        File file;
        ServerUpdatePicture(File f) {
            file=f;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                if(comm()){
                    return true;}
                else return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }




        protected boolean comm() throws IOException {
            boolean success = false;

            String charset = "UTF-8";
            String requestURL = "http://www-etud.iro.umontreal.ca/~beaurevg/ift3150/server/";
            File filesDir = getContext().getFilesDir();
            File imageFile = new File(filesDir,uri_photo);
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
            multipart.addFormField("username", username);
            multipart.addFormField("password", password);
            multipart.addFormField("IDOeuvre", ""+numOeuvre);
            multipart.addFormField("request", "addPicture");
            multipart.addFilePart("file",  file);
            String response = multipart.finish(); // response from server.

/*
            Bitmap bitmap = BitmapFactory.decodeFile(uri_photo);
            String attachmentName = "bitmap";
            String attachmentFileName = "bitmap.bmp";
            String crlf = "\r\n";
            String twoHyphens = "--";
            String boundary =  "*****";
            String urlParameters =  "?username="+username+
                                    "&password="+password+
                                    "&IDOeuvre="+numOeuvre+
                                    "&request=addPicture";

            HttpURLConnection httpUrlConnection = null;
            URL url = new URL("http://www-etud.iro.umontreal.ca/~beaurevg/ift3150/server/"+urlParameters);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setDoOutput(true);

            httpUrlConnection.setRequestMethod("POST");
            byte[] postDataBytes = urlParameters.getBytes("UTF-8");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
            httpUrlConnection.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);
            httpUrlConnection.getOutputStream().write(postDataBytes);
            DataOutputStream request = new DataOutputStream(
                    httpUrlConnection.getOutputStream());

            request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    this.attachmentName + "\";filename=\"" +
                    this.attachmentFileName + "\"" + this.crlf);
            request.writeBytes(this.crlf);

            byte[] pixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
            for (int i = 0; i < bitmap.getWidth(); ++i) {
                for (int j = 0; j < bitmap.getHeight(); ++j) {
                    //we're interested only in the MSB of the first byte,
                    //since the other 3 bytes are identical for B&W images
                    pixels[i + j] = (byte) ((bitmap.getPixel(i, j) & 0x80) >> 7);
                }
            }

            request.write(pixels);

            request.writeBytes(this.crlf);
            request.writeBytes(this.twoHyphens + this.boundary +
                    this.twoHyphens + this.crlf);

            request.flush();
            request.close();

            InputStream responseStream = new
                    BufferedInputStream(httpUrlConnection.getInputStream());

            BufferedReader responseStreamReader =
                    new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            String response = stringBuilder.toString();
            responseStream.close();
            httpUrlConnection.disconnect();*/
/*


            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            //String url = "http://donnees.ville.montreal.qc.ca/dataset/2980db3a-9eb4-4c0e-b7c6-a6584cb769c9/resource/18705524-c8a6-49a0-bca7-92f493e6d329/download/oeuvresdonneesouvertes.json";
            //String url ="www-etud.iro.umontreal.ca/~beaurevg/ift3150/server/?request=loadJson";
            //String jsonStr = sh.makeServiceCall(url);
            URL url = null;
            url = new URL(
            "http://www-etud.iro.umontreal.ca/");
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
*/



/*
            String jsonStr = response;

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
                });
            }

            return success;*/return true;
        }
    }
    public class MultipartUtility {

        private final String boundary;
        private static final String LINE_FEED = "\r\n";
        private HttpURLConnection httpConn;
        private String charset;
        private OutputStream outputStream;
        private PrintWriter writer;

        /**
         * This constructor initializes a new HTTP POST request with content type
         * is set to multipart/form-data
         *
         * @param requestURL
         * @param charset
         * @throws IOException
         */
        public MultipartUtility(String requestURL, String charset)
                throws IOException {
            this.charset = charset;

            // creates a unique boundary based on time stamp
            boundary = "===" + System.currentTimeMillis() + "===";

            URL url = new URL(requestURL);
            Log.e("URL", "URL : " + requestURL.toString());
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true); // indicates POST method
            httpConn.setDoInput(true);
            httpConn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);
            httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
            httpConn.setRequestProperty("Test", "Bonjour");
            outputStream = httpConn.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                    true);
        }

        /**
         * Adds a form field to the request
         *
         * @param name  field name
         * @param value field value
         */
        public void addFormField(String name, String value) {
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                    .append(LINE_FEED);
            writer.append("Content-Type: text/plain; charset=" + charset).append(
                    LINE_FEED);
            writer.append(LINE_FEED);
            writer.append(value).append(LINE_FEED);
            writer.flush();
        }

        /**
         * Adds a upload file section to the request
         *
         * @param fieldName  name attribute in <input type="file" name="..." />
         * @param uploadFile a File to be uploaded
         * @throws IOException
         */
        public void addFilePart(String fieldName, File uploadFile)
                throws IOException {
            String fileName = uploadFile.getName();
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append(
                    "Content-Disposition: form-data; name=\"" + fieldName
                            + "\"; filename=\"" + fileName + "\"")
                    .append(LINE_FEED);
            writer.append(
                    "Content-Type: "
                            + URLConnection.guessContentTypeFromName(fileName))
                    .append(LINE_FEED);
            writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();

            FileInputStream inputStream = new FileInputStream(uploadFile);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            writer.append(LINE_FEED);
            writer.flush();
        }

        /**
         * Adds a header field to the request.
         *
         * @param name  - name of the header field
         * @param value - value of the header field
         */
        public void addHeaderField(String name, String value) {
            writer.append(name + ": " + value).append(LINE_FEED);
            writer.flush();
        }

        /**
         * Completes the request and receives response from the server.
         *
         * @return a list of Strings as response in case the server returned
         * status OK, otherwise an exception is thrown.
         * @throws IOException
         */
        public String finish() throws IOException {
            StringBuffer response = new StringBuffer();

            writer.append(LINE_FEED).flush();
            writer.append("--" + boundary + "--").append(LINE_FEED);
            writer.close();

            // checks server's status code first
            int status = httpConn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        httpConn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                httpConn.disconnect();
            } else {
                throw new IOException("Server returned non-OK status: " + status);
            }
            System.out.println("responsenani : "+response.toString());
            return response.toString();
        }
    }

    public static boolean inRange(double lat1,double lon1,double lat2,double lon2,float range){
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lon1);

        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lon2);

        float distanceInMeters = loc1.distanceTo(loc2);
        System.out.println("range : ->" +distanceInMeters);
        System.out.println("lat1 : ->" +lat1);
        System.out.println("lon1 : ->" +lon1);
        System.out.println("lat2 : ->" +lat2);
        System.out.println("lon2 : ->" +lon2);
        if(distanceInMeters<=range)
            return true;
        else
            return false;
    }

}
