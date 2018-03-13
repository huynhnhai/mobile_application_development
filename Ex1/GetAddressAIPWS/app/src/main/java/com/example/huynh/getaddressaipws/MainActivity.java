package com.example.huynh.getaddressaipws;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button btnStart,btnRandom;
    EditText etxtLong,etxtLat;
    TextView txtAddress;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControll();
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
                if(etxtLong.getText().toString().equals("")||etxtLat.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"Long or Latitude is null", Toast.LENGTH_SHORT).show();
                }
                else {

                    final String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                            etxtLat.getText() + ", " + etxtLong.getText() + "&sensor=true";
                    txtAddress.setText(" Request from "+url+"\n\n      Waiting...");
                    JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray array = response.getJSONArray("results");
                                        JSONObject object = array.getJSONObject(0);
                                        String address = object.getString("formatted_address");
                                        txtAddress.setText(" Request from "+url+"\n\nAddress is: " + address);
                                    } catch (JSONException e) {
                                        txtAddress.setText("Có lỗi trong lúc yêu cầu, mời thực hiện lại!");

                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    txtAddress.setText("Có lỗi trong lúc yêu cầu, mời thực hiện lại!");



                                }
                            }
                    );
                    requestQueue.add(objectRequest);
                }
            }
        });
    }

    private void addControll() {
        btnStart=findViewById(R.id.btnStart);
        etxtLat=findViewById(R.id.etxtLat);
        etxtLong=findViewById(R.id.etxtLong);
        txtAddress=findViewById(R.id.txtAddress);
        btnRandom=findViewById(R.id.btnRandom);

        requestQueue= Volley.newRequestQueue(MainActivity.this);
    }

    private void randomLongLat(){
        Random random=new Random();
        int numLong,numLat;
        int min=100000;
        int max=999999;
        numLong= random.nextInt(max-min+1)+min;//(50->100)
        numLat=random.nextInt(max-min+1)+min;//(50->100)
        etxtLong.setText("106."+numLong);
        etxtLat.setText("10."+numLat);


    }

}
