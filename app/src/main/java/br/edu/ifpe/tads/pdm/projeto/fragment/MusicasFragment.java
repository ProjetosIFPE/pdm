package br.edu.ifpe.tads.pdm.projeto.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import br.edu.ifpe.tads.pdm.projeto.util.PreferencesUtil;
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;


public class MusicasFragment extends BaseConnectivityFragment implements MediaPlayerService.MediaPlayerServiceListener {

    public static final String SERVICE_BOUND = "SERVICE_BOUND";
    public static final String FILME = "FILME";
    public static final String INTENT_ATUALIZA_MUSICA = "br.edu.ifpe.tads.pdm.projeto.INTENT_ATUALIZA_MUSICA";

    protected RecyclerView recyclerView;
    protected ProgressBar progressBarMusicas;
    boolean serviceBound = Boolean.FALSE;
    private MediaPlayerService mediaPlayerService;
    private GerenciadorMusica gerenciadorMusica;
    private List<Musica> musicas;
    private Filme filme;
    private MusicaService musicaService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            mediaPlayerService = binder.getService();
            mediaPlayerService.setMediaPlayerServiceListener(MusicasFragment.this);
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
    public void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            getActivity().unbindService(serviceConnection);
        }
        getActivity().unregisterReceiver(gerenciadorMusica);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicaService = ApplicationService.getInstance().getMusicaService();
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.filme = (Filme) arguments.getSerializable(FILME);
            this.gerenciadorMusica = registrarGerenciadorMusicas();
        }
    }


    @Override
    View createFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_musicas, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewMusicas);
        progressBarMusicas = (ProgressBar) view.findViewById(R.id.progressRecyclerViewMusic);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(Boolean.TRUE);
        return view;
    }

    @Override
    void startFragmentTask() {
        esconderListaMusicas();
        carregarMusicas(filme);
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
            adicionarAlertaNenhumResultadoDisponível(R.id.fragment_musicas);
        }
    }

    private void atualizarRecyclerView(List<Musica> musicas) {
        this.musicas = musicas;
        recyclerView.setAdapter(new MusicaAdapter(getContext(), musicas, onClickMusica()));
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
            PreferencesUtil.putInt(getContext(), MediaPlayerService.AUDIO_INDEX, indexMusicaTocando);
            PreferencesUtil.putList(getContext(), MediaPlayerService.AUDIO_LIST, musicas);

            Intent playerIntent = new Intent(getActivity(), MediaPlayerService.class);
            playerIntent.setAction(MediaPlayerService.ACTION_NEW_MUSIC);
            getActivity().startService(playerIntent);
            getActivity().bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            PreferencesUtil.putInt(getContext(), MediaPlayerService.AUDIO_INDEX, indexMusicaTocando);
            Intent broadcastIntent = new Intent(MediaPlayerService.BROADCAST_PLAY_NEW_AUDIO);
            getActivity().sendBroadcast(broadcastIntent);
        }
    }


    private GerenciadorMusica registrarGerenciadorMusicas() {
        GerenciadorMusica gerenciadorMusica = new GerenciadorMusica();
        IntentFilter intentFilter = new IntentFilter(INTENT_ATUALIZA_MUSICA);
        getActivity().registerReceiver(gerenciadorMusica, intentFilter);
        return gerenciadorMusica;
    }

    @Override
    public void onChangeMusica(Musica musica) {
        Log.i(TAG, musica.getTitulo());
    }

    public class GerenciadorMusica extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}


