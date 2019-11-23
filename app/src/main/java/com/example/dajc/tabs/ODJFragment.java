package com.example.dajc.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import static com.example.dajc.tabs.FichePopUpFragment.REQUEST_IMAGE_PICTURE;

public class ODJFragment extends Fragment {
    ArrayList<OeuvreObject> oeuvreList= new ArrayList<>();
    TextView title;
    TextView author;
    TextView date;
    TextView infos;
    ImageButton map_b;
    ImageButton cam_b;

    int numOeuvre;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v;
        v = inflater.inflate(R.layout.oeuvre_jour_fragment, container, false);

        oeuvreList = FirstActivity.getOeuvreList();

        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        Random rand = new Random(dayOfYear);
        int idDuJour = rand.nextInt(FirstActivity.oeuvreList.size());


        title = (TextView) v.findViewById(R.id.titre);
        author = (TextView) v.findViewById(R.id.artiste);
        date = (TextView) v.findViewById(R.id.date);
        infos = (TextView) v.findViewById(R.id.info);
        map_b = (ImageButton) v.findViewById(R.id.button_map);
        cam_b = (ImageButton) v.findViewById(R.id.button_cam);

        //On set l'oeuvre du jour
        title.setText(oeuvreList.get(idDuJour).getTitre());
        author.setText(oeuvreList.get(idDuJour).getArtiste());
        date.setText(oeuvreList.get(idDuJour).getDatedeCreation());
        infos.setText(oeuvreList.get(idDuJour).getMateriaux());

        cam_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (!MainActivity.Permission.checkPermissionForExternalStorage()) {
                    MainActivity.Permission.requestPermissionForExternalStorage();
                }
                else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_PICTURE);
                }

            }
        });
        map_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //TODO: ouvrir la map avec l'emplacement de l'oeuvre
            }
        });

        return v;
    }

}
