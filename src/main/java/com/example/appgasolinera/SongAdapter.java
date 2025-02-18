package com.example.appgasolinera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SongAdapter extends BaseAdapter {
    private Context context;
    private List<Cancion> cancionList;

    public SongAdapter(Context context, List<Cancion> cancionList) {
        this.context = context;
        this.cancionList = cancionList;
    }

    @Override
    public int getCount() {
        return cancionList.size();
    }

    @Override
    public Object getItem(int position) {
        return cancionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        }

        ImageView songImage = convertView.findViewById(R.id.songImage);
        TextView songTitle = convertView.findViewById(R.id.songTitle);
        TextView songArtist = convertView.findViewById(R.id.songArtist);

        Cancion cancion = cancionList.get(position);

        songImage.setImageResource(cancion.getImagen());
        songTitle.setText(cancion.getTitulo());
        songArtist.setText(cancion.getAutor());

        return convertView;
    }
}
