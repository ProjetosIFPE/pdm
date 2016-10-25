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

    private final List<String> musicas;

    public MusicaAdapter(Context context, List<Musica> musicas, MusicaOnClickListener musicaOnClickListener) {
        this.context = context;
        this.musicas = createListOfMusic();
        this.musicaOnClickListener = musicaOnClickListener;
    }



    @Override
    public MusicaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_musicas, parent, false);
        MusicaViewHolder musicaHolder = new MusicaViewHolder(view);
        return musicaHolder;
    }

    @Override
    public void onBindViewHolder(MusicaViewHolder holder, int position) {
        String musica = musicas.get(position);

        holder.textView.setText(musica);

        holder.itemView.setOnClickListener(getOnClickListener(holder, position));
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
        CardView cardView;
        TextView textView;

        public MusicaViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.music_name);
            cardView = (CardView) itemView.findViewById(R.id.card_viewMusic);
        }
    }


    private List<String> createListOfMusic(){
        List<String> list = new ArrayList<>();
        list.add("Gangsta");
        list.add("Bohemian Rhapsody");
        list.add("Fortunate Son");
        list.add("Heathens");
        list.add("I Started a Joke");
        list.add("Know Better");
        list.add("Medieval Warfare");
        list.add("Purple Lamborghini");
        list.add("Slippin' Into Darkness");
        list.add("Without Me");

        return list;
    }



}
