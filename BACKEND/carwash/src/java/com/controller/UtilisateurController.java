/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.model.ModelUtilisateur;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.POST;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 *
 * @author Kemane Donfack
 */

@Path("utilisateurcontroller")
public class UtilisateurController {
    
    /* 
     * service web permettant de récupere les employés de la laveris
    */
    @GET
    @Path("/listutilisateur")
    @Produces({"application/json"})
    
    public ArrayList<ModelUtilisateur> listeutilisateur() {
        ArrayList<ModelUtilisateur> listeu = new ArrayList<>();
        //try ctrl+espace raccourci clavier    
        try {
            System.out.println("Vérification des logs");
            Statement etat = com.connexion.Connexion.seconnecter().createStatement();
            ResultSet rs = etat.executeQuery("SELECT * FROM utilisateur");
            System.out.println("resultat list utilisateur "+rs);
            while (rs.next()) {
                ModelUtilisateur unutilisateur = new ModelUtilisateur();
                unutilisateur.setIdutilisateur(rs.getInt("id"));
                unutilisateur.setNom(rs.getString("nom"));
                unutilisateur.setNumero(rs.getString("numero"));
                unutilisateur.setStatut(rs.getInt("statut"));
                unutilisateur.setPassword(rs.getString("mot_de_passe"));
                listeu.add(unutilisateur);
            }
            etat.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("erreur de type utilisateur : "+e.getMessage());

        }
        return listeu;
    }    

    /* 
      *  service web permettant la connexion d'un utilisateur
    */
    @GET
    @Path("/login")
    @Produces({"text/plain"})
   
    public int loginutilisateur(@QueryParam("numero") String numero, @QueryParam("password") String password) {
        
        int id=0;
        String resultat="";
        //try ctrl+espace raccourci clavier    
        try {
            System.out.println("Vérification des logs");
            Statement etat = com.connexion.Connexion.seconnecter().createStatement();
            String insert = "SELECT * FROM utilisateur WHERE numero ='"+numero+"' AND mot_de_passe='"+password+"' LIMIT 1 ";
            ResultSet rs = etat.executeQuery(insert);
            
            while (rs.next()) {
                
               id = rs.getInt("id");
            }
            
        } catch (Exception e) {
            System.out.println("erreur de type utilisateur : "+e.getMessage());

        }
        return id;
    }
    
    /* 
     * service web permettant de récupere le nom de l'utilisateur connecté
    */
    @GET
    @Path("/auth")
    @Produces({"text/plain"})
   
    public String authUser(@QueryParam("id") String id) {
        
        String resultat="";
        //try ctrl+espace raccourci clavier    
        try {
            System.out.println("Vérification des logs");
            Statement etat = com.connexion.Connexion.seconnecter().createStatement();
            String insert = "SELECT * FROM utilisateur WHERE id ='"+id+"' ";
            ResultSet rs = etat.executeQuery(insert);
            
            while (rs.next()) {
                
               resultat = rs.getString("nom");
            }
            
        } catch (Exception e) {
            System.out.println("erreur de type utilisateur : "+e.getMessage());

        }
        return resultat;
    }
    
}
