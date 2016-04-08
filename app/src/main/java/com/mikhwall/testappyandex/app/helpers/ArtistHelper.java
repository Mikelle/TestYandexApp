package com.mikhwall.testappyandex.app.helpers;

import android.content.Context;

import com.mikhwall.testappyandex.app.R;
import com.mikhwall.testappyandex.app.model.Artist;

public class ArtistHelper {

    private static String getRightWord(int amount, String single, String several, String many) {
        int value = amount % 100;

        if (value > 10 && value < 20) {
            return many;
        } else {
            value = amount % 10;

            if (value == 1) {
                return single;
            } else if (value > 1 && value < 5) {
                return several;
            } else {
                return many;
            }
        }
    }

    public static String buildArtistAlbumsInfo(Artist artist, Context context) {

        int albums = artist.getAlbums();

        return albums + " " + getRightWord(albums,
                context.getString(R.string.albums_single),
                context.getString(R.string.albums_several),
                context.getString(R.string.albums_many));
    }

    public static String buildArtistTracksInfo(Artist artist, Context context) {

        int tracks = artist.getTracks();

        return tracks + " " + getRightWord(tracks,
                context.getString(R.string.tracks_single),
                context.getString(R.string.tracks_several),
                context.getString(R.string.tracks_many));
    }

}
