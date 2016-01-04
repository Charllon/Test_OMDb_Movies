package com.charllonlobo.omdbmovies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.charllonlobo.omdbmovies.db_comunication.AppController;
import com.charllonlobo.omdbmovies.db_comunication.DatabaseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class SearchFragment extends Fragment {
    private static String TAG = "SearchFragment";
    // Declare Variables

    private View view;
    private DatabaseHandler db;
    private String search_text;
    private String home;
    // json object response url
    private String urlJsonObj;
    // Progress dialog
    private ProgressDialog pDialog;
    private TextView txtResponse;
    // temporary string to show the parsed response
    private String jsonResponse;

    public String getSearch_text() {
        return search_text;
    }

    public void setSearch_text(String search_text) {
        this.search_text = search_text;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.search_movie, container, false);

        // Set progress dialog
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setMessage(getResources().getString(R.string.progress_dialog_message));
        pDialog.setCancelable(false);

        txtResponse = (TextView) view.findViewById(R.id.response);
        db = new DatabaseHandler(view.getContext());

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.hide();

        // Only to delete the SQlite data base
        //view.getContext().deleteDatabase("filmsManager");

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_drawer, menu);
        super.onCreateOptionsMenu(menu, inflater);

        // Set search box in actionbar
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.setQueryHint(getResources().getString(R.string.searchbox_query_hint));

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {

                // If search box is empty call the fragment to refresh it.
                if (newText.isEmpty()) {
                    setSearch_text(null);
                    Fragment newFragment;
                    newFragment = new SearchFragment();
                    Bundle args = new Bundle();
                    newFragment.setArguments(args);

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_container, newFragment).commit();
                }
                return true;
            }

            // Getting the value "query" which is entered in the search box.
            public boolean onQueryTextSubmit(String query) {

                search_text = query;
                search_text = search_text.replace(" ", "+");
                // Save the value in the url to search.
                urlJsonObj = "http://www.omdbapi.com/?t=" + search_text + "&y=&plot=full&r=json&type=movie";
                makeJsonObjectRequest();

                searchView.clearFocus();

                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
    }

    /**
     * Method to make json object request.
     */
    private void makeJsonObjectRequest() {

        // show the progress dialog
        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, "", new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Set an object with response values.
                    final MovieItem movieItemDescription = new MovieItem();
                    movieItemDescription.setTitle(response.getString("Title"));
                    movieItemDescription.setCountry(response.getString("Country"));
                    movieItemDescription.setGenre(response.getString("Genre"));
                    movieItemDescription.setRuntime(response.getString("Runtime"));
                    movieItemDescription.setReleased(response.getString("Released"));
                    movieItemDescription.setPlot(response.getString("Plot"));
                    home = response.getString("Poster");

                    // Set text view with the values.
                    jsonResponse = "";
                    jsonResponse += getResources().getString(R.string.movie_title) + " " + movieItemDescription.getTitle() + "\n\n";
                    jsonResponse += getResources().getString(R.string.movie_contry) + " " + movieItemDescription.getCountry() + "\n\n";
                    jsonResponse += getResources().getString(R.string.movie_genre) + " " + movieItemDescription.getGenre() + "\n\n";
                    jsonResponse += getResources().getString(R.string.movie_runtime) + " " + movieItemDescription.getRuntime() + "\n\n";
                    jsonResponse += getResources().getString(R.string.movie_released) + " " + movieItemDescription.getReleased() + "\n\n";
                    jsonResponse += getResources().getString(R.string.movie_plot) + " " + movieItemDescription.getPlot() + "\n\n";

                    final ImageView mImageView;
                    mImageView = (ImageView) view.findViewById(R.id.myImage);

                    // If have an image, it will set the image view.
                    if (!home.equals("N/A")) {

                        // Retrieves an image specified by the URL, displays it in the UI.
                        ImageRequest request = new ImageRequest(home,
                                new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        mImageView.setImageBitmap(bitmap);

                                        // Save image in byte array to put in Internal data base.
                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                        //Log.d(TAG, "Image(byte): " + stream.toByteArray());
                                        movieItemDescription.setImage(stream.toByteArray());
                                    }
                                }, 0, 0, null,
                                new Response.ErrorListener() {
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(view.getContext(),
                                                "Error: " + error.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                                    }
                                });
                        // Adding request to request queue
                        AppController.getInstance().addToRequestQueue(request);
                    } else {
                        // Set image view with default image.
                        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(),
                                R.drawable.no_image);
                        mImageView.setImageBitmap(bitmap);
                    }
                    // Floating button to add movies to "my movies"
                    FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
                    fab.show();
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                            alertBuilder.setMessage(getResources().getString(R.string.alert_message));
                            alertBuilder.setCancelable(true);
                            final View vTemp = view;

                            // Alert pop up to confirm the action with "yes" or "no".
                            alertBuilder.setPositiveButton(
                                    getResources().getString(R.string.alert_alternative_yes),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            // Inserting MovieItem
                                            Log.d(TAG, "Inserting ..");
                                            db.addContact(movieItemDescription);
                                            Toast.makeText(vTemp.getContext(), R.string.alert_confirmed_action, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            alertBuilder.setNegativeButton(
                                    getResources().getString(R.string.alert_alternative_no),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = alertBuilder.create();
                            alert.show();
                        }
                    });

                    // Set text view with the movie description.
                    txtResponse.setText(jsonResponse);
                    hidepDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(view.getContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    // hide the progress dialog
                    hidepDialog();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(view.getContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}