package com.example.carwash.model;

import com.google.firebase.storage.StorageReference;

public class VoitureAdapter {

    private int id;
    private  String immatriculation;
    private  String numero_proprietaire;
    private int statut;
    private String image;
    private  String laveur;


    public VoitureAdapter(int id, String immatriculation, String numero_proprietaire, int statut, String image, String laveur) {
        this.id = id;
        this.immatriculation = immatriculation;
        this.numero_proprietaire = numero_proprietaire;
        this.statut = statut;
        this.image = image;
        this.laveur = laveur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getNumero_proprietaire() {
        return numero_proprietaire;
    }

    public void setNumero_proprietaire(String numero_proprietaire) {
        this.numero_proprietaire = numero_proprietaire;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLaveur() {
        return laveur;
    }

    public void setLaveur(String laveur) {
        this.laveur = laveur;
    }
}
