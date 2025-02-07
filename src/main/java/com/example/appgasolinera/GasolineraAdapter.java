package com.example.appgasolinera;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class GasolineraAdapter extends RecyclerView.Adapter<GasolineraAdapter.ViewHolder> {

    private Gasolinera[] localDataSet;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView gasolinera, precios;
        private final ImageView imagen;

        public ViewHolder(View view, OnItemClickListener listener) {
            super(view);
            gasolinera = view.findViewById(R.id.article_title);
            precios = view.findViewById(R.id.article_description);
            imagen = view.findViewById(R.id.featured_image);

            view.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public GasolineraAdapter(Gasolinera[] dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.gas_row_item, viewGroup, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.gasolinera.setText(localDataSet[position].gasolinera);
        viewHolder.precios.setText(localDataSet[position].precios);
        viewHolder.imagen.setImageResource(localDataSet[position].gasImage);
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
