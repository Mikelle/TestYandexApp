package com.mikhwall.testappyandex.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhwall.testappyandex.app.R;
import com.mikhwall.testappyandex.app.model.Artist;
import com.mikhwall.testappyandex.app.model.DataTransition;
import com.squareup.picasso.Picasso;

public class ArtistDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanseState) {
        super.onCreate(savedInstanseState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Artist artist = intent.getParcelableExtra(DataTransition.ARTIST);

        makeGUI(artist);
    }

    private void makeGUI(Artist artist) {
        setTitle(artist.getName());

        TextView genres = (TextView) findViewById(R.id.detail_genres);
        TextView info = (TextView) findViewById(R.id.detail_info);
        TextView biography = (TextView) findViewById(R.id.detail_biography);
        ImageView image = (ImageView) findViewById(R.id.big_cover);

        Picasso.with(this).load(artist.getCover_big())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(image);

    }
}
