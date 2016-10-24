package br.edu.ifpe.tads.pdm.projeto.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.parceler.apache.commons.lang.StringUtils;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;

/**
 * Created by Edmilson Santana on 30/09/2016.
 */

public class FilmeAdapter extends RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder> {

    protected static final String TAG = FilmeAdapter.class.getSimpleName();

    private final List<Filme> filmes;

    private final Context context;

    private FilmeOnClickListener filmeOnClickListener;

    public FilmeAdapter(Context context, List<Filme> filmes, FilmeOnClickListener filmeOnClickListener) {
        this.context = context;
        this.filmes = filmes;
        this.filmeOnClickListener = filmeOnClickListener;
    }

    @Override
    public FilmeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.filmes_listitem, parent, false);
        FilmeViewHolder holder = new FilmeViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FilmeViewHolder holder, int position) {
        Filme filme = filmes.get(position);

        holder.progressBar.setVisibility(View.VISIBLE);
        if (StringUtils.isNotEmpty(filme.getUrlPoster())) {
            Picasso.with(context).load(filme.getUrlPoster()).fit().into(holder.img,
                    this.getImageLoadCallback(holder));
        }
        holder.itemView.setOnClickListener(getOnClickListener(holder, position));
    }

    /**
     *  Gerencia dos eventos  de sucesso ou falha ao realizar o carregamento de uma imagem
     *  do poster de um filme da lista
     *  @param filmeViewHolder
     * **/
    public Callback getImageLoadCallback(final FilmeViewHolder filmeViewHolder) {
        return new Callback() {
            @Override
            public void onSuccess() {
                filmeViewHolder.progressBar.setVisibility(View.GONE);

            }
            @Override
            public void onError() {
                filmeViewHolder.progressBar.setVisibility(View.GONE);
            }
        };
    }

    /**
     *  Aplica a ação de click através da interface FilmeOnClickListener
     *  @param filmeViewHolder
     *  @param position
     * **/
    public View.OnClickListener getOnClickListener(final FilmeViewHolder filmeViewHolder, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( filmeOnClickListener != null ) {
                    filmeOnClickListener.onClickFilme(filmeViewHolder.itemView, position);
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return this.filmes != null ? this.filmes.size() : 0;
    }


    public interface FilmeOnClickListener {
        public void onClickFilme(View view, int idx);
    }

    class FilmeViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        ProgressBar progressBar;
        CardView cardView;

        public FilmeViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.filme_poster);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressImg);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}
