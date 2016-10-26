package br.edu.ifpe.tads.pdm.projeto.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.domain.musica.Musica;

/**
 * Created by Douglas Albuquerque on 23/10/2016.
 */

public class MusicaAdapter extends RecyclerView.Adapter<MusicaAdapter.MusicaViewHolder>{

    protected static final String TAG = MusicaAdapter.class.getSimpleName();

    private final Context context;

    private MusicaOnClickListener musicaOnClickListener;

    private final List<Musica> musicas;

    public MusicaAdapter(Context context, List<Musica> musicas, MusicaOnClickListener musicaOnClickListener) {
        this.context = context;
        this.musicas = musicas;
        this.musicaOnClickListener = musicaOnClickListener;
    }



    @Override
    public MusicaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.musicas_listitem, parent, false);
        MusicaViewHolder musicaHolder = new MusicaViewHolder(view);
        return musicaHolder;
    }

    @Override
    public void onBindViewHolder(MusicaViewHolder holder, int position) {
        Musica musica = musicas.get(position);

        holder.tituloMusica.setText(musica.getTitulo());
        holder.artistaMusica.setText(musica.getArtista().getNome());

        holder.cardView.setOnClickListener(getOnClickListener(holder, position));
    }

    public View.OnClickListener getOnClickListener(final MusicaViewHolder musicaViewHolder, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( musicaOnClickListener != null ) {
                    musicaOnClickListener.onClickMusica(musicaViewHolder.itemView, position);
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return this.musicas != null ? this.musicas.size() : 0;
    }

    public interface MusicaOnClickListener {
        public void onClickMusica(View view, int idx);
    }

    class MusicaViewHolder extends RecyclerView.ViewHolder {
        TextView tituloMusica;
        TextView artistaMusica;
        CardView cardView;

        public MusicaViewHolder(View itemView) {
            super(itemView);
            tituloMusica = (TextView) itemView.findViewById(R.id.titulo_musica);
            cardView = (CardView) itemView.findViewById(R.id.card_view_musica);
            artistaMusica = (TextView) itemView.findViewById(R.id.artista_musica);
        }
    }




}
