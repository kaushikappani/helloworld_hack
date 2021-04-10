package com.example.edu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.edu.ui.CollegeInfoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static int checked;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor myEdit;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        myEdit = sharedpreferences.edit();
        int default1;
        switch (Configuration.UI_MODE_NIGHT_MASK & AppCompatDelegate.getDefaultNightMode()){
            case AppCompatDelegate.MODE_NIGHT_NO:
                default1=0;
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                default1=1;
                break;
            default:
                default1=2;
        }
        checked=sharedpreferences.getInt("checked",default1);
        switch (checked){
            case 0:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        DownloadTask task=new DownloadTask();
        task.execute("https://us-central1-imperial-welder-273309.cloudfunctions.net/get_admittance");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.theme){
            MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(MainActivity.this);
            builder.setTitle("Theme");
            final String[] strings={"Light","Dark","System Default"};
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    changeTheme();
                    restart();

                }
            });
            builder.setSingleChoiceItems(strings, checked, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checked=which;
                    myEdit.putInt("checked",checked);
                    myEdit.apply();

                }
            });
            builder.setNeutralButton("Cancel",null);
            builder.show();
        }
        if(item.getItemId()==R.id.post){
            volleyPost();
        }
        return super.onOptionsItemSelected(item);
    }
    public void restart(){
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }



    public void changeTheme(){
        if(checked==0){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        if(checked==1){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        if(checked==2){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    public void volleyPost(){
        String postUrl = "https://us-central1-imperial-welder-273309.cloudfunctions.net/get_admittance";
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        JSONObject postData = new JSONObject();
        try {
            postData.put("GRE Score",320);
            postData.put("TOEFL Score",115);
            postData.put("University Rating",1);
            postData.put("SOP",1);
            postData.put("LOR",1);
            postData.put("CGPA",10);
            postData.put("Research",1);
            postData.put("sports",4);
            postData.put("certifications",4);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("response",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }
    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("https://us-central1-imperial-welder-273309.cloudfunctions.net/get_admittance"); //in the real code, there is an ip and a port
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.connect();

                JSONObject postData = new JSONObject();
                postData.put("GRE Score", 320);
                postData.put("TOEFL Score", 115);
                postData.put("University Rating", 1);
                postData.put("SOP", 1);
                postData.put("LOR", 1);
                postData.put("CGPA", 10);
                postData.put("Research", 1);
                postData.put("sports", 4);
                postData.put("certifications", 4);


                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(URLEncoder.encode(postData.toString(), "UTF-8"));

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG", conn.getResponseMessage());

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }
    }

}
