package com.example.weatherappfinal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.os.*;
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

public class Activity3 extends AppCompatActivity {
    TextView cityInput,cityOutput ;
    Button enterBtn,buttonTo2;
    String url;
    ImageView weatherImage;

    class getWeather extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls){
            StringBuilder result = new StringBuilder();
            try{
                URL url= new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
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
                String weatherInfo = jsonObject.getString("clouds");
                int i = weatherInfo.indexOf(":");
                weatherInfo = weatherInfo.substring(i+1, weatherInfo.length()-1);
                int j = Integer.parseInt(weatherInfo.trim());
                if (j < 25) {
                    weatherImage.setImageResource(R.drawable.clear);
                }else if(j>=25 && j<50) {
                    weatherImage.setImageResource(R.drawable.clear25);
                }else if(j>=50 && j<75) {
                    weatherImage.setImageResource(R.drawable.cloudy50);
                }else if(j>=75){
                    weatherImage.setImageResource(R.drawable.cloudy);
                }
                weatherImage.setVisibility(View.VISIBLE);
                weatherInfo = weatherInfo + "% cloudy!";
                cityOutput.setText(weatherInfo);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        cityInput = findViewById(R.id.cityInput);
        enterBtn = findViewById(R.id.enterBtn);
        cityOutput = findViewById(R.id.cityOutput);
        weatherImage = findViewById(R.id.imageView);
        buttonTo2 = (Button) findViewById(R.id.buttonTo2);

        final String[] temp={""};
        buttonTo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity3.this, Activity2.class);
                startActivity(intent);
            }
        });


        enterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(Activity3.this, "Button Clicked! ", Toast.LENGTH_SHORT).show();
                String city = cityInput.getText().toString();
                try{
                    if(city!=null){
                        url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=66362104a5391009d4ff70db389e81b9&units=imperial";
                    }else{
                        Toast.makeText(Activity3.this, "Enter City", Toast.LENGTH_SHORT).show();
                    }
                    getWeather task= new getWeather();
                    temp[0] = task.execute(url).get();
                }catch(ExecutionException e){
                    e.printStackTrace();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                if(temp[0] == null){
                    cityOutput.setText("Unable to find the weather");
                }

            }
        });

    }
}