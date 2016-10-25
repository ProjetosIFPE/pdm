package br.edu.ifpe.tads.pdm.projeto.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.adapter.MusicaAdapter;
import br.edu.ifpe.tads.pdm.projeto.domain.musica.Musica;


public class PlaylistFragment extends Fragment {

    protected RecyclerView recyclerView;

    private List<Musica> musicas;

    public  static PlaylistFragment newInstance(Bundle bundle) {
        PlaylistFragment playlistFragment = new PlaylistFragment();
        playlistFragment.setArguments(bundle);
        return playlistFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewMusicas);
        recyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(Boolean.TRUE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setAdapter(new MusicaAdapter(getContext(), musicas, onClickMusica()));
    }

    public  MusicaAdapter.MusicaOnClickListener onClickMusica() {
        return new MusicaAdapter.MusicaOnClickListener() {
            @Override
            public void onClickMusica(View view, int idx) {
                Toast.makeText(getContext(), "Clicou música", Toast.LENGTH_SHORT).show();
            }
        };
    }

}
