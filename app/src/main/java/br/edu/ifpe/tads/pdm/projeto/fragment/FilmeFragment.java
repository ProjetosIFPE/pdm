package br.edu.ifpe.tads.pdm.projeto.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.activity.FilmeActivity;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;


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

            Bundle musicasFragmentArgs = new Bundle();
            musicasFragmentArgs.putSerializable(MusicasFragment.FILME, filme);
            MusicasFragment musicasFragment = MusicasFragment.newInstance(musicasFragmentArgs);

            getChildFragmentManager().beginTransaction()
                    .add(R.id.fragment_musicas, musicasFragment).commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b){
        View view = inflater.inflate(R.layout.fragment_filme, container, false);
        TextView filmeDescricao = (TextView) view.findViewById(R.id.filme_descricao);
        filmeDescricao.setText(filme.getSinopse());
        return view;
    }

}
