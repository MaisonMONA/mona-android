package com.example.dajc.tabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.PopupMenu;

import java.util.ArrayList;

public class ODJActivity extends AppCompatActivity {

    ArrayList<OeuvreObject> oeuvreList= new ArrayList<>();
    ImageButton mMenu_Button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fiche_noimg2_test);
        oeuvreList = FirstActivity.getOeuvreList();

        mMenu_Button = findViewById(R.id.menu_button);
    }
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            intent.putExtra("List", oeuvreList);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.badges) {
            Intent intent = new Intent(this, Badge_Activity.class);
            intent.putExtra("List", oeuvreList);
            startActivity(intent);
        }
        return true;
    }

}
