/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.connexion;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Kemane Donfack
 */
public class Connexion {

    private static Connection conex;
    private String url = "jdbc:postgresql://localhost:5432/laverie";
    private String user = "postgres";
    private String pwd = "DNKI13c042";

    public Connexion() {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.out.println("erreur de driver :" + e.getMessage());
        }

        try {
            conex = DriverManager.getConnection(url, user, pwd);
        } catch (Exception e) {
            System.out.println("erreur de connexion :" + e.getMessage());
        }
    }

    public static Connection seconnecter() {

        if (conex == null) {
            Connexion connexion = new Connexion();
        }
        return conex;
    }

}