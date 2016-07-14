package com.clicktheglobe.pizzame;

/**
 * Created by Administrator on 7/11/2016.
 */
import com.clicktheglobe.pizzame.adapter.CustomListAdapter;
import com.clicktheglobe.pizzame.AppController;
import com.clicktheglobe.pizzame.model.Movie;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

public class MainActivity extends AppCompatActivity {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url = "http://jpgenagencies.org/pizzame/movies.json";
    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Search EditText
        //EditText inputSearch;
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, movieList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        //start search
       // inputSearch = (EditText) findViewById(R.id.inputSearch);

       // inputSearch.addTextChangedListener(new TextWatcher() {
          //  @Override
        //    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
          //      MainActivity.this.adapter.getFilter().filter(cs);
          //  }
         //   @Override
          //  public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
           //                               int arg3) {
                // TODO Auto-generated method stub
         //   }
          //  @Override
         //   public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            //}
       // });


        //end secrh
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(MainActivity.this, MainActivity1.class);
                //intent.putExtra("", listView.get(position).getMessage());
                String item = movieList.get(position).getTitle();
                double rating = movieList.get(position).getRating();
               // String  [] genre=  movieList.get(position).getGenre();
                intent.putExtra("movie", item);
                intent.putExtra("rating", rating);
                startActivity(intent);
            }
        });


        // changing action bar color
        //getActionBar().setBackgroundDrawable(
            //    new ColorDrawable(Color.parseColor("#1b1b1b")));

        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Movie movie = new Movie();
                                movie.setTitle(obj.getString("title"));
                                movie.setThumbnailUrl(obj.getString("image"));
                                movie.setRating(((Number) obj.get("rating"))
                                        .doubleValue());
                                movie.setYear(obj.getInt("releaseYear"));

                                // Genre is json array
                                JSONArray genreArry = obj.getJSONArray("genre");
                                ArrayList<String> genre = new ArrayList<String>();
                                for (int j = 0; j < genreArry.length(); j++) {
                                    genre.add((String) genreArry.get(j));
                                }
                                movie.setGenre(genre);

                                // adding movie to movies array
                                movieList.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


    @Override
   public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
      return true;
 }


}