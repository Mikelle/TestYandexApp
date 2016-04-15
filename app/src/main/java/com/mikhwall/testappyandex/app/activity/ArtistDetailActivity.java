package com.mikhwall.testappyandex.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhwall.testappyandex.app.R;
import com.mikhwall.testappyandex.app.helpers.ArtistHelper;
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

        TextView genresTextView = (TextView) findViewById(R.id.detail_genres);
        TextView infoTextView = (TextView) findViewById(R.id.detail_info);
        TextView biographyTextView = (TextView) findViewById(R.id.detail_biography);
        ImageView imageImageView = (ImageView) findViewById(R.id.big_cover);

        String genres = artist.getGenres();
        String info = ArtistHelper.buildArtistAlbumsInfo(artist, this) + "  .  "
                + ArtistHelper.buildArtistTracksInfo(artist, this);
        String biography = artist.getDescription();

        genresTextView.setText(genres);
        infoTextView.setText(info);
        biographyTextView.setText(biography);

        Picasso.with(this).load(artist.getCover_big())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(imageImageView);

    }
}
