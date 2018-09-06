package com.example.dajc.tabs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * Created by LenaMK on 18/04/2016.
 */
public class MapFragment extends Fragment implements LocationListener, View.OnClickListener {

    GeoPoint startPoint;
    MapView map;
    GeoPoint myLocation;
    ResourceProxy mResourceProxy;
    IMapController mapController;
    ItemizedIconOverlay<OverlayItem> overlayL;
    ArrayList<OeuvreObject> oeuvreList = new ArrayList<>();
    //attention, ces coordonnées existent donc si c'est le cas il y aura un bug
    public double lati = 0;
    public double longi = 0;
    FichePopUpFragment dialogFragment;
    double mtl_lati;
    double mtl_longi;

    ImageButton locationButton;
    ImageButton imgcollectionbutton;
    ImageButton imgciblebutton;
    ImageButton imgnonvisitebutton;
    Button collectionbutton;
    Button ciblebutton;
    Button nonvisitebutton;
    Boolean ciblee;
    Boolean collectionnee;
    Boolean nonvisitee;

    View v;

    public MapFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ciblee=true;
        collectionnee=true;
        nonvisitee=true;
        v = inflater.inflate(R.layout.map_frag_layout, container, false);

        locationButton = (ImageButton) v.findViewById(R.id.button_location);
        locationButton.setOnClickListener(this);
        imgcollectionbutton = (ImageButton) v.findViewById(R.id.imageCollectionButton);
        imgcollectionbutton.setOnClickListener(this);
        imgciblebutton = (ImageButton) v.findViewById(R.id.imageCibleeButton3);
        imgciblebutton.setOnClickListener(this);
        imgnonvisitebutton = (ImageButton) v.findViewById(R.id.imageNonVisiteeButton2);
        imgnonvisitebutton.setOnClickListener(this);
        collectionbutton = (Button) v.findViewById(R.id.collection_button);
        collectionbutton.setOnClickListener(this);
        ciblebutton = (Button) v.findViewById(R.id.cible_button);
        ciblebutton.setOnClickListener(this);
        nonvisitebutton = (Button) v.findViewById(R.id.non_visitebutton);
        nonvisitebutton.setOnClickListener(this);

        //set map view
        map = (MapView) v.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //set default zoom buttons and ability to zoom with fingers
        //map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        //set map default view point
        mapController = map.getController();
        mapController.setZoom(15);

