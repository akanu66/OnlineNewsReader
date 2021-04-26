package uk.ac.tees.aad.W9497217;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class SingleNews extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_news);

        TextView title = findViewById(R.id.singleTitle);
        ImageView img = findViewById(R.id.singleImage);
        TextView conte = findViewById(R.id.singleContent);


        title.setText( getIntent().getExtras().getString("title"));
        conte.setText(getIntent().getExtras().getString("content" +
                "") );

        Glide.with(this).load( getIntent().getExtras().getString("image")).into(img);
    }


}
