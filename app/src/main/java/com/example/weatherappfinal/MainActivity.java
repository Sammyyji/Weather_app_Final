package com.example.weatherappfinal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.*;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import android.content.Intent;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    TextView cityInput;
    Button enterBtn, buttonTo2;
    String url;
    ImageView icon;



    //this class gets the weather details, it is created below
    class getWeather extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls){
            StringBuilder result = new StringBuilder();      //weather details are stored in result, ??StringBuilder??(learn)
            try{
                URL url= new URL(urls[0]);    //??URL class??(learn)
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();  //establishes connection to internet....??HttpURLCOnnection??(learn)
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();    //InputStream class???(learn)
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));     //BufferedReader???(learn)
                String line="";    //initialize as empty
                while((line = reader.readLine()) != null){
                    result.append(line).append("\n");
                }
                return result.toString();
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try{
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");   //weatherInfo holds all the information in the main section of all the details the API provides
                int i = weatherInfo.indexOf("icon");
                int j = weatherInfo.indexOf("\",\"des");
                weatherInfo = weatherInfo.substring(weatherInfo.length()-6,weatherInfo.length()-3);

                icon = findViewById(R.id.icon);
                String weatherIcon = "https://openweathermap.org/img/wn/" + weatherInfo + "@2x.png";
                Picasso.get().load(weatherIcon).into(icon);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityInput = findViewById(R.id.cityInput);
        enterBtn = findViewById(R.id.enterBtn);
        buttonTo2 = (Button) findViewById(R.id.buttonTo2);

        //event switches from screen 1 to screen2
        buttonTo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity2.class);
                startActivity(intent);
            }
        });


        //stores the list of temperature details
        final String[] temp={""};
        enterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String city = cityInput.getText().toString();
                try{
                    //checks if input is not empty--------------------
                    if(city!=null){
                            url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=66362104a5391009d4ff70db389e81b9&units=imperial";
                    }else{
                        Toast.makeText(MainActivity.this, "You have to enter a city^^", Toast.LENGTH_SHORT).show();   //error message displayed on app
                    }
                    getWeather task = new getWeather();  //class that gets the weather details
                    temp[0] = task.execute(url).get(); //list stores weather details
                }catch(ExecutionException e){
                    e.printStackTrace();
                }catch(InterruptedException e){  //if you enter any invalid city, this will catch the error
                    e.printStackTrace();
                }
            }
        });
    }
}

