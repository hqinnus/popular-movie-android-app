package com.android.huangq.popularmovieapplication;

import android.net.Uri;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    View rootView;
    private ImageAdapter imageAdapter;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        Movie[] movies = new Movie[4];
        movies[0] = new Movie();
        movies[1] = new Movie();
        movies[2] = new Movie();
        movies[3] = new Movie();

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        imageAdapter = new ImageAdapter(getActivity(),
                R.layout.grid_view_movie_thumbnail,
                movies);
        gridview.setAdapter(imageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        (new RequestMovieInformation()).execute();

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

        //Update the movie list
        (new RequestMovieInformation()).execute();
    }


    private class RequestMovieInformation extends AsyncTask<Void, Void, List<Movie>>{
        private final String LOG_TAG = RequestMovieInformation.class.getSimpleName();
        private static final String API_KEY = "06d54b9e78a409a6c122f7dd0feb88b9";
        private static final String POPULAR_MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie/";

        @Override
        protected List<Movie> doInBackground(Void... v){
            List<Movie> result = new ArrayList<>();

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String param_apikey = "api_key";
            String param_sort_by = "sort_by";

            String value_sort_by_popularity = "popularity.desc";

            Uri requestUri = Uri.parse(POPULAR_MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(param_apikey, API_KEY)
                    .appendQueryParameter(param_sort_by, value_sort_by_popularity)
                    .build();
            Log.v(LOG_TAG, "Request URL is "+requestUri.toString());

            try {
                URL url = new URL(requestUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream input = urlConnection.getInputStream();
                StringBuffer sb = new StringBuffer();

                if(input == null){
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(input));

                String line;
                while((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }

                if(sb.length() == 0){
                    return null;
                }

                Log.v(LOG_TAG, "Request Result: "+sb.toString());

                try {
                    result = MovieDataParser.parsePopularMovies(sb.toString());
                }catch(JSONException e){
                    Log.e(LOG_TAG, "JSON Parsing Error", e);
                    return null;
                }
            }catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;
            }finally{
                if(urlConnection != null){
                    urlConnection.disconnect();
                }

                if(reader != null){
                    try{
                        reader.close();
                    }catch(IOException e){
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return result;
        }


        protected void onPostExecute(List<Movie> movieData) {
            if(movieData != null){
                imageAdapter.clear();
                imageAdapter.addAll(movieData);
            }
        }
    }
}
