package com.example.dajc.tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomGrid extends BaseAdapter {
    private Context mContext;
    private final String[] web;
    private final int[] Imageid;
    private final int[] Progress;
    private final int[] Objectives;

    public CustomGrid(Context c, String[] web, int[] Imageid, int[] progress, int[] objectives) {
        mContext = c;
        this.Imageid = Imageid;
        this.Progress = progress;
        this.Objectives = objectives;
        this.web = web;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return web.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            if (web[position].contains("Neige")) {
                ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
                if (Progress[position] < Objectives[position]) {
                    imageView.setImageResource(R.drawable.cdn);
                } else {
                    imageView.setImageResource(R.drawable.cdn_color);
                }
            }
            if (web[position].contains("Hochelaga")) {
                ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
                if (Progress[position] < Objectives[position]) {
                    imageView.setImageResource(R.drawable.hoch);
                } else {
                    imageView.setImageResource(R.drawable.hoch_color);
                }
            }
            if (web[position].contains("Université de Montréal")) {
                ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
                if (Progress[position] < Objectives[position]) {
                    imageView.setImageResource(R.drawable.udm);
                } else {
                    imageView.setImageResource(R.drawable.udm_color);
                }
            }
            if (web[position].contains("Mont-Royal")) {
                ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
                if (Progress[position] < Objectives[position]) {
                    imageView.setImageResource(R.drawable.mr);
                } else {
                    imageView.setImageResource(R.drawable.mr_color);
                }
            }
            if (web[position].contains("Villeray")) {
                ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
                if (Progress[position] < Objectives[position]) {
                    imageView.setImageResource(R.drawable.vry);
                } else {
                    imageView.setImageResource(R.drawable.vry_color);
                }
            }
            if (web[position].contains("Ahuntsic-Cartierville")) {
                ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
                if (Progress[position] < Objectives[position]) {
                    imageView.setImageResource(R.drawable.ac);
                } else {
                    imageView.setImageResource(R.drawable.ac_color);
                }
            }
            if (web[position].contains("Lachine")) {
                ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
                if (Progress[position] < Objectives[position]) {
                    imageView.setImageResource(R.drawable.lach);
                } else {
                    imageView.setImageResource(R.drawable.lach_color);
                }
            }
                if (web[position].contains("Sud-Ouest")) {
                    ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
                    if (Progress[position] < Objectives[position]) {
                        imageView.setImageResource(R.drawable.so);
                    } else {
                        imageView.setImageResource(R.drawable.so_color);
                    }
                } else {
                    TextView textView = grid.findViewById(R.id.textView6);
                    textView.setText(web[position]);
                }
                ProgressBar progressBar = (ProgressBar) grid.findViewById(R.id.grid_progressBar);
                //

                progressBar.setProgress(position);
                progressBar.setMax(Objectives[position]);
                progressBar.setProgress(Progress[position]);
                System.out.println("i " + Objectives[position]);
                //
            } else {
                grid = (View) convertView;
            }

            return grid;
        }
    }


