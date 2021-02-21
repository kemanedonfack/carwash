package com.example.carwash.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class voitureController {


    private Connection connection;

    private final String host = "192.168.137.1";  // For Google Cloud Postgresql
    private final String database = "laverie";
    private final int port = 5432;
    private final String user = "postgres";
    private final String pass = "DNKI13c042";
    private String url = "jdbc:postgresql://%s:%d/%s";
    private boolean status;

    public voitureController() {
        this.url = String.format(this.url, this.host, this.port, this.database);
        recuperer();
        //this.disconnect();
        System.out.println("connection status:" + status);
    }

    public void recuperer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, pass);
                    Statement etat = connection.createStatement();
                    ResultSet rs = etat.executeQuery("SELECT * FROM UTILISATEUR");

                    while (rs.next()) {

                        System.out.print(rs.getInt("id"));
                        System.out.print(rs.getString("nom"));
                        System.out.print(rs.getString("numero"));
                        System.out.print(rs.getString("mot_de_passe"));
                        System.out.print(rs.getString("statut"));
                        System.out.println(" ");

                    }
                    etat.close();
                    rs.close();

                    status = true;
                    System.out.println("connected:" + status);
                } catch (Exception e) {
                    status = false;
                    System.out.print(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            this.status = false;
        }
    }


    public void insersion() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, pass);
                    Statement etat = connection.createStatement();
                    String nom = "kemane";
                    int statut = 0;
                    String sql = "INSERT INTO UTILISATEUR (nom, numero, statut, mot_de_passe) VALUES ('"+nom+"','"+nom+"','"+statut+"','"+nom+"')";
                    etat.executeUpdate(sql);
                    etat.close();
                    status = true;
                    System.out.println("connected:" + status);
                } catch (Exception e) {
                    status = false;
                    System.out.print(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            this.status = false;
        }
    }


}
