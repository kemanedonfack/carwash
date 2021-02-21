package com.example.carwash.pageMenu;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carwash.HttpHandler;
import com.example.carwash.MainActivity;
import com.example.carwash.R;
import com.example.carwash.VoitureCostumList;
import com.example.carwash.model.VoitureAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ajouter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ajouter extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView imageView;
    Button button;
    View view;
    Uri imageUri;
    private EditText immatriculation;
    EditText numero_proprietaire;
    TextView isValidNumero;
    TextView isValidImmatriculation;
    String reponse="";
    private FirebaseAuth mAuth;
    private String image;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private String nom;



    public Ajouter() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ajouter.
     */
    // TODO: Rename and change types and number of parameters
    public static Ajouter newInstance(String param1, String param2) {
        Ajouter fragment = new Ajouter();
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

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        signIn();
        view = inflater.inflate(R.layout.fragment_ajouter, container, false);

        imageView = (ImageView) view.findViewById(R.id.imageVoiture);
        button = (Button) view.findViewById(R.id.btn_ajouter);
        immatriculation = (EditText) view.findViewById(R.id.immatriculation);
        numero_proprietaire = (EditText) view.findViewById(R.id.numero_proprietaire);
        isValidImmatriculation = (TextView) view.findViewById(R.id.invalid_immatriculation);
        isValidNumero = (TextView) view.findViewById(R.id.invalid_numero);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(getContext())
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult( intent, 1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean Vimmma = isValid(immatriculation.getText().toString(), "^[A-Za-z]{2}[0-9]{3}[A-Za-z]{2}$");
                boolean Vnumber = isValid(numero_proprietaire.getText().toString(), "^6[957][0-9]{7}$");

                if (Vimmma) {
                    isValidImmatriculation.setVisibility(View.INVISIBLE);
                } else {
                    isValidImmatriculation.setVisibility(View.VISIBLE);
                }

                if (Vnumber) {
                    isValidNumero.setVisibility(View.INVISIBLE);
                } else {
                    isValidNumero.setVisibility(View.VISIBLE);
                }

               if(imageUri != null){

                   if(Vimmma == true && Vnumber == true){

                       System.out.println("insersion réussi");
                       uploadImage();

                   }

               }else{
                   Toast.makeText(getContext(), "Veuillez choisir une image ", Toast.LENGTH_LONG).show();
               }

            }
        });

        // récupération de l'id de utilisateur connecté

        String id = getActivity().getIntent().getStringExtra("id");
        System.out.println("id de l'utilisateur connecté : "+id);
        new getSingleAuthUser(id).execute();

        // Inflate the layout for this fragment
        return view;

    }

    public boolean isValid(String string, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode == RESULT_OK)
        {
//            bitmap=(Bitmap)data.getExtras().get("data");
            imageView.setImageURI(data.getData());
            imageUri = data.getData();
//            encodebitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadImage(){

        FirebaseUser user = mAuth.getCurrentUser();
        System.out.println("utilisateur cournat "+user);
        if (user != null) {

            System.out.println("après user!= null ");
            final ProgressDialog pd = new ProgressDialog(getContext());
            pd.setTitle("Importation de l'image... ");
            pd.show();

            System.out.println("après la boite de dialogue");
            final String randomkey = UUID.randomUUID().toString();
            final StorageReference riversRef = storageReference.child("images/"+randomkey);

            riversRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String fileLink = task.getResult().toString();
                                    image = fileLink;
                                    System.out.println("url du fichier importer "+fileLink);
                                    new addVoiture().execute();

                                }
                            });

                            Toast.makeText(getContext(), "importation réussi", Toast.LENGTH_LONG).show();


                                pd.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "échec d'importation", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progresspourcent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            pd.setMessage("Pourcentage : "+(int) progresspourcent + "%");
                        }
                    });
        } else {
           System.out.println("veuillez vous connecté ");
        }



    }

    private class addVoiture extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            System.out.println("dans la fonction");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String url ="http://192.168.43.123:8080/carwash/webresources/voiturecontroller/ajouter?immatriculation="+immatriculation.getText().toString()+"&numero_proprietaire="+numero_proprietaire.getText().toString()+"&image="+image+"&laveur="+nom;

            HttpHandler service = new HttpHandler();
            reponse  = service.serviceCall(url, "GET");
            System.out.println("url : "+url);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            System.out.println("valeur : "+reponse);

            if(reponse.trim().equals("true")){

                immatriculation.setText("");
                numero_proprietaire.setText("");
                Toast.makeText(getContext(), "insersion réussi", Toast.LENGTH_SHORT).show();

            }else{

                Toast.makeText(getContext(), "échec d'insercion", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class getSingleAuthUser extends AsyncTask<Void, Void, Void> {

        private String id;
        public getSingleAuthUser(String id) {

            this.id = id;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String url ="http://192.168.43.123:8080/carwash/webresources/utilisateurcontroller/auth?id="+id+"";
            HttpHandler service = new HttpHandler();
            String jsonvoiture = service.serviceCall(url, "GET");

            nom = jsonvoiture.trim();
            System.out.println("nom de l'utilisateur connecté : "+nom);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

    }

    void signIn(){

        mAuth.signInWithEmailAndPassword("kemanedonfack5@gmail.com", "dnki13c042")
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }


}


