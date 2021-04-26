package uk.ac.tees.aad.W9497217;

import  androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import java.io.IOException;


public class NewsScreen extends AppCompatActivity {

    ListView listView;
    String[] Title;
    String[] Description ;
    String[] images ;
    String[] content;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_screen);


        listView = findViewById(R.id.list_view);
        TextView trxt = findViewById(R.id.countryNameText);
        String text = "from "+getIntent().getExtras().getString("countryName");
        trxt.setText(text);

        getNews2(getIntent().getExtras().getString("country"));

    }

    public void getNews2(final String Country)
    {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.
                Builder().
                url("http://newsapi.org/v2/top-headlines?country="+Country+"&apiKey=f8efb3bc73074d1fb98131e245f444f8").build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                final String fata = response.body().string();
                NewsScreen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JsonObject jsonObject = new JsonParser().parse(fata).getAsJsonObject();
                        int size = jsonObject.get("articles").getAsJsonArray().size();

                         Title=new String[size];
                         Description = new String[size];
                         images = new String[size];
                         content = new String[size];

                       for( int i=0;i<size;i++ )
                       {
                           if(i<200){
                               try {
                                   JsonElement loop = jsonObject.get("articles").getAsJsonArray().get(i);
                                   Title[i] = loop.getAsJsonObject().get("title").getAsString();
                                   Description[i] = loop.getAsJsonObject().get("description").getAsString();
                                   images[i] = loop.getAsJsonObject().get("urlToImage").getAsString();
                                   content[i] = loop.getAsJsonObject().get("source").getAsJsonObject().get("name").getAsString();

                               }catch (Exception e){

                               }
                           }
                       }

                        MyAdapter adapter = new MyAdapter(getApplicationContext(), Title,  content);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Intent intent = new Intent(getApplicationContext(),SingleNews.class);
                                intent.putExtra("title",Title[position]);
                                intent.putExtra("image",images[position]);
                                intent.putExtra("content",Description[position]);
                                intent.putExtra("des",content[position]);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        finishAffinity();
    }
}

class MyAdapter extends ArrayAdapter<String> {

    Context context;
    String rTitle[];
    String rDescription[];



    MyAdapter (Context c, String title[], String description[])
    {
        super(c, R.layout.row, R.id.textView1,title);
        this.context = c;
        this.rTitle = title;
        this.rDescription = description;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row, parent, false);

        TextView myTitle = row.findViewById(R.id.textView1);
        TextView myDescription = row.findViewById(R.id.textView2);

        myTitle.setText(rTitle[position]);
        myDescription.setText(rDescription[position]);

        return row;
    }


}

