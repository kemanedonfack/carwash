package com.example.carwash;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpHandler {

    public HttpHandler() {
    }

    public String serviceCall(String reqUrl, String method){
        String response = null;
        try {
            URL url = new URL(reqUrl);

            System.out.println("iciicic");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod(method);
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertInputString(in);
            System.out.println("pas d'erreur");

        } catch (MalformedURLException e) {
            System.out.println("Erreur url mal former"+ e.getMessage() );

        } catch (IOException e) {
            System.out.println("Erreur d'ouverture de l'url"+ e.getMessage() );

        }

        return response;
    }

    private String convertInputString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        try{

            while ((line = reader.readLine()) != null ){
                sb.append(line).append("\n");
            }

        } catch (IOException e) {

            e.printStackTrace();

        }finally {

            try {

                is.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
