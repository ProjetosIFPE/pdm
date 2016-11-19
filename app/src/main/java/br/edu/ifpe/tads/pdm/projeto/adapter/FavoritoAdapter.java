package br.edu.ifpe.tads.pdm.projeto.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;

/**
 * Created by Douglas Albuquerque on 15/11/2016.
 */

public class FavoritoAdapter extends RecyclerView.Adapter<FavoritoAdapter.FavoritoViewHolder> {
    protected static final String TAG = FavoritoAdapter.class.getSimpleName();

    private final List<Filme> filmes;

    private final Context context;

    public FavoritoAdapter(Context context, List<Filme> filmes) {
        this.context = context;
        this.filmes = filmes;
    }

    @Override
    public FavoritoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_favoritos, parent, false);
        // set the view's size, margins, paddings and layout parameters

        FavoritoViewHolder vh = new FavoritoViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FavoritoViewHolder holder, int position) {
        holder.titleFavorito.setText(this.filmes.get(position).getTitulo());
    }

    @Override
    public int getItemCount() {
        return this.filmes.size();
    }

    public static class FavoritoViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleFavorito;
        public CardView cv;

        public FavoritoViewHolder(View v) {
            super(v);
            cv = (CardView)itemView.findViewById(R.id.cv_favorito);
            titleFavorito = (TextView)itemView.findViewById(R.id.tile_favorito);
        }
    }
}
