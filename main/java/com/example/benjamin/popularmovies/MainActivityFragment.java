package com.example.benjamin.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    ArrayList<MovieItem> movieArrayList;
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();



    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FetchMovieTask fetchMovie = new FetchMovieTask();
        fetchMovie.execute("popularity.desc");
        return inflater.inflate(R.layout.fragment_main, container, false);



    }
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

        for (MovieItem s : movieArrayList) {
            Log.v(LOG_TAG, "Movie entry: " + s.toString());
        }
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
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;

                }
                movieJsonStr = buffer.toString();
                try {
                    ArrayList<MovieItem> movieArrayLiswt = getMovieDataFromJson(movieJsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                    e.printStackTrace();
                }



                Log.v(LOG_TAG,"Movie Json Test: "+ movieJsonStr);
                Log.v(LOG_TAG,"Movie Url Test: "+ url);
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




}
