package com.mikhwall.testappyandex.app.model;

public class Artist {
    private long id;
    private String name;
    private String genres;
    private int tracks;
    private int albums;
    private String link;
    private String description;
    private String cover_small;
    private String cover_big;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public int getTracks() {
        return tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    public int getAlbums() {
        return albums;
    }

    public void setAlbums(int albums) {
        this.albums = albums;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover_small() {
        return cover_small;
    }

    public void setCover_small(String cover_small) {
        this.cover_small = cover_small;
    }

    public String getCover_big() {
        return cover_big;
    }

    public void setCover_big(String cover_big) {
        this.cover_big = cover_big;
    }
}