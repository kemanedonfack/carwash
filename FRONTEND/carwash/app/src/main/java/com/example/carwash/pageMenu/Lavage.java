package com.example.carwash.pageMenu;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;


import com.example.carwash.HttpHandler;
import com.example.carwash.R;
import com.example.carwash.VoitureCostumList;
import com.example.carwash.model.VoitureAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Lavage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Lavage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressDialog pdialogue;
    private ArrayList<HashMap<String, String>> menuVoiture;
    private ListView listvoiture;
    private VoitureCostumList adapter;
    private ArrayList<VoitureAdapter> arrayList;
    private Button button;

    View view;

    public Lavage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Lavage.
     */
    // TODO: Rename and change types and number of parameters
    public static Lavage newInstance(String param1, String param2) {
        Lavage fragment = new Lavage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_lavage, container, false);
        listvoiture = (ListView) view.findViewById(R.id.idlvvoiture);
        arrayList = new ArrayList<>();
        menuVoiture = new ArrayList<>();
        new getVoiture().execute();
        return view;
    }


     private class getVoiture extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            System.out.println("dans la fonction");
            super.onPreExecute();
            menuVoiture.clear();
            listvoiture.setAdapter(null);
            pdialogue = new ProgressDialog(getContext());
            pdialogue.setMessage("chargement encours...");
            pdialogue.setCancelable(false);
            pdialogue.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String url ="http://192.168.43.123:8080/carwash/webresources/voiturecontroller/listvoiture?statut=0";
            HttpHandler service = new HttpHandler();
            String jsonvoiture = service.serviceCall(url, "GET");
            if(jsonvoiture != null){

                try {
                    JSONArray voitures = new JSONArray(jsonvoiture);

                    for(int i =0; i < voitures.length(); i++){
                        JSONObject p = voitures.getJSONObject(i);

                        int idvoiture = p.getInt("idvoiture");
                        String immatriculation = p.getString("immatriculation");
                        String numero_proprietaire = p.getString("numero_proprietaire");
                        int statut = p.getInt("statut");
                        String image = p.getString("image");
                        String laveur = p.getString("laveur");

                        arrayList.add(new VoitureAdapter(  idvoiture, immatriculation, numero_proprietaire, statut, image, laveur  ));

                        HashMap<String, String> hasphmap = new HashMap<>();
                        hasphmap.put("idvoiture", Integer.toString(idvoiture) );
                        hasphmap.put("immatriculation", immatriculation );
                        hasphmap.put("numero_proprietaire", numero_proprietaire );
                        hasphmap.put("statut", Integer.toString(statut) );
                        hasphmap.put("image", image );
                        hasphmap.put("laveur", laveur );
                        menuVoiture.add(hasphmap);

                    }

                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Réessayer après quelques minutes", Toast.LENGTH_LONG).show();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(pdialogue.isShowing()) pdialogue.dismiss();
            adapter = new VoitureCostumList(getContext(), R.layout.listvoiture, arrayList);
            listvoiture.setAdapter(adapter);
        }

    }

}