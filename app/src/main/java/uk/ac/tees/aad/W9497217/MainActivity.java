package uk.ac.tees.aad.W9497217;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;

    String CountryName;


    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       final TextView textviewed = findViewById(R.id.hello);

        sharedpreferences = getSharedPreferences("state", Context.MODE_PRIVATE);
        if (sharedpreferences.getString("intro","notCompleted").equals("notCompleted")) {
            startActivity(new Intent(getApplicationContext(),Intro.class));
        }else{
            if (sharedpreferences.getString("login","no").equals("no")) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }else{
                getCurrentLocation();
            }
        }

    }




    private void getCurrentLocation()
    {
        ActivityCompat.requestPermissions( this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
    }



    private void OnGPS()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }



    private void getLocation()
    {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null)
                            {
                                CountryName = getCountryName(getApplicationContext(),location.getLatitude(),location.getLongitude());
                                Intent intent2 = new Intent(getApplicationContext(),NewsScreen.class);
                                intent2.putExtra("countryName",CountryName);
                                intent2.putExtra("country", countryNameToCodeConverter( CountryName));
                                startActivity(intent2);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Unable to fetch location", Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(getApplicationContext(),NewsScreen.class);
                                intent2.putExtra("countryName","UK");
                                intent2.putExtra("country", countryNameToCodeConverter("gb"));
                                startActivity(intent2);

                            }
                        }
                    });
        }
    }


    public static String getCountryName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getCountryName();
            }
            return null;
        } catch (IOException ignored) {
            //do something
        }
        return null;
    }




    public static String countryNameToCodeConverter(String CountryName){

        String CountryCode="";

        switch (CountryName)
        {
            case "United States":
                CountryCode = "us";
                break;
            case "United Kingdom":
                CountryCode ="gb";
                break;
            case "India":
                CountryCode ="in";
                break;
            case "Afghanistan":
                CountryCode ="ae";
                default:
                    CountryCode ="gb";
        }
        return  CountryCode;
    }





    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();

    }
}