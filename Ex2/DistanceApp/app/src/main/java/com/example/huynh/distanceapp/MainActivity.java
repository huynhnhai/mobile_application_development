package com.example.huynh.distanceapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Random;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Socket mSocket;
    private boolean isLacationCur=true;
    private EditText etxtLat1,etxtLat2,etxtLong1,etxtLong2;
    private Button btnStart,btnRandom;
    private CheckBox cbInput;
    private TextView txtDistance;
    private  String latitude1="", longitude1="",latitude2="", longitude2="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addControlls();
        addEvents();

    }

    private void addEvents() {
        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomLongLat();
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtDistance.setText("   Waiting...");
                latitude1=etxtLat1.getText().toString();
                longitude1=etxtLong1.getText().toString();
                latitude2=etxtLat2.getText().toString();
                longitude2=etxtLong2.getText().toString();
                connectServer();
            }
        });
        cbInput.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if( cbInput.isChecked()){
                    etxtLat1.setText("");
                    etxtLong1.setText("");
                }
            }
        });
    }

    private void addControlls() {
        etxtLat1=findViewById(R.id.etxtLat1);
        etxtLat2=findViewById(R.id.etxtLat2);
        etxtLong1=findViewById(R.id.etxtLong1);
        etxtLong2=findViewById(R.id.etxtLong2);
        btnRandom=findViewById(R.id.btnRandom);
        btnStart=findViewById(R.id.btnStart);
        cbInput=findViewById(R.id.cbInput);
        txtDistance=findViewById(R.id.txtDistance);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void connectServer(){
        try {
            mSocket= IO.socket("https://distanceapp.herokuapp.com/");

        } catch (URISyntaxException e) {
            Toast.makeText(this, "socket error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        mSocket.connect();
        mSocket.emit("client-send-data",etxtLat1.getText().toString()+","+etxtLong1.getText().toString()+
                                    ","+etxtLat2.getText().toString()+","+etxtLong2.getText().toString());
        mSocket.on("server-send-data",onRetrieveData);

    }
    private Emitter.Listener onRetrieveData = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject object= (JSONObject) args[0];
                    try {
                        String noidung=object.getString("noidung");

                        txtDistance.setText("From Server distanceapp.herokuapp.com \nDistance is: "+noidung+" km.");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(googleMap!=null){
            mMap=googleMap;
            Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();

            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationChangeListener(listener);
        }
        else Toast.makeText(this, "Map is null", Toast.LENGTH_SHORT).show();
    }


    GoogleMap.OnMyLocationChangeListener listener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            String latitude,longitude;
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());

            loadLatLongCurrent(latitude,longitude);

            if (mMap != null) {
                mMap.clear();
                if(!cbInput.isChecked()) {
                    LatLng loc1 = new LatLng(
                            location.getLatitude(),
                            location.getLongitude());
                    Marker mMarker1 = mMap.addMarker(new
                            MarkerOptions().position(loc1).title("Vị trí của bạn")
                            .snippet("Huỳnh Hải")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map5)));

                    if(!latitude2.equals("")&&!longitude2.equals("")) {
                        LatLng loc2 = new LatLng(
                                Double.parseDouble(latitude2), Double.parseDouble(longitude2));
                        Marker mMarker2 = mMap.addMarker(new
                                MarkerOptions().position(loc2).title("Someone")
                                .snippet("Vị trí khác"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc2, 8.5f));

                        PolylineOptions line=
                                new PolylineOptions().add(loc1,loc2)
                                         .width(6).color(Color.RED);

                        mMap.addPolyline(line);

                        mMarker2.showInfoWindow();
                        mMarker1.showInfoWindow();
                    }
                }
                else if(!latitude1.equals("")&&!longitude1.equals("")&&!latitude2.equals("")&&!longitude2.equals("")) {
                        LatLng loc1 = new LatLng(
                                Double.parseDouble(latitude1), Double.parseDouble(longitude1));
                        LatLng loc2 = new LatLng(
                                Double.parseDouble(latitude2), Double.parseDouble(longitude2));
                        Marker mMarker1 = mMap.addMarker(new
                                MarkerOptions().position(loc1).title("Vị trí của bạn")
                                .snippet("Huỳnh Hải")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map5)));
                        Marker mMarker2 = mMap.addMarker(new
                                MarkerOptions().position(loc2).title("Someone")
                                .snippet("Vị trí khác"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc2, 8.5f));

                        PolylineOptions line=
                                new PolylineOptions().add(loc1,loc2)
                                        .width(6).color(Color.RED);

                        mMap.addPolyline(line);

                        mMarker2.showInfoWindow();
                        mMarker1.showInfoWindow();
                    }
                }


            }



    };

    public void loadLatLongCurrent(String lat,String longitude){
        if(!cbInput.isChecked()){
            etxtLat1.setText(lat);
            etxtLong1.setText(longitude);
        }
    }
    private void randomLongLat(){
        Random random=new Random();
        int numLong1,numLat1,numLong2,numLat2;
        int min=100000;
        int max=999999;
        numLong2= random.nextInt(max-min+1)+min;
        numLat2=random.nextInt(max-min+1)+min;

        etxtLong2.setText("106."+numLong2);
        etxtLat2.setText("10."+numLat2);
        if(cbInput.isChecked()){
            numLong1= random.nextInt(max-min+1)+min;
            numLat1=random.nextInt(max-min+1)+min;
            etxtLong1.setText("106."+numLong1);
            etxtLat1.setText("10."+numLat1);

        }


    }

}
