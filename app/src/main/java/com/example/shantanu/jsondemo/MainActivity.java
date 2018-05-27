package com.example.shantanu.jsondemo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv);

        DataExtend dataExtend = new DataExtend();
        dataExtend.execute();
    }


    class DataExtend extends AsyncTask<Void,Void,String> {

        String data="";
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(Void... params) {
            try {

                URL url= new URL("http://api.androidhive.info/contacts/");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream fin = new BufferedInputStream(httpURLConnection.getInputStream());
                data = inputStreamToString(fin);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;

        }

        private String inputStreamToString(InputStream input){
            String s = "";

            StringBuilder stringBuilder = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(isr);

            try {
                if ((s = bufferedReader.readLine())!=null) {
                    stringBuilder.append(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("LOADING");
            progressDialog.setMessage("The data is being parsed");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            try {
                JSONObject jsonObject =new JSONObject(aVoid);
                JSONArray jsonArray = jsonObject.getJSONArray("contacts");

                for (int i=0; i<jsonArray.length();++i){

                    String id = jsonArray.getJSONObject(i).getString("id");
                    String name = jsonArray.getJSONObject(i).getString("name");
                    String email = jsonArray.getJSONObject(i).getString("email");
                    String phones = jsonArray.getJSONObject(i).getJSONObject("phone").getString("mobile");
                    String home = jsonArray.getJSONObject(i).getJSONObject("phone").getString("home");
                    String office = jsonArray.getJSONObject(i).getJSONObject("phone").getString("office");

                    textView.append("ID  "+id+"\n");
                    textView.append("Name "+name+"\n");
                    textView.append("Email "+email+"\n");
                  //  textView.append("Phones "+"\n");
                    textView.append("Mobile "+phones+"\n");
                    textView.append("Home  "+home+"\n");
                    textView.append("Office "+office+"\n");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}

