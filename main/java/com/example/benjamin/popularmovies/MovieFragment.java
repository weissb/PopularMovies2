package com.example.benjamin.popularmovies;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * @author benjamin
 * @date 12.07.2015
 * @udacity nanodegree phase 1
 *
 */


//sorry for typos ect.. was rly bsy over the last 5 week from work and havent had the time to code, so i had to do all today and now its rly late and i am kinda tired!
//i hope all is okay for basic passing, i will try to get much more time for my phase 2 and make it nicer looking and try to clean the code!
//greetings benjamin

public class MovieFragment extends Fragment {
    //vars
    private final String LOG_TAG = MovieFragment.class.getSimpleName();
    ArrayList<MovieItem> movieArrayList;
    View rootView;



    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //start fetching of movies. goes in background.
        //ui picture will be build over method setMovieImage which is executet on onPostExecute from fetchData AsyncTask
        //so datas can be loadet at start and whanever i want ober the fetch class and gui will always be responsible

        FetchMovieTask fetchMovie = new FetchMovieTask();
        fetchMovie.execute("popularity.desc");

        rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        return rootView;

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.moviefragment,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {
            FetchMovieTask fetchMovie = new FetchMovieTask();
            fetchMovie.execute("popularity.desc");
            return true;
        }

        if (id == R.id.action_rating) {
            FetchMovieTask fetchMovie = new FetchMovieTask();
            //highest ratet...result suxx but that is what i have to search!
            fetchMovie.execute("vote_average.desc");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //method for write images in the gridview
    //automatic executes after fetchdata (onPostExecute)
    public void setMovieImages()
    {
        Context context = getActivity().getApplicationContext();
        //base url pictures
        final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
        String[]posterUrls = new String[movieArrayList.size()];
        for(int i = 0 ; i< posterUrls.length ;i++)
        {
            //replace picture if there is no poster available
            if(movieArrayList.get(i).getsPosterPath()=="null") {
                posterUrls[i] = "http://i.i.cbsi.com/cnwk.1d/i/tim//2010/01/31/fmimg2276274178127844128.jpg";
            }
            else
            {
                posterUrls[i] = IMAGE_URL + movieArrayList.get(i).getsPosterPath();
            }
            Log.v(LOG_TAG, "Movie entry: " +  posterUrls[i]);
        }

        GridView gView = (GridView) rootView.findViewById(R.id.gridViewMovie);

        gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getActivity().getApplicationContext();

               // Log.v(LOG_TAG, "DEBUGGG***** view.getId(): " + view.getId());
               // Log.v(LOG_TAG, "DEBUGGG***** position: " + position);
               // Log.v(LOG_TAG, "DEBUGGG***** id: " + id);

                //create explizit intent for details
                //send position whit it so we can do the details
                //also send the movieitem from the current movie which has to be serialazable for that.


                Intent detailIntent = new Intent(getActivity(), DetailMovieActivity.class)
                        .putExtra("position", position)
                        .putExtra("movieItem",movieArrayList.get(position));

                startActivity(detailIntent);

            }
        });

        gView.setAdapter(new ImageListAdapter(context, posterUrls));

    }

    //method for parse the fetchet data directly into the movieitem class and write it into an array
    private ArrayList<MovieItem> getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_RESULTS = "results";
        final String OWM_BACKDROP = "backdrop_path";
        final String OWM_ID = "id";
        final String OWM_ORIGINAL_TITLE = "original_title";
        final String OWM_OVERVIEW = "overview";
        final String OWM_RELEASE_DATE = "release_date";
        final String OWM_POSTER_PATH = "poster_path";
        final String OWM_VOTE_AVERAGE = "vote_average";

        //target array initialisation
        movieArrayList = new  ArrayList<MovieItem>();

        //get the json - star tag is results. no more inner circles so we can get all from that
        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieJasonArray = movieJson.getJSONArray(OWM_RESULTS);

        for(int i = 0; i < movieJasonArray.length(); i++) {

            // Get the JSON object representing the day
            JSONObject movieResult = movieJasonArray.getJSONObject(i);
            movieArrayList.add(new MovieItem(
                            movieResult.getString(OWM_BACKDROP),
                            movieResult.getString(OWM_ID),
                            movieResult.getString(OWM_ORIGINAL_TITLE),
                            movieResult.getString(OWM_OVERVIEW),
                            movieResult.getString(OWM_RELEASE_DATE),
                            movieResult.getString(OWM_POSTER_PATH),
                            movieResult.getString(OWM_VOTE_AVERAGE)));

        }
        //debug
        //  for (MovieItem s : movieArrayList) {
        //    Log.v(LOG_TAG, "Movie entry: " + s.getsOriginalTitle());
        //}
        return movieArrayList;

    }

    /**
     * inner class for get movie list from themoviedb.org
     * run in background because of freezing ui

     *
     */
    public class FetchMovieTask extends AsyncTask<String, Integer, String[]>{

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
           // List<String> movie = new ArrayList<String>();
           // for (MovieItem s : movieArrayList) {
           //     movie.add(s.getsOriginalTitle());
            //}

            setMovieImages();
        }

        @Override
        //@param String "kindofmovie.sorting"
        protected String[] doInBackground(String... params) {
            if(params.length == 0)
            {
                return null;
            }
            //vars
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String[] movieData =null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;


            try {
                //Strings for moviedb api
                final String API_KEY = "7e7c8f7849c15a04cc45769a2303ba0c";
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String QUERY_PARAM = "sort_by";
                final String API_PARAM = "api_key";


                //create get movie list uri
                Uri movieUrl = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM,params[0])
                        .appendQueryParameter(API_PARAM,API_KEY)
                        .build();

                URL url = new URL(movieUrl.toString());
                // Create the request to moviedb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();



                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;

                }
                movieJsonStr = buffer.toString();
                try {
                    movieArrayList = getMovieDataFromJson(movieJsonStr);


                } catch (JSONException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                    e.printStackTrace();
                }



                //Log.v(LOG_TAG,"Movie Json Test: "+ movieJsonStr);
                //Log.v(LOG_TAG,"Movie Url Test: "+ url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }

            }
            return movieData;
        }






    }

    //adapter fuer die image view
    public class ImageListAdapter extends ArrayAdapter {
        private Context context;
        private LayoutInflater inflater;

        private String[] imageUrls;

        public ImageListAdapter(Context context, String[] imageUrls) {
            super(context, R.layout.movie_grid, imageUrls);

            this.context = context;
            this.imageUrls = imageUrls;

            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.movie_grid, parent, false);
            }

            Picasso
                    .with(context)
                    .load(imageUrls[position])
                    .fit() // will explain later
                    .into((ImageView) convertView);

            return convertView;
        }
    }




}
