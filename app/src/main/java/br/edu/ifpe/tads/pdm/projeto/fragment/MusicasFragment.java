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
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.activity.MediaPlayerActivity;
import br.edu.ifpe.tads.pdm.projeto.adapter.MusicaAdapter;
import br.edu.ifpe.tads.pdm.projeto.application.ApplicationService;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.domain.musica.Musica;
import br.edu.ifpe.tads.pdm.projeto.domain.musica.MusicaService;
import br.edu.ifpe.tads.pdm.projeto.service.PlayerService;
import br.edu.ifpe.tads.pdm.projeto.util.NetworkUtil;
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;


public class MusicasFragment extends BaseFragment {

    protected RecyclerView recyclerView;

    private List<Musica> musicas;

    private MusicaService musicaService;

    private PlayerService playerService;

    public static final String MUSICAS = "MUSICAS";

    public static final String FILME = "FILME";

    protected ProgressBar progressBarMusicas;

    private Filme filme;


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
            filme = (Filme) arguments.getSerializable(FILME);
            carregarMusicas(filme);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musicas, container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewMusicas);
        progressBarMusicas = (ProgressBar) view.findViewById(R.id.progressRecyclerViewMusic);
        recyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(Boolean.TRUE);

        esconderListaMusicas();
        carregarMusicas(filme);


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
                mostrarListaMusicas();
                musicas = response;
                if(musicas.isEmpty()){
                    Toast toast = Toast.makeText(getContext(),"Playlist não disponivel",Toast.LENGTH_SHORT);
                    toast.show();
                    recyclerView.setAdapter(new MusicaAdapter(getContext(), musicas, onClickMusica()));
                }else
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

    private void esconderListaMusicas() {
        if (recyclerView != null && progressBarMusicas != null) {
            recyclerView.setVisibility(View.GONE);
            progressBarMusicas.setVisibility(View.VISIBLE);
        }
    }

    private void mostrarListaMusicas() {
        if (recyclerView != null && progressBarMusicas != null) {
            progressBarMusicas.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

}
