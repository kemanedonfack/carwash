/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model;

/**
 *
 * @author Kemane Donfack
 */
public class ModelVoiture {
    
    private Integer idvoiture;
    private String immatriculation;
    private String numero_proprietaire;
    private Integer statut;
    private String image;
    private String laveur;

    public ModelVoiture() {
    }

    public Integer getIdvoiture() {
        return idvoiture;
    }

    public void setIdvoiture(Integer idvoiture) {
        this.idvoiture = idvoiture;
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

    public Integer getStatut() {
        return statut;
    }

    public void setStatut(Integer statut) {
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
