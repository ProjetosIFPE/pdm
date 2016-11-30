package br.edu.ifpe.tads.pdm.projeto.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.adapter.MusicaAdapter;
import br.edu.ifpe.tads.pdm.projeto.application.ApplicationService;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.domain.musica.Musica;
import br.edu.ifpe.tads.pdm.projeto.domain.musica.MusicaService;
import br.edu.ifpe.tads.pdm.projeto.service.MediaPlayerService;
import br.edu.ifpe.tads.pdm.projeto.util.IOUtil;
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;


public class MusicasFragment extends BaseFragment {

    public static final String SERVICE_BOUND = "SERVICE_BOUND";
    public static final String FILME = "FILME";
    protected RecyclerView recyclerView;
    protected ProgressBar progressBarMusicas;
    boolean serviceBound = Boolean.FALSE;
    private MediaPlayerService mediaPlayerService;
    private List<Musica> musicas;
    private MusicaService musicaService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            mediaPlayerService = binder.getService();
            serviceBound = Boolean.TRUE;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = Boolean.FALSE;
        }
    };


    public static MusicasFragment newInstance(Bundle bundle) {
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
        View view = inflater.inflate(R.layout.fragment_musicas, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewMusicas);
        progressBarMusicas = (ProgressBar) view.findViewById(R.id.progressRecyclerViewMusic);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(Boolean.TRUE);

        esconderListaMusicas();

        return view;
    }

    /**
     * Inicia tarefa assíncrona para carregar as músicas de um filme
     *
     * @param filme
     */
    public void carregarMusicas(Filme filme) {
        startTask(consultarMusicas(filme));
    }

    /**
     * Tarefa assíncrona para carregar as músicas de um filme
     *
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
            public void updateView(List<Musica> musicas) {
                mostrarListaMusicas();
                verificarResultadoConsultarMusicas(musicas);
            }
        };
    }

    private void verificarResultadoConsultarMusicas(List<Musica> musicas) {
        if (musicas != null && !musicas.isEmpty()) {
            removerAlertaNenhumResultado();
            atualizarRecyclerView(musicas);
        } else {
            adicionarAlertaNenhumResultadoDisponível();
        }
    }

    /**
     * Remover fragmento de alerta de nenhum resultado disponível
     */
    public void removerAlertaNenhumResultado() {
        Fragment fragment = getChildFragmentManager().findFragmentByTag(
                AlertNoResultsFragment.ALERT_NO_RESULTS_FRAGMENT);
        if (fragment != null) {
            getChildFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .remove(fragment).commit();
        }
    }

    private void atualizarRecyclerView(List<Musica> musicas) {
        this.musicas = musicas;
        recyclerView.setAdapter(new MusicaAdapter(getContext(), musicas, onClickMusica()));
    }


    /**
     * Adiciona o fragmento com um alerta de nenhum resultado disponível
     */
    public void adicionarAlertaNenhumResultadoDisponível() {
        Bundle arguments = new Bundle();
        AlertNoResultsFragment alertNoResultsFragment = AlertNoResultsFragment.newInstance(arguments);
        if (getActivity() != null) {
            getChildFragmentManager().beginTransaction()
                    .add(R.id.fragment_musicas, alertNoResultsFragment,
                            AlertNoResultsFragment.ALERT_NO_RESULTS_FRAGMENT).commit();
        }
    }

    public MusicaAdapter.MusicaOnClickListener onClickMusica() {
        return new MusicaAdapter.MusicaOnClickListener() {
            @Override
            public void onClickMusica(View view, int idx) {
                playAudio(idx);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putBoolean(SERVICE_BOUND, serviceBound);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            serviceBound = savedInstanceState.getBoolean(SERVICE_BOUND);
        }
    }

    private void playAudio(int indexMusicaTocando) {
        if (!serviceBound) {
            IOUtil.putInt(getContext(), MediaPlayerService.AUDIO_INDEX, indexMusicaTocando);
            IOUtil.putList(getContext(), MediaPlayerService.AUDIO_LIST, musicas);

            Intent playerIntent = new Intent(getActivity(), MediaPlayerService.class);
            getActivity().startService(playerIntent);
            getActivity().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            IOUtil.putInt(getContext(), MediaPlayerService.AUDIO_INDEX, indexMusicaTocando);
            Intent broadcastIntent = new Intent(MediaPlayerService.BROADCAST_PLAY_NEW_AUDIO);
            getActivity().sendBroadcast(broadcastIntent);
        }
    }
}


