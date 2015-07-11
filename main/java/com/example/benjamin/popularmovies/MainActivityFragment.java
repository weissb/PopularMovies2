package com.example.benjamin.popularmovies;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    public ArrayAdapter<String> adArrayAdapter;
    ArrayList<MovieItem> movieArrayList;
    View rootView;



    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FetchMovieTask fetchMovie = new FetchMovieTask();
        fetchMovie.execute("popularity.desc");

        rootView = inflater.inflate(R.layout.fragment_main, container, false);



        return rootView;



    }

    public void setMovieImages()
    {
        Context context = getActivity().getApplicationContext();
        //ImageView imageView = (ImageView) rootView.findViewById(R.id.imageViewMovie);
        //Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);
        final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
        String[]posterUrls = new String[movieArrayList.size()];
        for(int i = 0 ; i< posterUrls.length ;i++)
        {
            posterUrls[i]= IMAGE_URL+movieArrayList.get(i).getsPosterPath();
            Log.v(LOG_TAG, "Movie entry: " +  posterUrls[i]);
        }




        String[] eatFoodyImages = {
                "http://i.imgur.com/rFLNqWI.jpg",
                "http://i.imgur.com/C9pBVt7.jpg",
                "http://i.imgur.com/rT5vXE1.jpg",
                "http://i.imgur.com/aIy5R2k.jpg",
                "http://i.imgur.com/MoJs9pT.jpg",
                "http://i.imgur.com/S963yEM.jpg",
                "http://i.imgur.com/rLR2cyc.jpg",
                "http://i.imgur.com/SEPdUIx.jpg",
                "http://i.imgur.com/aC9OjaM.jpg",
                "http://i.imgur.com/76Jfv9b.jpg",
                "http://i.imgur.com/fUX7EIB.jpg",
                "http://i.imgur.com/syELajx.jpg",
                "http://i.imgur.com/COzBnru.jpg",
                "http://i.imgur.com/Z3QjilA.jpg",
        };



        GridView gView = (GridView) rootView.findViewById(R.id.gridViewMovie);
        gView.setAdapter(new ImageListAdapter(context, posterUrls));




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

        movieArrayList = new  ArrayList<MovieItem>();
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
