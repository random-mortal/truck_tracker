package com.example.pranav.asapp;

import android.app.AlertDialog;
import android.content.Context;

import android.os.AsyncTask;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;





public class background extends AsyncTask <String, Void, String> {

    AlertDialog dialog;
    Context context;

    public background(Context context)
    {
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("Database update status:");
    }

    protected void onPostExecute(String s) {
        dialog.setMessage(s);
        dialog.show();

    }







    @Override
    protected String doInBackground(String... args) {
        String result="";

        String ownername = args[0];
        String regno = args[1];
        String makermodel = args[2];
        String regdate = args[3];
        String fueltype = args[4];
        String vehclass = args[5];

        //String vehage = args[6];
        String engineno = args[6];
        String chassisno = args[7];
        String fitness = args[8];
        String insurance = args[9];

        //dialog.setMessage(ownername+" "+regno+" "+regdate);
        //dialog.show();

        //String connstr  = "http://localhost:80/udb.php";
        String connstr  = "http://192.168.43.86:80/udb.php";



        try {

            URL url = new URL(connstr);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);

            OutputStream ops = http.getOutputStream();
            BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));
            String data = URLEncoder.encode("ownername","UTF-8") + "=" + URLEncoder.encode(ownername,"UTF-8")+ "&" +
                    URLEncoder.encode("regno","UTF-8")+"="+URLEncoder.encode(regno,"UTF-8") +"&"+
                    URLEncoder.encode("makermodel","UTF-8")+"="+URLEncoder.encode(makermodel,"UTF-8") +"&"+
                    URLEncoder.encode("regdate","UTF-8")+"="+URLEncoder.encode(regdate,"UTF-8") +"&"+
                    URLEncoder.encode("fueltype","UTF-8")+"="+URLEncoder.encode(fueltype,"UTF-8") +"&"+
                    URLEncoder.encode("vehclass","UTF-8")+"="+URLEncoder.encode(vehclass,"UTF-8") +"&"+

                    URLEncoder.encode("engineno","UTF-8")+"="+URLEncoder.encode(engineno,"UTF-8") +"&"+
                    URLEncoder.encode("chassisno","UTF-8")+"="+URLEncoder.encode(chassisno,"UTF-8") +"&"+
                    URLEncoder.encode("fitness","UTF-8")+"="+URLEncoder.encode(fitness,"UTF-8") +"&"+
                    URLEncoder.encode("insurance","UTF-8")+"="+URLEncoder.encode(insurance,"UTF-8");

            bwriter.write(data);
            bwriter.flush();
            bwriter.close();
            ops.close();


            //String st = "Kalyan: Kalyan, Dombivli, Ulhasnagar, Ambernath and Badlapur - RTO is located in Kalyan";









            InputStream ips = http.getInputStream();
            BufferedReader breader = new BufferedReader(new InputStreamReader(ips,"ISO-8859-1"));
            String line ="";
            while ((line = breader.readLine()) != null)
            {
                result += line;
            }
            breader.close();
            ips.close();














            http.disconnect();
            return result;




        }
        catch (MalformedURLException e) {
            result = e.getMessage();
        }
        catch (IOException e) {
            result = e.getMessage();
        }

        return result;


    }




}

