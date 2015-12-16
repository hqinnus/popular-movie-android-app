package com.android.huangq.popularmovieapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangq on 16/12/2015.
 */
public class MovieDataParser {

    private static final String KEY_RESULT = "results";

    public static List<Movie> parsePopularMovies(String raw) throws JSONException{
        List<Movie> result = new ArrayList<>();

        JSONObject moviesJson = new JSONObject(raw);
        JSONArray movieArray = moviesJson.getJSONArray(KEY_RESULT);

        String KEY_TITLE = "original_title";
        String KEY_POSTER = "poster_path";
        String KEY_OVERVIEW = "overview";
        String KEY_RATING = "vote_average";
        String KEY_RELEASE_DATE = "release_date";

        for(int i=0; i<movieArray.length(); i++){
            JSONObject movieJson = movieArray.getJSONObject(i);
            Movie movie = new Movie();

            if(movieJson.has(KEY_TITLE))
                movie.setTitle(movieJson.getString(KEY_TITLE));
            if(movieJson.has(KEY_RATING))
                movie.setRating(movieJson.getString(KEY_RATING));
            if(movieJson.has(KEY_RELEASE_DATE))
                movie.setRelease_date(movieJson.getString(KEY_RELEASE_DATE));
            if(movieJson.has(KEY_OVERVIEW))
                movie.setOverview(movieJson.getString(KEY_OVERVIEW));
            if(movieJson.has(KEY_POSTER))
                movie.setPoster_path(movieJson.getString(KEY_POSTER));

            if(!movie.isValid())
                continue;

            result.add(movie);
        }

        return result;
    }

}
