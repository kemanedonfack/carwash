package com.example.carwash;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.carwash.model.VoitureAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoitureCostumList extends ArrayAdapter<VoitureAdapter> {

    private ArrayList<VoitureAdapter> voitureAdapters;
    private Context context;
    private int resource;
    private ImageView imageView;
    private ProgressDialog pdialogue;
    private View view;
    String resultat;
    private StorageReference storageReference;
    private ImageView voiture;
    private ArrayList<HashMap<String, String>> menuVoiture;
    private ListView listvoiture;
    private VoitureCostumList adapter;
    private ArrayList<VoitureAdapter> arrayList;
    private String imageURL = "https://firebasestorage.googleapis.com/v0/b/carwash-12bc7.appspot.com/o/images%2Fc31cda94-78ca-4581-92a2-747233261f0c?alt=media&token=8bd8ea4e-e4c2-4c7d-8b31-7eb73fb14878";



    public VoitureCostumList(@NonNull Context context, int resource, @NonNull List<VoitureAdapter> objects) {
        super(context, resource, objects);

        this.voitureAdapters = voitureAdapters;
        this.context = context;
        this.resource = resource;
    }

    @SuppressLint("CutPasteId")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.listvoiture, parent, false);
        }

        VoitureAdapter voitureAdapter = getItem(position);


        voiture = (ImageView) convertView.findViewById(R.id.imageVoiture);

        // / ce code permet de charger l'url de l'image et de resoudre les probleme recontrer avec l'url
        Picasso.with(getContext())
                .load(voitureAdapter.getImage().replaceAll("/images/","/images%2F"))
                .into(voiture);
        System.out.println("url de l'image "+voitureAdapter.getImage());
//        new DownloadImageFromURL(voitureAdapter.getImage()).execute();


        TextView immatriculation = (TextView) convertView.findViewById(R.id.immatriculation);
        immatriculation.setText(voitureAdapter.getImmatriculation());

        TextView laveur = (TextView) convertView.findViewById(R.id.laveur);
        laveur.setText(voitureAdapter.getLaveur());

        imageView = (ImageView) convertView.findViewById(R.id.termine);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(parent.getContext(), v);
                popup.getMenuInflater().inflate(R.menu.lavage_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {
                         System.out.println("resultat : "+ item.getTitle().toString().trim());
                        if(item.getTitle().toString().trim().equals("Terminer") ){
                            new lavageterminer(getItem(position).getId(), getItem(position).getImmatriculation() ).execute();

                        }else if(item.getTitle().toString().trim().equals("Annuler")) {

                            Toast.makeText(getContext(),"Lavage annuler : " + getItem(position).getId(), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(),"Iconnu : " + getItem(position).getId(), Toast.LENGTH_SHORT).show();
                        }
                        return true;

                    }
                });
                popup.show();
            }
        });

        View popmenu = convertView.findViewById(R.id.imageVoiture);
        popmenu.setTag(getItem(position));

        return convertView;
    }


    private class lavageterminer extends AsyncTask<Void, Void, Void> {

        private int id;
        private String immatriculation;
        public lavageterminer(int id, String immatriculation) {
            this.id = id;
            this.immatriculation = immatriculation;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pdialogue = new ProgressDialog(getContext());
            pdialogue.setMessage("Veuillez patientez...");
            pdialogue.setCancelable(false);
            pdialogue.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String url ="http://192.168.43.123:8080/carwash/webresources/voiturecontroller/modifier?id="+id+"";
            HttpHandler service = new HttpHandler();
            resultat = service.serviceCall(url, "PUT");
            System.out.println("url "+url);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(pdialogue.isShowing()) pdialogue.dismiss();
            System.out.println("resultat de la connexion "+ resultat);

            if(resultat.trim().equals("0")){

                Toast.makeText(getContext(),"erreur inconnu", Toast.LENGTH_SHORT).show();
            }else {

                Toast.makeText(getContext(),"Lavage de "+immatriculation+" terminer", Toast.LENGTH_SHORT).show();
            }

        }

    }


    private class DownloadImageFromURL extends AsyncTask<String, Void, String> {
        Bitmap bitmap = null;
        String image;

        public DownloadImageFromURL(String image) {

            this.image = image;
        }


        protected String doInBackground(String... urls) {
            try {
                Log.e("imageURL is ", image);
                InputStream in = new java.net.URL(image).openStream();
                if (in != null) {
                    bitmap = BitmapFactory.decodeStream(in);
                } else
                    Log.e("Empty InputStream", "InputStream is empty.");
            } catch (MalformedInputException e) {
                Log.e("Error URL", e.getMessage().toString());
            } catch (Exception ex) {
                Log.e("Input stream error", "Input stream error");
            }
            return "";
        }

        protected void onPostExecute(String result) {

            if (bitmap != null) {
                voiture.setImageBitmap(bitmap);
            } else{
                Log.e("Empty Bitmap", "Bitmap is empty.");
            }
        }
    }


}
