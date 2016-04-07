package com.mikhwall.testappyandex.app.adapter;


import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhwall.testappyandex.app.R;
import com.mikhwall.testappyandex.app.model.Artist;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.MyViewHolder> {

    private List<Artist> artistList;
    private Context context;

    public ArtistAdapter(Context context, List<Artist> artistList) {
        this.context = context;
        this.artistList = artistList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, genres;
        public ImageView small_cover;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            genres = (TextView) view.findViewById(R.id.genres);
            small_cover = (ImageView) view.findViewById(R.id.small_cover);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Artist artist = artistList.get(position);
        holder.name.setText(artist.getName());
        holder.genres.setText(artist.getGenres());
        Picasso.with(context).load(artist.getCover_small())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.small_cover);
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

}
