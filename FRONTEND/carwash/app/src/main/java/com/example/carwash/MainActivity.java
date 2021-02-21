package com.example.carwash;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private EditText numberInput;
    private EditText passwordInput;
    private Button connexionButton;
    private ProgressDialog pdialogue;
    String resultat;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        System.out.println("Utilisateur courant "+currentUser);

        signIn();

        numberInput = (EditText) findViewById(R.id.numero);
        passwordInput = (EditText) findViewById(R.id.password);
        connexionButton = (Button) findViewById(R.id.btn_login);

        connexionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {


                String numero = numberInput.getText().toString();
                String password = passwordInput.getText().toString();

                if(numero.isEmpty() || password.isEmpty()){

                    Toast.makeText(MainActivity.this, "Veillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }else{
                    new login().execute();

                }
            }
        });
    }

    private class login extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pdialogue = new ProgressDialog(MainActivity.this);
            pdialogue.setMessage("Veuillez patientez...");
            pdialogue.setCancelable(false);
            pdialogue.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String url ="http://192.168.43.123:8080/carwash/webresources/utilisateurcontroller/login?numero="+numberInput.getText().toString()+"&password="+passwordInput.getText().toString()+" ";
            HttpHandler service = new HttpHandler();
            resultat = service.serviceCall(url, "GET");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(pdialogue.isShowing()) pdialogue.dismiss();
                System.out.println("resultat de la connexion "+ resultat);
            if(resultat == null){
                connexion();
            }else{

                if(!resultat.trim().equals("0")){

                    Intent intent = new Intent(MainActivity.this, Menu.class);
                    intent.putExtra("id", resultat.trim());
                    startActivity(intent);
                }else {

                    erreur();

                }
            }

        }

    }

    private void erreur(){

        AlertDialog alertDialog = new AlertDialog.Builder( MainActivity.this ).create();
        alertDialog.setTitle("Erreur");
        alertDialog.setMessage("Numéro ou Mot de passe incorrecte");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void connexion(){

        AlertDialog alertDialog = new AlertDialog.Builder( MainActivity.this ).create();
        alertDialog.setTitle("Erreur");
        alertDialog.setMessage("Veillez vous connectez au réseau");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    void signIn(){

        mAuth.signInWithEmailAndPassword("kemanedonfack5@gmail.com", "dnki13c042")
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }

}
