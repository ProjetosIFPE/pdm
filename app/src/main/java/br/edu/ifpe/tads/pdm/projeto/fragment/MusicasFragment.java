package br.edu.ifpe.tads.pdm.projeto.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.activity.MediaPlayerActivity;
import br.edu.ifpe.tads.pdm.projeto.adapter.MusicaAdapter;
import br.edu.ifpe.tads.pdm.projeto.application.ApplicationService;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.domain.musica.Musica;
import br.edu.ifpe.tads.pdm.projeto.domain.musica.MusicaService;
import br.edu.ifpe.tads.pdm.projeto.service.PlayerService;
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;


public class MusicasFragment extends BaseFragment {

    protected RecyclerView recyclerView;

    private List<Musica> musicas;

    private MusicaService musicaService;

    private PlayerService playerService;

    public static final String MUSICAS = "MUSICAS";

    public static final String FILME = "FILME";


    public  static MusicasFragment newInstance(Bundle bundle) {
        MusicasFragment musicasFragment = new MusicasFragment();
        musicasFragment.setArguments(bundle);
        return musicasFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicaService = ApplicationService.getInstance().getMusicaService();
        Bundle arguments = getArguments();
        if (arguments != null) {
            Filme filme = (Filme) arguments.getSerializable(FILME);
            carregarMusicas(filme);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musicas, container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewMusicas);
        recyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(Boolean.TRUE);
        return view;
    }

    /**
     * Inicia tarefa assíncrona para carregar as músicas de um filme
     * @param filme
     */
    public void carregarMusicas(Filme filme) {
        startTask(consultarMusicas(filme));
    }

    /**
     * Tarefa assíncrona para carregar as músicas de um filme
     * @param filme
     * @return
     */
    public TaskListener<List<Musica>> consultarMusicas(final Filme filme) {
        return new TaskListener<List<Musica>>() {
            @Override
            public List<Musica> execute() throws Exception {
                return musicaService.getMusicas(getContext(), filme);
            }

            @Override
            public void updateView(List<Musica> response) {
                musicas = response;
                recyclerView.setAdapter(new MusicaAdapter(getContext(), musicas, onClickMusica()));

            }
        };
    }

    public  MusicaAdapter.MusicaOnClickListener onClickMusica() {
        return new MusicaAdapter.MusicaOnClickListener() {
            @Override
            public void onClickMusica(View view, int idx) {

                Musica musica = musicas.get(idx);
                Intent intent = new Intent(getContext(), MediaPlayerActivity.class);
                intent.putExtra(MediaPlayerActivity.MUSICA, musica);
                startActivity(intent);
            }
        };
    }

}
