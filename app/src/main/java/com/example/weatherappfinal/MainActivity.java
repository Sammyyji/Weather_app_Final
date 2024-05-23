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


public class MainActivity extends AppCompatActivity {
    TextView cityName;
    Button search;
    TextView weatherDetails;
    String url;
    Switch Cswitch;
    Switch Fswitch;
    public Button button2;
    public Button button22;




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
                String weatherInfo = jsonObject.getString("weather");

                weatherDetails.setText(weatherInfo);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = findViewById(R.id.cityInput);
        search = findViewById(R.id.enterBtn);
        weatherDetails = findViewById(R.id.cityOutput);
        //Cswitch = findViewById(R.id.switchC);
        //Fswitch = findViewById(R.id.switchF);

        button2 = (Button) findViewById(R.id.homeBtn);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity2.class);
                startActivity(intent);
            }
        });





        //stores the list of temperature details
        final String[] temp={""};

        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Toast.makeText(Activity2.this, "The button has been clicked! ", Toast.LENGTH_SHORT).show();
                String city = cityName.getText().toString();
                try{
                    //checks if input is not empty--------------------
                    if(city!=null){
                            url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=66362104a5391009d4ff70db389e81b9&units=imperial";
                    }else{
                        //error message displayed on app
                        Toast.makeText(MainActivity.this, "You have to enter a city^^", Toast.LENGTH_SHORT).show();
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
                    weatherDetails.setText("Unable to find the weather for your city Sorry try again!");
                }
            }
        });



    }


}

