package com.charllonlobo.omdbmovies.my_movies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.charllonlobo.omdbmovies.MovieItem;
import com.charllonlobo.omdbmovies.R;
import com.charllonlobo.omdbmovies.db_comunication.DatabaseHandler;
import com.charllonlobo.omdbmovies.ListViewAdapter;
import com.charllonlobo.omdbmovies.SearchFragment;

import java.util.ArrayList;
import java.util.List;


public class MyMovies extends Fragment {

    private static final String TAG = "MyMovies";

    public MyMovies() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_movies_list, container, false);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, new SearchFragment()).addToBackStack("tag").commit();
            }
        });

        DatabaseHandler db = new DatabaseHandler(view.getContext());

        // Reading all movies
        Log.d(TAG, "Reading all movies..");
        List<MovieItem> movies = db.getAllMovies();
        Log.d(TAG, "n= " + movies.size());

        if (movies.size() == 0){
            TextView empty = (TextView) view.findViewById(R.id.empty);

            String message = "";
            message += "Você não possui filmes adicionados.\n Aperte o botão '+' para procurar e adicionar seu filme favorito!!";
            empty.setText(message);
        }

        List<MovieItem> worldpopulationlist = new ArrayList<>();
        for (MovieItem cn : movies) {
            //db.deleteContact(cn);
            String log = "Id: " + cn.getId() + " , Title: " + cn.getTitle();
            Log.d(TAG, "Image(byte): " + cn.getImage());
            Log.d(TAG, log);
            worldpopulationlist.add(cn);
        }

        ListViewAdapter adapter = new ListViewAdapter(getActivity(), worldpopulationlist);
        // Set the adapter
        GridView mListView = (GridView) view.findViewById(android.R.id.list);
        mListView.setAdapter(adapter);

        return view;
    }
}
