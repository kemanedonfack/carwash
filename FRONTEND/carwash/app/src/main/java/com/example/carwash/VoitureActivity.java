package com.example.carwash;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carwash.model.VoitureAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class VoitureActivity extends AppCompatActivity {

    private String id;
    private ProgressDialog pdialogue;
    private ArrayList<VoitureAdapter> arrayList;
    private TextView laveur;
    private TextView num_client;
    private TextView imma_voiture;
    private ImageView image_voiture;
    private ArrayList<HashMap<String, String>> menuVoiture;
    private VoitureAdapter voitureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        System.out.println("valeur de id : "+intent.getStringExtra("id"));

        new Voiture().execute();
        setContentView(R.layout.activity_voiture);
    }



    private class Voiture extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            System.out.println("dans la fonction");
            super.onPreExecute();
            pdialogue = new ProgressDialog(VoitureActivity.this);
            pdialogue.setMessage("chargement encours...");
            pdialogue.setCancelable(false);
            pdialogue.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String url ="http://192.168.43.123:8080/carwash/webresources/voiturecontroller/voiture?id="+id+"";
            HttpHandler service = new HttpHandler();
            String jsonvoiture = service.serviceCall(url, "GET");
            if(jsonvoiture != null){

                try {
                    JSONArray voitures = new JSONArray(jsonvoiture);

                    for(int i = 0; i < voitures.length(); i++){
                        JSONObject p = voitures.getJSONObject(i);

                        int idvoiture = p.getInt("idvoiture");
                        String immatriculation = p.getString("immatriculation");
                        String numero_proprietaire = p.getString("numero_proprietaire");
                        int statut = p.getInt("statut");
                        String image = p.getString("image");
                        String laveur = p.getString("laveur");

                        voitureAdapter = new VoitureAdapter (idvoiture, immatriculation, numero_proprietaire, statut, image, laveur);
                    }

                } catch (JSONException e) {
                    Toast.makeText(VoitureActivity.this, "Réessayer après quelques minutes", Toast.LENGTH_LONG).show();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(pdialogue.isShowing()) pdialogue.dismiss();

            imma_voiture = (TextView) findViewById(R.id.imma_proprietaire);
            num_client = (TextView) findViewById(R.id.num_proprietaire);
            laveur = (TextView) findViewById(R.id.nomlaveur);
            image_voiture = (ImageView) findViewById(R.id.imageV);

            imma_voiture.setText(voitureAdapter.getImmatriculation());
            num_client.setText(voitureAdapter.getNumero_proprietaire());
           laveur.setText(voitureAdapter.getLaveur());

            // / ce code permet de charger l'url de l'image et de resoudre les probleme recontrer avec l'url
            Picasso.with(VoitureActivity.this)
                    .load(voitureAdapter.getImage().replaceAll("/images/","/images%2F"))
                    .into(image_voiture);
//        System.out.println("url de l'image "+voitureAdapter.getImage());

        }

    }

}
