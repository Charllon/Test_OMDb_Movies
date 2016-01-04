package com.charllonlobo.omdbmovies;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.charllonlobo.omdbmovies.my_movies.MyMovieDetails;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private static final String TAG = "ListViewAdapter";
    private Activity context;
    private LayoutInflater inflater;
    private List<MovieItem> worldpopulationlist = null;
    private ArrayList<MovieItem> arraylist;

    public ListViewAdapter(Activity context,
                           List<MovieItem> worldpopulationlist) {

        this.context = context;
        this.worldpopulationlist = worldpopulationlist;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(worldpopulationlist);
    }

    public class ViewHolder {
        TextView user_name;
        ImageView user_pic;
    }

    @Override
    public int getCount() {
        return worldpopulationlist.size();
    }

    @Override
    public Object getItem(int position) {
        return worldpopulationlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.my_movies_single, null);
            // Locate the TextViews in my_movie_descriptioniption.xml
            holder.user_name = (TextView) view.findViewById(R.id.single_title);

            // Locate the ImageView in my_movie_descriptioniption.xml
            holder.user_pic = (ImageView) view.findViewById(R.id.single_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.user_name.setText(worldpopulationlist.get(position).getTitle());
        // Set the results into ImageView
        byte[] bytes = worldpopulationlist.get(position).getImage();
        if (bytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.user_pic.setImageBitmap(bitmap);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.no_image);
            holder.user_pic.setImageBitmap(bitmap);
        }

        // Listen for ListView Item Click
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MovieItem temp = worldpopulationlist.get(position);

                Log.d(TAG, "Title =" + temp.getTitle());

                Intent intent = new Intent(context, MyMovieDetails.class);

                // Pass all data
                intent.putExtra("image",
                        (worldpopulationlist.get(position).getImage()));
                intent.putExtra("title",
                        (worldpopulationlist.get(position).getTitle()));
                intent.putExtra("country",
                        (worldpopulationlist.get(position).getCountry()));
                intent.putExtra("genre",
                        (worldpopulationlist.get(position).getGenre()));
                intent.putExtra("runtime",
                        (worldpopulationlist.get(position).getRuntime()));
                intent.putExtra("released",
                        (worldpopulationlist.get(position).getReleased()));
                intent.putExtra("plot",
                        (worldpopulationlist.get(position).getPlot()));
                context.startActivity(intent);
            }
        });

        return view;
    }
}
