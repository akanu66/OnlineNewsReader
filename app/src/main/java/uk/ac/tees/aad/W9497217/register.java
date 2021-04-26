package uk.ac.tees.aad.W9497217;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class register extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText mobile = findViewById(R.id.editTextTextPersonName);
        final EditText password = findViewById(R.id.editTextTextPassword);
        final EditText name = findViewById(R.id.userName);

        Button btn = findViewById(R.id.btn_register);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String url = getResources().getString(R.string.userServerEndpoint)+"register.php?mobile="+
                        mobile.getText().toString()+"&password="+
                        password.getText().toString()+"&name="+
                        name.getText().toString();

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();

                                if(jsonObject.get("status").getAsString().equals("success")) {
                                    Toast.makeText(getApplicationContext(), "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),login.class));
                                }else {
                                    Toast.makeText(getApplicationContext(), "Account Not Created", Toast.LENGTH_SHORT).show();
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
       startActivity(new Intent(this,login.class));
    }
}