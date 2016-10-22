package br.edu.ifpe.tads.pdm.projeto.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.activity.FilmeActivity;
import br.edu.ifpe.tads.pdm.projeto.domain.Filme;


public class FilmeFragment extends BaseFragment {

    private Filme filme;

    public static FilmeFragment newInstance(Bundle args) {
        FilmeFragment filmeFragment = new FilmeFragment();
        filmeFragment.setArguments(args);
        return filmeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        Log.d(TAG, String.valueOf(arguments.size()));
        if ( arguments != null ) {
            this.filme = (Filme) arguments.getSerializable(FilmeActivity.FILME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b){
        View view = inflater.inflate(R.layout.fragment_filme, container, false);
        ImageView filmePoster = (ImageView) view.findViewById(R.id.filme_poster);
        TextView filmeDescricao = (TextView) view.findViewById(R.id.filme_descricao);
        // Carrega a imagem da url do serviço de filmes e ajusta(fit) no (into) ImageView
        Picasso.with(getContext()).load(filme.getUrlPoster()).fit().into(filmePoster);
        filmeDescricao.setText(filme.getSinopse());
        return view;
    }


}
