/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;
 

import com.model.ModelVoiture;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 *
 * @author Kemane Donfack
 */
@Path("voiturecontroller")
public class VoitureController {
    
    /* service web permettant de récupere les voitures
     *  prend en parametre le statut de la voiture
    */
    @GET
    @Path("/listvoiture")
    @Produces({"application/json"})

    public ArrayList<ModelVoiture> listvoiture(@QueryParam("statut") int statut) {
        ArrayList<ModelVoiture> listev = new ArrayList<>();
        //try ctrl+espace raccourci clavier    
        try {
            System.out.println("Vérification des logs");
            Statement etat = com.connexion.Connexion.seconnecter().createStatement();
            ResultSet rs = etat.executeQuery("SELECT * FROM VOITURE WHERE statut = '"+statut+"' ORDER BY id DESC ");
            
            while (rs.next()) {
                ModelVoiture unevoiture = new ModelVoiture();
                unevoiture.setIdvoiture(rs.getInt("id"));
                unevoiture.setImmatriculation(rs.getString("immatriculation"));
                unevoiture.setNumero_proprietaire(rs.getString("numero_proprietaire"));
                unevoiture.setStatut(rs.getInt("statut"));
                unevoiture.setImage(rs.getString("image"));
                unevoiture.setLaveur(rs.getString("laveur"));
                listev.add(unevoiture);
            }
            etat.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("erreur de type voiture : "+e.getMessage());

        }
        return listev;
    }
    
    /* 
    service web permettant d'ajouter une voiture
    */
    @GET
    @Path("/ajouter")
    @Produces({"text/plain"})

    public String ajoutervoiture(@QueryParam("immatriculation") String immatriculation, @QueryParam("image") String image, 
            @QueryParam("numero_proprietaire") String numero_proprietaire, @QueryParam("laveur") String laveur, @QueryParam("token") String token   ) {
      
        System.out.println("image url : " +image);
        token="&token="+token;
        image = image + token;
      int statut=0;
      int resultat=0;
      String reponse="";
      
        try {
            System.out.println("Vérification des logs");
            Statement etat = com.connexion.Connexion.seconnecter().createStatement();
            String insert = "INSERT INTO voiture (immatriculation, numero_proprietaire, statut, image, laveur) VALUES "
                    + "('"+immatriculation+"','"+numero_proprietaire+"','"+statut+"','"+image+"','"+laveur+"')";
             
            resultat = etat.executeUpdate(insert);
            
           
        } catch (Exception e) {
            System.out.println("erreur de type utilisateur : "+e.getMessage());
        }
        
        if(resultat == 1){
            reponse = "true";
        }else{
            reponse = "false";
        }
        
        return reponse;
    }
   
    /* service web permettant de modifier le statut d'une voitures
     *  prend en parametre l'id de la voiture
    */
    @PUT
    @Path("/modifier")
    @Produces({"text/plain"})

    public int modifierrvoiture(@QueryParam("id") int id ) {
     
      int resultat=0;
        try {
            System.out.println("Vérification des logs");
            Statement etat = com.connexion.Connexion.seconnecter().createStatement();
            String update = "UPDATE voiture SET statut = 1 WHERE id = '"+id+"'";
            resultat = etat.executeUpdate(update);
           
        } catch (Exception e) {
            System.out.println("erreur de type utilisateur : "+e.getMessage());

        }
        return resultat;
    }
    
    /* 
     *  service web permettant d'afficher une voiture 
     *  prend en parametre l'id de la voiture
    */
    @GET
    @Path("/voiture")
    @Produces({"application/json"})

    public ArrayList<ModelVoiture> voiture(@QueryParam("id") int id) {
        ArrayList<ModelVoiture> listev = new ArrayList<>();
        //try ctrl+espace raccourci clavier    
        try {
            System.out.println("Vérification des logs");
            Statement etat = com.connexion.Connexion.seconnecter().createStatement();
            ResultSet rs = etat.executeQuery("SELECT * FROM VOITURE WHERE id = '"+id+"' ");
            
            while (rs.next()) {
                ModelVoiture unevoiture = new ModelVoiture();
                unevoiture.setIdvoiture(rs.getInt("id"));
                unevoiture.setImmatriculation(rs.getString("immatriculation"));
                unevoiture.setNumero_proprietaire(rs.getString("numero_proprietaire"));
                unevoiture.setStatut(rs.getInt("statut"));
                unevoiture.setImage(rs.getString("image"));
                unevoiture.setLaveur(rs.getString("laveur"));
                listev.add(unevoiture);
            }
            etat.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("erreur de type voiture : "+e.getMessage());

        }
        return listev;
    }
      
      
    
}
