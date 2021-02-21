package com.example.carwash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.carwash.model.VoitureAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VoitureCostumListLaver extends ArrayAdapter<VoitureAdapter> {

    private ArrayList<VoitureAdapter> voitureAdapters;
    private Context context;
    private int resource;
    private ImageView imageView;
    private ListView listView;
    private ProgressDialog pdialogue;
    String resultat;


    public VoitureCostumListLaver(@NonNull Context context, int resource, @NonNull List<VoitureAdapter> objects) {
        super(context, resource, objects);

        this.voitureAdapters = voitureAdapters;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.voiturelaver, parent, false);
        }

        VoitureAdapter voitureAdapter = getItem(position);


       ImageView voiture = (ImageView) convertView.findViewById(R.id.imageVoiture);

        // / ce code permet de charger l'url de l'image et de resoudre les probleme recontrer avec l'url
        Picasso.with(getContext())
                .load(voitureAdapter.getImage().replaceAll("/images/","/images%2F"))
                .into(voiture);
//        System.out.println("url de l'image "+voitureAdapter.getImage());


        TextView immatriculation = (TextView) convertView.findViewById(R.id.immatriculation);
        immatriculation.setText(voitureAdapter.getImmatriculation());

        TextView laveur = (TextView) convertView.findViewById(R.id.laveur);
        laveur.setText(voitureAdapter.getLaveur());
        System.out.println("id : "+voitureAdapter.getId());

        TextView textid = (TextView) convertView.findViewById(R.id.idvehicule);
        textid.setText(Integer.toString(voitureAdapter.getId()));
        textid.setVisibility(View.INVISIBLE);

        return convertView;
    }

}