        LocationManager locationManager = (LocationManager)getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            MainActivity.Permission.requestPermissionForLocationC();
            MainActivity.Permission.requestPermissionForLocation();
        }
        else {
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(bestProvider, 5000, 0, this);
        }
        //set the start point to user last known location or to mtl
        mtl_lati = 45.508567;
        mtl_longi = -73.566455;
        if (lati != 0 && longi != 0) {
            startPoint = new GeoPoint(lati, longi);
            Log.d("map", "start set to phone location");
        } else {
            startPoint = new GeoPoint(mtl_lati, mtl_longi);
            Log.d("map", "start set to default location");
        }
        mapController.setCenter(startPoint);

        return v;

    }

    // fonctions de localisation
    @Override
    public void onResume() {
        super.onResume();
        new Refresh().execute();
    }

    public void updateMap(){
        map.getOverlays().clear();

        //adding specific pins: create array + add as in example
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        // ItemizedIconOverlay<OverlayItem> items = new ItemizedIconOverlay<OverlayItem>(
        //         new ArrayList<OverlayItem>(), artMarker, null,
        //       new DefaultResourceProxyImpl(getActivity()));
        // example items.add(new OverlayItem("Title", "Description", new GeoPoint(0.0d,0.0d))); // Lat/Lon decimal degrees
        String title;
        String idItem;
        double art_lati;
        double art_longi;
        int item_nb = 0;
        OverlayItem myOverlayItem;
        Drawable artMarker = getResources().getDrawable(R.drawable.ic_pingreen);
        Drawable artMarker2 = getResources().getDrawable(R.drawable.ic_pinblue);
        Drawable artMarker3 = getResources().getDrawable(R.drawable.ic_pingold);
        //dbh = new DBHelper(Activity.this);
        for (int i=0;i<oeuvreList.size();i++)
        {
            title = oeuvreList.get(i).getTitre();
            idItem = oeuvreList.get(i).getId();
            art_lati = oeuvreList.get(i).getLocationX();
            art_longi = oeuvreList.get(i).getLocationY();
            if (oeuvreList.get(i).getEtat()==2&&collectionnee)
            {
                myOverlayItem = new OverlayItem(idItem, title, idItem, new GeoPoint(art_lati, art_longi));
                myOverlayItem.setMarker(artMarker3);
                items.add(myOverlayItem);
            }
            else if (oeuvreList.get(i).getEtat()==1&&ciblee)
        {
            myOverlayItem = new OverlayItem(idItem, title, idItem, new GeoPoint(art_lati, art_longi));
            myOverlayItem.setMarker(artMarker2);
            items.add(myOverlayItem);
        }
            else if(nonvisitee){
                //System.out.println(oeuvreList.get(i).getEtat());
                //System.out.println("this is an ID "+ idItem);
                myOverlayItem = new OverlayItem(idItem, title, idItem, new GeoPoint(art_lati, art_longi));
                myOverlayItem.setMarker(artMarker);
                items.add(myOverlayItem);

            }
            item_nb++;
        }


        Log.d("map", "Items added = " + item_nb);
        ItemizedIconOverlay.OnItemGestureListener iOverlay = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            //ItemizedIconOverlay iOverlay = new ItemizedIconOverlay(overlay, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                //show the state
                Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onItemLongPress(final int index, OverlayItem item) {
                //shows the name of the artwork
                //should work on getting the fiche_noimg
                System.out.println("item no" + index +" "+  (item.getUid()));
                /*MainActivity.oeuvreDuJour=false;
                MainActivity.listFrag(Integer.parseInt(item.getUid()));*/
                //new changeFrag().execute((index));
                MainActivity.numOeuvre= Integer.parseInt(item.getUid());
                if(oeuvreList.get(index).getEtat()==2){
                    MainActivity.withImg=false;
                }
                else{
                    MainActivity.withImg=false;
                }
                MainActivity.caller="Map";
                dialogFragment = new FichePopUpFragment ();
                dialogFragment.setTargetFragment(MapFragment.this, 1337);
                dialogFragment.show(getActivity().getSupportFragmentManager(),"simple dialog");
                return false;
            }


        };

        mResourceProxy = new DefaultResourceProxyImpl(getContext());
        //the overlay
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items, iOverlay
                , mResourceProxy);
        mOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(mOverlay);


        Log.d("map", "Overlay added");
        updateIcone();
    }


    private void updateIcone(){
        if(collectionnee){
            imgcollectionbutton.setImageResource(R.drawable.ic_pingold);
        }
        else{
            imgcollectionbutton.setImageResource(R.drawable.ic_pingold_gray);
        }
        if(ciblee){
            imgciblebutton.setImageResource(R.drawable.ic_pinblue);
        }
        else{
            imgciblebutton.setImageResource(R.drawable.ic_pinblue_gray);
        }
        if(nonvisitee){
            imgnonvisitebutton.setImageResource(R.drawable.ic_pingreen);
        }
        else{
            imgnonvisitebutton.setImageResource(R.drawable.ic_pingreen_gray);
        }
    }
    public void refreshDialog(){
        //en prevision du swipe
        /*if(oeuvreList.get(MainActivity.numOeuvre).getEtat()==2){
            MainActivity.withImg=true;
        }
        else{
            MainActivity.withImg=false;
        }*/
        dialogFragment = new FichePopUpFragment ();
        dialogFragment.setTargetFragment(MapFragment.this, 1337);
        dialogFragment.show(getActivity().getSupportFragmentManager(),"simple dialog");
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.d("gps","location changed "+location.getLatitude()+","+location.getLongitude());
        lati = location.getLatitude();
        longi = location.getLongitude();
        MainActivity.lati=lati;
        MainActivity.longi=longi;
        /* do not change startpoint(center of map)
        startPoint = new GeoPoint(lati,longi);
        mapController.setCenter(startPoint);
        */

        if (this.getView() != null) {
            this.setMarker(); //set an own marker, attention, this creates a new marker every time!
        }
        map.postInvalidate();


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("gps","status changed");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("gps","provider enabled : "+provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("gps","provider disabled : "+provider);
    }
    void setMarker() {
        map.getOverlays().remove(overlayL);

        //case marker already exists, should be removed and replaced by new one
        Drawable personMarker = getResources().getDrawable(R.mipmap.ic_map_person);
        overlayL = new ItemizedIconOverlay<OverlayItem>(
                new ArrayList<OverlayItem>(), personMarker, null,
                new DefaultResourceProxyImpl(getActivity()));
        // gc: last GeoPoint
        //remove last Marker

        //map.updateViewLayout(v);

        myLocation = new GeoPoint(lati, longi);
        OverlayItem item = new OverlayItem(null, null, myLocation);

        overlayL.addItem(item);
        map.getOverlays().add(overlayL);

        Log.d("gps", "my location added");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_location:
                startPoint = new GeoPoint(lati, longi);
                mapController.setCenter(startPoint);
                break;
            case R.id.non_visitebutton:
            case R.id.imageNonVisiteeButton2:
                nonvisitee = !nonvisitee;
                updateIcone();
                updateMap();
                break;
            case R.id.collection_button:
            case R.id.imageCollectionButton:
                collectionnee = !collectionnee;
                updateMap();
                updateIcone();
                break;
            case R.id.cible_button:
            case R.id.imageCibleeButton3:
                ciblee = !ciblee;
                updateMap();
                updateIcone();
                break;
        }
    }
    private class Refresh extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            oeuvreList = new ArrayList<OeuvreObject>(FirstActivity.getDb().getOeuvreDao().getAllOeuvre());
            System.out.println(oeuvreList.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        updateMap();

        }
    }

}

