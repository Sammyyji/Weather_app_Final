package com.example.weatherappfinal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.*;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
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


public class Activity2 extends AppCompatActivity {
    TextView cityInput, cityOutput;
    Button enterBtn,buttonTo3,buttonTo1;
    String url;
    Switch switchC, switchF;

    //this class gets the weather details, it is created below
    class getWeather extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls){
            //weather details are stored in result, ??StringBuilder??(learn)
            StringBuilder result = new StringBuilder();
            try{
                //??URL class??(learn)
                URL url= new URL(urls[0]);
                //establishes connection to internet....??HttpURLCOnnection??(learn)
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                //InputStream class???(learn)
                InputStream inputStream = urlConnection.getInputStream();
                //BufferedReader???(learn)
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                //initialize as empty
                String line="";
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
                //weatherInfo holds all the information in the main section of all the details the API provides
                String weatherInfo = jsonObject.getString("main");
                weatherInfo = weatherInfo.replaceFirst("temp","Temperature ");
                weatherInfo = weatherInfo.replace("'", "");
                weatherInfo = weatherInfo.replace("feels_like","Feels Like ");
                weatherInfo = weatherInfo.replace("temp_max","Max Temperature");
                weatherInfo = weatherInfo.replace("temp_min","Min Temperature");
                weatherInfo = weatherInfo.replace("pressure","Pressure");
                weatherInfo = weatherInfo.replace("humidity","Humidity");
                weatherInfo = weatherInfo.replace("{","");
                weatherInfo = weatherInfo.replace("}","");
                weatherInfo = weatherInfo.replace(",","\n");
                weatherInfo = weatherInfo.replace(":",": ");
                weatherInfo = weatherInfo.replace("\""," ");
                weatherInfo = weatherInfo.replace("sea_level","sea level(hPa) ");
                weatherInfo = weatherInfo.replace("grnd_level","ground level(hPa) ");
                cityOutput.setText(weatherInfo);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        cityInput = findViewById(R.id.cityInput);
        enterBtn = findViewById(R.id.enterBtn);
        cityOutput = findViewById(R.id.cityOutput);
        switchC = findViewById(R.id.switchC);
        switchF = findViewById(R.id.switchF);
        buttonTo3 = (Button) findViewById(R.id.buttonTo3);
        buttonTo1 = (Button) findViewById(R.id.buttonTo1);

        //Event to switch from screen 2 to 3
        buttonTo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity2.this, Activity3.class);
                startActivity(intent);
            }
        });

        //Event to switch from screen 2 to 1

        buttonTo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity2.this, MainActivity.class);
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
                            if (switchC.isChecked()) {
                                url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=66362104a5391009d4ff70db389e81b9&units=metric";
                            } else if (switchF.isChecked()) {
                                url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=66362104a5391009d4ff70db389e81b9&units=imperial";
                            }
                    }else{
                        //error message displayed on app
                        Toast.makeText(Activity2.this, "You have to enter a city^^", Toast.LENGTH_SHORT).show();
                    }
                    //class that gets the weather details
                    getWeather task = new getWeather();
                    temp[0] = task.execute(url).get();
                }catch(ExecutionException e){
                    e.printStackTrace();
                    //if you enter any invalid city, this will catch the error
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                if(temp[0] == null){
                    cityOutput.setText("Unable to find the weather for your city Sorry try again!");
                }
            }
        });



        }


    }

