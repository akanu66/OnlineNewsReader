package uk.ac.tees.aad.W9497217;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class NewsScreen extends AppCompatActivity {

    ListView listView;
    String mTitle[] = {"Facebook", "Whatsapp", "Twitter", "Instagram", "Youtube"};
    String mDescription[] = {"Facebook Description", "Whatsapp Description", "Twitter Description", "Instagram Description", "Youtube Description"};



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_screen);

        listView = findViewById(R.id.list_view);

        getNews(getIntent().getExtras().getString("country"));


        MyAdapter adapter = new MyAdapter(this, mTitle, mDescription);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position ==  0) {
                    Toast.makeText(getApplicationContext(), "Facebook Description", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(getApplicationContext(), "Whatsapp Description", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(getApplicationContext(), "Twitter Description", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(getApplicationContext(), "Instagram Description", Toast.LENGTH_SHORT).show();
                }
                if (position ==  0) {
                    Toast.makeText(getApplicationContext(), "Youtube Description", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void getNews(String Country){


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://newsapi.org/v2/top-headlines?country="+Country+"&apiKey=f8efb3bc73074d1fb98131e245f444f8";

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
}

class MyAdapter extends ArrayAdapter<String> {

    Context context;
    String rTitle[];
    String rDescription[];


    MyAdapter (Context c, String title[], String description[]) {
        super(c, R.layout.row, R.id.textView1, title);
        this.context = c;
        this.rTitle = title;
        this.rDescription = description;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row, parent, false);

        TextView myTitle = row.findViewById(R.id.textView1);
        TextView myDescription = row.findViewById(R.id.textView2);

        myTitle.setText(rTitle[position]);
        myDescription.setText(rDescription[position]);

        return row;
    }
}


