package com.example.dajc.tabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by DAJC on 2016-04-20.
 */
public class FicheActivity extends Activity implements View.OnClickListener {
    static final int REQUEST_IMAGE_PICTURE = 1;
    ArrayList<OeuvreObject> oeuvreList= new ArrayList<>();
    static int position;
    AppDatabase db;
    TextView title;
    TextView author;
    TextView date;
    TextView infos;
    TextView date_ajout;
    ImageView photo;
    ImageButton fav_b;
    ImageButton map_b;
    ImageButton cam_b;
    EditText user_c;
    RatingBar ratingBar;

    int numOeuvre;

    int etat_o;
    String id;
    String user_comment;
    int user_rating;

    SharedPreferences changes;


    public FicheActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fiche_noimg);
        Bundle bundle = getIntent().getExtras();
        oeuvreList = bundle.getParcelableArrayList("List");
        title = (TextView) findViewById(R.id.titre);
        author = (TextView) findViewById(R.id.artiste);
        date = (TextView) findViewById(R.id.date);
        infos = (TextView) findViewById(R.id.info);
        photo = (ImageView) findViewById(R.id.photo);
        fav_b = (ImageButton) findViewById(R.id.button_fav);
        map_b = (ImageButton) findViewById(R.id.button_map);
        cam_b = (ImageButton) findViewById(R.id.button_cam);
        date_ajout = (TextView) findViewById(R.id.tv_date);
        user_c = (EditText) findViewById(R.id.user_comment);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        db=FirstActivity.getDb();
        Intent intent = getIntent();
        numOeuvre = intent.getIntExtra("numOeuvre",0);

        String titre_o = oeuvreList.get(numOeuvre).getTitre();
        String tech_nbr = oeuvreList.get(numOeuvre).getTitre();
        String cat_nbr = oeuvreList.get(numOeuvre).getTitre();
        String quart_nbr = oeuvreList.get(numOeuvre).getTitre();
        String mat_nbr = oeuvreList.get(numOeuvre).getTitre();
        String dimension_o = oeuvreList.get(numOeuvre).getDimension();
        String uri_photo = oeuvreList.get(numOeuvre).getURI();
        String date_oeuvre = oeuvreList.get(numOeuvre).getDatedeCreation();
        user_comment = oeuvreList.get(numOeuvre).getTitre();
        user_rating = oeuvreList.get(numOeuvre).getEtat();
        etat_o = oeuvreList.get(numOeuvre).getEtat();
        //set title
        title.setText(titre_o);


        //set artist(s) name(s)
        //String o_artistes = oeuvreList.get(numOeuvre).getArtiste().getNom();
        String o_artistes = oeuvreList.get(numOeuvre).getArtiste();
        author.setText(o_artistes);

        //date de la photo
        date.setText(date_oeuvre);

        //dimensions de l'oeuvre
        String dimension = dimension_o;

        //technique
        String tech_oeuvre = oeuvreList.get(numOeuvre).getTechnique();
        String cat_oeuvre = oeuvreList.get(numOeuvre).getTitre();
        String quart_oeuvre = oeuvreList.get(numOeuvre).getQuartier();
        String mat_oeuvre = oeuvreList.get(numOeuvre).getMateriaux();


        String information = "Quartier: " + quart_oeuvre + "\n" + "Dimensions: " + dimension + "\n"
                + "Catégorie: " + cat_oeuvre + "\n" + "Technique: " + tech_oeuvre + "\n"
                + "Matériau: " + mat_oeuvre;
        infos.setText(information);

        //image de l'oeuvre ou par défaut
        //if (uri_photo.equals(dbh.URI_DEF)) {
            Picasso.with(this).load("http://www-ens.iro.umontreal.ca/~krausele/emptyImage.png").resize(500, 888).into(photo);
            photo.setVisibility(View.VISIBLE);
       /* } else {
            Bitmap bmImg = BitmapFactory.decodeFile(uri_photo);
            photo.setImageBitmap(bmImg);
        }

*/
       //si l'oeuvre est favorite
        if (etat_o==1) {
            fav_b.setBackgroundResource(R.mipmap.ic_favorite_active);
            date_ajout.setVisibility(View.GONE);
        }
        //si l'oeuvre est dans la galerie, on ne peut pas prendre de photo ou l'ajouter aux favoris
        //par contre, on affiche la date à laquelle la photo a été prise
        else if (etat_o==2) {

            fav_b.setBackgroundResource(R.mipmap.ic_favorite_passive);
            fav_b.setVisibility(View.GONE);
            cam_b.setVisibility(View.GONE);

        }
            /*

            //date de la photo de l'utilisateur
            String date_photo = dbh.retourneDatephoto(numOeuvre);
            date_ajout.setText("photo du " + date_photo);

            //click listener pour agrandir la photo
            photo.setOnClickListener(this);

            if (! user_comment.equals("")){
                user_c.setText(user_comment);
            }
            if (user_rating != 0) {
                ratingBar.setRating((float) user_rating);
            }


        }
        */else {
            fav_b.setBackgroundResource(R.mipmap.ic_favorite_passive);
            date_ajout.setVisibility(date_ajout.GONE);
            user_c.setVisibility(user_c.GONE);
            ratingBar.setVisibility(ratingBar.GONE);
        }

        changes = getApplicationContext().getSharedPreferences("change_rating", Context.MODE_PRIVATE);

        fav_b.setOnClickListener(this);
        cam_b.setOnClickListener(this);
        map_b.setOnClickListener(this);
        ratingBar.setOnClickListener(this);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                SharedPreferences.Editor editor = changes.edit() ;
                editor.putBoolean("rating", true);
                editor.commit();
            }
        });

        user_c.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editor = changes.edit() ;
                editor.putBoolean("comment", true);
                editor.commit();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICTURE && resultCode != 0) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            if (imageBitmap != null) {
                photo.setImageBitmap(imageBitmap);
            }


            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            try {
                File f = File.createTempFile(
                        "MTL_ART_" + numOeuvre + "_", //prefix
                        ".jpg",        //suffix
                        storageDir      //directory
                );
                String currentPath = f.getAbsolutePath();

                FileOutputStream fout = new FileOutputStream(currentPath);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                //dbh.ajoutePhoto(numOeuvre, currentPath, timeStamp);
                //dbh.changeEtat(numOeuvre, dbh.ETAT_GALERIE);

                GalleryFragment.sca.notifyDataSetChanged();

                Intent refresh = new Intent(this, FicheActivity.class);
                refresh.putExtra("numOeuvre", numOeuvre);
                startActivity(refresh);//Start the same Activity
                this.finish();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_fav:
                oeuvreList.get(numOeuvre).setEtat(1);
                new updateOeuvre().execute(oeuvreList.get(numOeuvre));
/*
                if (etat_o.equals(dbh.ETAT_NORMAL)) {

                    //méthode qui change l'état dans la base de données
                    dbh.changeEtat(numOeuvre, dbh.ETAT_FAVORIS);

                    Toast.makeText(this, "Oeuvre ajoutée aux favoris", Toast.LENGTH_SHORT).show();
                    WishListFragment.sca.notifyDataSetChanged();
                } else if (etat_o.equals(dbh.ETAT_FAVORIS)) {

                    dbh.changeEtat(numOeuvre, dbh.ETAT_NORMAL);

                    Toast.makeText(this, "Oeuvre retirée des favoris", Toast.LENGTH_SHORT).show();
                    WishListFragment.sca.notifyDataSetChanged();
                } else {*/
                    Log.d("listViewCursorAdaptor", "on ne peut pas changer l'état: " + etat_o);
               // }

                Intent refresh = new Intent(this, FicheActivity.class);
                refresh.putExtra("numOeuvre", numOeuvre);
                refresh.putExtra("List", oeuvreList);
                startActivity(refresh);//Start the same Activity
                this.finish();

                break;
            case R.id.button_map:
/*
                intent = new Intent(this, MapActivity.class);
                intent.putExtra("numOeuvre", numOeuvre);
                startActivity(intent);*/
                MainActivity.MapFrag(0);

                break;

            case R.id.button_cam:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, REQUEST_IMAGE_PICTURE);
                break;
            case R.id.photo:
                intent = new Intent (this, PhotoActivity.class);
                intent.putExtra("numOeuvre", numOeuvre);
                startActivity(intent);
                break;

            case R.id.ratingBar:
                Toast.makeText(getApplicationContext(),
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        Boolean changesComment;
        Boolean changesRating;

        changesComment = changes.getBoolean("comment", false);
        changesRating = changes.getBoolean("rating", false);

        if (changesComment) {
            String comment = String.valueOf(user_c.getText());

            //add modified value to DB
            //dbh.ajouteComment(numOeuvre, comment);

            SharedPreferences.Editor editor = changes.edit() ;
            editor.putBoolean("comment", false);
            editor.commit();
        }

        if(changesRating){
            int rating = (int) ratingBar.getRating();
            //add modified value to DB
            //dbh.ajouteRating(numOeuvre, rating);

            SharedPreferences.Editor editor = changes.edit() ;
            editor.putBoolean("rating", false);
            editor.commit();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Boolean changesComment;
        Boolean changesRating;

        changesComment = changes.getBoolean("comment", false);
        changesRating = changes.getBoolean("rating", false);

        if (changesComment) {
            String comment = String.valueOf(user_c.getText());

            //add modified value to DB
            //dbh.ajouteComment(numOeuvre, comment);

            SharedPreferences.Editor editor = changes.edit() ;
            editor.putBoolean("comment", false);
            editor.commit();
        }

        if(changesRating){
            int rating = (int) ratingBar.getRating();
            //add modified value to DB
            //dbh.ajouteRating(numOeuvre, rating);

            SharedPreferences.Editor editor = changes.edit() ;
            editor.putBoolean("rating", false);
            editor.commit();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        Boolean changesComment;
        Boolean changesRating;

        changesComment = changes.getBoolean("comment", false);
        changesRating = changes.getBoolean("rating", false);

        if (changesComment) {
            String comment = String.valueOf(user_c.getText());

            //add modified value to DB
            //dbh.ajouteComment(numOeuvre, comment);

            SharedPreferences.Editor editor = changes.edit() ;
            editor.putBoolean("comment", false);
            editor.commit();
        }

        if(changesRating){
            int rating = (int) ratingBar.getRating();
            //add modified value to DB
            //dbh.ajouteRating(numOeuvre, rating);

            SharedPreferences.Editor editor = changes.edit() ;
            editor.putBoolean("rating", false);
            editor.commit();
        }

    }
}
