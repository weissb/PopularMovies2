package com.example.benjamin.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailMovieActivity extends ActionBarActivity {

    //vars
    private final String LOG_TAG = DetailMovieActivity.class.getSimpleName();
    private final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);


        //intent auslesen
        Intent intent = getIntent();
        int id = intent.getIntExtra("position", 0);
        //Log.v(LOG_TAG, "POSITION " + id);

        //movie item
        MovieItem movie = (MovieItem) intent.getSerializableExtra("movieItem");

        //set up gui elements
        TextView title = (TextView) findViewById(R.id.tVMovieTitle);
        title.setText(movie.getsOriginalTitle());

        TextView rating = (TextView) findViewById(R.id.tvRating);
        rating.setText(movie.getsVoteAverage());

        TextView release = (TextView) findViewById(R.id.tvRelease);
        release.setText(movie.getsReleaseDate());

        TextView plot = (TextView) findViewById(R.id.tvPlot);
        plot.setText(movie.getsOverview());

        ImageView poster = (ImageView) findViewById(R.id.ivMovieDetail);
        String posterUrl = IMAGE_URL + movie.getsPosterPath();
        Context context = getApplicationContext();
        Picasso.with(context).load(posterUrl).into(poster);





        Log.v(LOG_TAG, "TITLE " + movie.getsOriginalTitle());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
