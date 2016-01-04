package com.charllonlobo.omdbmovies.my_movies;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.charllonlobo.omdbmovies.R;

public class MyMovieDetails extends AppCompatActivity {

    private static final String TAG = "MyMovieDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_movie_details);
        Bundle extras = getIntent().getExtras();
        String jsonResponse;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView textView = (TextView) findViewById(R.id.response);

        setTitle(extras.getString("title"));
        jsonResponse = "";
        jsonResponse += getResources().getString(R.string.movie_title) + " " + extras.getString("title") + "\n\n";
        jsonResponse += getResources().getString(R.string.movie_contry) + " " + extras.getString("country") + "\n\n";
        jsonResponse += getResources().getString(R.string.movie_genre) + " " + extras.getString("genre") + "\n\n";
        jsonResponse += getResources().getString(R.string.movie_runtime) + " " + extras.getString("runtime") + "\n\n";
        jsonResponse += getResources().getString(R.string.movie_released) + " " + extras.getString("released") + "\n\n";
        jsonResponse += getResources().getString(R.string.movie_plot) + " " + extras.getString("plot") + "\n\n";

        textView.setText(jsonResponse);

        ImageView imageView = (ImageView) findViewById(R.id.myImage);
        byte[] bytes = extras.getByteArray("image");
        if (bytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bitmap);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.no_image);
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
