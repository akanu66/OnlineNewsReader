package uk.ac.tees.aad.W9497217;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView create_account = findViewById(R.id.create_account);
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),register.class));
            }
        });

        final EditText mobile = findViewById(R.id.input_mobile);
        final EditText pass = findViewById(R.id.input_password);



        Button btn_login  = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.31.181/OnlineNews/login.php?mobile="+mobile.getText().toString()+"&password="+pass.getText().toString();

                StringRequest stringRequest = new StringRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();

                                if(jsonObject.get("name").getAsString().equals("nouser")) {
                                    Toast.makeText(getApplicationContext(), "Incorrect Details", Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(getApplicationContext(), "Welcome "+jsonObject.get("name"), Toast.LENGTH_SHORT).show();
                                    SharedPreferences sharedPreferences = getSharedPreferences("state", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("login","yes");
                                    editor.apply();
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();

                            }
                        });
                requestQueue.add(stringRequest);
            }
        });

    }


    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }
}