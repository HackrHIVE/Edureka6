package com.chirag.edureka6;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView name,latitude,longitude,country,description;
    TextView temp,humidity,minTemp,maxTemp,degreeWind,dt,speed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        progressBar = findViewById( R.id.progressBar );
        name = findViewById( R.id.cityname );
        latitude = findViewById( R.id.latitude );
        longitude = findViewById( R.id.longitude );
        country = findViewById( R.id.country );
        description = findViewById( R.id.description );
        temp = findViewById( R.id.temp );
        humidity = findViewById( R.id.humidity );
        minTemp = findViewById( R.id.temp_min );
        maxTemp = findViewById( R.id.temp_max );
        degreeWind = findViewById( R.id.wind_degree );
        dt = findViewById( R.id.dt );
        speed = findViewById( R.id.speed );

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    String response = fetchData();
                    Log.i( "STUNNx",response );
                    final CityWeatherData data = new Gson().fromJson( response,CityWeatherData.class );
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility( View.VISIBLE );
                            Handler handler = new Handler(  );
                            handler.postDelayed( new Runnable() {
                                @Override
                                public void run() {
                                    name.setText( data.getName() );
                                    latitude.setText( String.valueOf( data.getCoord().getLat() ) );
                                    longitude.setText( String.valueOf( data.getCoord().getLon() ) );
                                    country.setText( data.getSys().getCountry() );
                                    description.setText( data.getWeather().get( 0 ).getDescription() );
                                    temp.setText( String.valueOf( data.getMain().getTemp() ) );
                                    speed.setText( String.valueOf( data.getWind().getSpeed() ) );
                                    humidity.setText( String.valueOf( data.getMain().getHumidity() ) );
                                    minTemp.setText( String.valueOf( data.getMain().getTemp_min() ) );
                                    maxTemp.setText( String.valueOf( data.getMain().getTemp_min() ) );
                                    degreeWind.setText( String.valueOf( data.getWind().getDeg() ) );
                                    dt.setText( String.valueOf( data.getDt() ) );
                                    progressBar.setVisibility( View.INVISIBLE );
                                    Toast.makeText( MainActivity.this, "Made by Chirag Rawal using GSON library", Toast.LENGTH_SHORT ).show();
                                }
                            },2000 );
                        }
                    } );

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private String fetchData() throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=mumbai&AppID=ebfcac32bda131ed5a160f2757938396");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.connect();
        int responseCode = httpURLConnection.getResponseCode();
        if(responseCode==200){
            Log.i("STUNNx","Success!");
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader( inputStream );
            BufferedReader bufferedReader = new BufferedReader( inputStreamReader );

            String temp;
            StringBuffer buffer = new StringBuffer(  );
            while((temp=bufferedReader.readLine())!=null){
                buffer.append( temp+"\n" );
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            return buffer.toString();
        }
        else{
            Log.i("STUNNx","Failure!");
            return null;
        }

    }
}
