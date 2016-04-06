package com.mikhwall.testappyandex.app.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mikhwall.testappyandex.app.R;
import com.mikhwall.testappyandex.app.adapter.ArtistAdapter;
import com.mikhwall.testappyandex.app.model.Artist;
import com.mikhwall.testappyandex.app.model.JSONobj;

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

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArtistAdapter adapter;
    private List<Artist> artistsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager aLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(aLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ArtistAdapter(artistsList);

        new ParseTask().execute();
    }

    private class ParseTask extends AsyncTask<Void, Void, Void> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://cache-spb09.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json");
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

        @Override
        protected void onPostExecute(Void result) {
            recyclerView.setAdapter(adapter);
        }

        public Artist parseJSON(JSONObject obj) throws JSONException {
            Artist artist = new Artist();
            artist.setId(obj.getLong(JSONobj.TAG_ID));
            artist.setName(obj.getString(JSONobj.TAG_NAME));
            artist.setGenres(obj.getString(JSONobj.TAG_GENRES).
                    replace("[\"", "").
                    replace("\"]", "").
                    replace("\",\"",", "));
            artist.setTracks(obj.getInt(JSONobj.TAG_TRACKS));
            artist.setAlbums(obj.getInt(JSONobj.TAG_ALBUMS));
            artist.setLink(obj.getString(JSONobj.TAG_LINK));
            artist.setDescription(obj.getString(JSONobj.TAG_DESCRIPTION));
            artist.setCover_small(obj.getJSONObject(JSONobj.TAG_COVER).getString(JSONobj.TAG_COVER_SMALL));
            artist.setCover_big(obj.getJSONObject(JSONobj.TAG_COVER).getString(JSONobj.TAG_COVER_BIG));
            return artist;
        }
    }
}
