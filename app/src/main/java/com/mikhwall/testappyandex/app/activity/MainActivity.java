package com.mikhwall.testappyandex.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.mikhwall.testappyandex.app.DividerItemDecoration;
import com.mikhwall.testappyandex.app.R;
import com.mikhwall.testappyandex.app.adapter.ArtistAdapter;
import com.mikhwall.testappyandex.app.helpers.ConnectionHelper;
import com.mikhwall.testappyandex.app.data.Artist;
import com.mikhwall.testappyandex.app.data.DataTransition;

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
import java.util.List;

/**
 * Main activity for artists list viewing.
 *
 * @author Mikhail Wall
 */

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArtistAdapter artistAdapter;
    private List<Artist> artistsList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    //initialization of helpers variables for saving state of recyclerViewList
    private static int index = -1;
    private static int top = -1;

    //methods for saving state of recyclerViewList
    @Override
    public void onPause() {
        super.onPause();
        index = layoutManager.findFirstVisibleItemPosition();
        View v = recyclerView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - recyclerView.getPaddingTop());
    }

    @Override
    public void onResume(){
        super.onResume();
        if (index != -1) {
            layoutManager.scrollToPositionWithOffset(index, top);
        }
    }

    //method for creating activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.main_activity_title);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        artistAdapter = new ArtistAdapter(MainActivity.this, artistsList);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getArtistRefresh();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Artist artist = artistsList.get(position);
                Toast.makeText(getApplicationContext(), artist.getName() + " is selected!",
                        Toast.LENGTH_SHORT).show();

                artistSelected(artist);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        
        if (ConnectionHelper.isOnline(getApplicationContext())) {
            new ParseTask().execute();
        } else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getApplicationContext(), "You are offline. Please, connect " +
                    "to the internet", Toast.LENGTH_SHORT).show();
        }
    }

    // refresh activity with swipeRefresh
    private void getArtistRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                artistAdapter = new ArtistAdapter(MainActivity.this, artistsList);
                recyclerView.setAdapter(artistAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 400);
    }

    // transite data to ArtistDetailActivity
    private void artistSelected(Artist artist) {
        Intent detailIntent = new Intent(getApplicationContext(), ArtistDetailActivity.class);
        detailIntent.putExtra(DataTransition.ARTIST, artist);
        startActivity(detailIntent);
    }

    // class for download json data and parsing it into Artist model
    private class ParseTask extends AsyncTask<Void, Void, Void> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        // download data
        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://cache-spb09.cdn.yandex.net/download.cdn.yandex.net/" +
                        "mobilization-2016/artists.json");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

                JSONArray array = new JSONArray(resultJson);
                for (int i = 0; i < array.length(); i++) {
                    artistsList.add(parseJSON(array.getJSONObject(i)));
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        // set Artist into ArtistAdapter
        @Override
        protected void onPostExecute(Void result) {
            recyclerView.setAdapter(artistAdapter);
        }
        // parse JSON into Artist
        public Artist parseJSON(JSONObject obj) throws JSONException {
            Artist artist = new Artist();
            artist.setId(obj.getLong(DataTransition.JSONobj.TAG_ID));
            artist.setName(obj.getString(DataTransition.JSONobj.TAG_NAME));
            artist.setGenres(obj.getString(DataTransition.JSONobj.TAG_GENRES).
                    replace("[\"", "").
                    replace("\"]", "").
                    replace("\",\"",", "));
            if (obj.has(DataTransition.JSONobj.TAG_LINK)) {
                artist.setLink(obj.getString(DataTransition.JSONobj.TAG_LINK));
            }
            artist.setTracks(obj.getInt(DataTransition.JSONobj.TAG_TRACKS));
            artist.setAlbums(obj.getInt(DataTransition.JSONobj.TAG_ALBUMS));
            artist.setDescription(obj.getString(DataTransition.JSONobj.TAG_DESCRIPTION));
            artist.setCover_small(obj.getJSONObject(DataTransition.JSONobj.TAG_COVER)
                    .getString(DataTransition.JSONobj.TAG_COVER_SMALL));
            artist.setCover_big(obj.getJSONObject(DataTransition.JSONobj.TAG_COVER)
                    .getString(DataTransition.JSONobj.TAG_COVER_BIG));
            return artist;
        }
    }

    // interface for class for click touch listening
    private interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    // class for click touch listening
    private static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView,
                                     final MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
