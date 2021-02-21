package com.example.carwash;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Connexion {

    private Connection connection;

    // private final String host = "ssprojectinstance.csv2nbvvgbcb.us-east-2.rds.amazonaws.com"  // For Amazon Postgresql
    private final String host = "192.168.137.1";  // For Google Cloud Postgresql
    private final String database = "laverie";
    private final int port = 5432;
    private final String user = "postgres";
    private final String pass = "DNKI13c042";
    private String url = "jdbc:postgresql://%s:%d/%s";
    private boolean status;

    public Connexion() {
        this.url = String.format(this.url, this.host, this.port, this.database);
        connect();
        //this.disconnect();
        System.out.println("connection status:" + status);
    }

    private void connect() {
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

    public Connection getExtraConnection(){
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }
}
