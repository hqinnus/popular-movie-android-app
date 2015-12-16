package com.android.huangq.popularmovieapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by huangq on 16/12/2015.
 */
public class ImageAdapter extends ArrayAdapter<Movie> {
    Context mContext;
    int layoutResourceId;
    Movie data[] = null;

    public ImageAdapter(Context context, int layoutResourceId, Movie[] data){
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            //imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(8, 8, 8, 8);

            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            imageView = (ImageView) inflater.inflate(layoutResourceId, parent, false);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

        } else {
            imageView = (ImageView) convertView;
        }

        if(data[position].getPoster_path() == null)
            Picasso.with(mContext).load(R.drawable.ant_man).into(imageView);
        else Picasso.with(mContext).load(data[position].getPoster_path()).into(imageView);

        return imageView;
    }

}
