package br.edu.ifpe.tads.pdm.projeto.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.activity.FilmeActivity;
import br.edu.ifpe.tads.pdm.projeto.adapter.FilmeAdapter;
import br.edu.ifpe.tads.pdm.projeto.application.ApplicationService;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeManager;
import br.edu.ifpe.tads.pdm.projeto.listener.EndlessRecyclerViewScrollListener;
import br.edu.ifpe.tads.pdm.projeto.util.Constantes;
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;


public class FavoritosFragment extends BaseFragment {

    public static final int ADICIONAR_FAVORITO = 0;

    public static final int REMOVER_FAVORITO = 1;

    public static final String INTENT_ATUALIZAR_FILME = "br.edu.ifpe.tads.pdm.projeto.INTENT_ATUALIZAR_FILME";

    public static final String FILME_ATUALIZADO = "ATUALIZAR_FILME";

    public static final String ACAO_FILME_ATUALIZADO = "ACAO_FILME_ATUALIZADO";

    protected RecyclerView recyclerView;

    protected ProgressBar progressRecyclerView;

    private List<Filme> filmes;

    private FilmeManager filmeManager;

    private GerenciadorFavoritos gerenciadorFavoritos;

    private Boolean atualizarFavoritos = Boolean.FALSE;


    public static FavoritosFragment newInstance(Bundle bundle) {
        FavoritosFragment favoritosFragment = new FavoritosFragment();
        favoritosFragment.setArguments(bundle);
        return favoritosFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filmeManager = ApplicationService.getInstance().getFilmeManager();
        gerenciadorFavoritos = registrarGerenciadorFavoritos();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos, container, Boolean.FALSE);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressRecyclerView = (ProgressBar) view.findViewById(R.id.progressRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(Boolean.TRUE);

        esconderListaFilmes();
        consultarFilmesFavoritos();

        return view;
    }


    /**
     * Inicia a tarefa assíncrona que realiza a pesquisa de filmes favoritos
     **/
    private void consultarFilmesFavoritos() {
        startTask(getFilmesFavoritos());
    }

    /**
     * Realiza a pesquisa de filmes por categoria
     **/
    private TaskListener<List<Filme>> getFilmesFavoritos() {
        return new TaskListener<List<Filme>>() {
            @Override
            public List<Filme> execute() throws Exception {
                return filmeManager.getFavoritos();
            }

            @Override
            public void updateView(List<Filme> filmes) {
                verificarResultadoConsultaFilmes(filmes);
            }

        };
    }

    private void verificarResultadoConsultaFilmes(List<Filme> filmes) {
        if (filmes != null && !filmes.isEmpty()) {
            removerAlertaNenhumResultado();
            atualizarRecyclerView(filmes);
        } else {
            esconderResultados();
            adicionarAlertaNenhumResultadoDisponível(R.id.fragment_favoritos);
        }
    }

    private void esconderResultados() {
        if (recyclerView != null && progressRecyclerView != null) {
            recyclerView.setVisibility(View.GONE);
            progressRecyclerView.setVisibility(View.GONE);
        }
    }

    /**
     * Cria o adapter da recycler view, se está não possuir um, ou
     * atualiza o conteúdo do adapter existente
     *
     * @param filmes
     */
    private void atualizarRecyclerView(List<Filme> filmes) {
        mostrarListaFilmes();
        if (recyclerView != null && recyclerView.getAdapter() == null) {
            inicializarDadosRecyclerView(filmes);
        } else {
            atualizarDadosRecyclerView(filmes);
        }
    }

    /**
     * Atualiza a lista de filmes do Recycler View com uma nova
     * lista de filmes
     *
     * @param novosFilmes
     */
    private void atualizarDadosRecyclerView(List<Filme> novosFilmes) {
        if (this.filmes != null && recyclerView != null) {
            if (!this.filmes.containsAll(novosFilmes)) {
                filmes.addAll(novosFilmes);
            }
            notificarAtualizacoesAdapter();
        }
    }



    /**
     * Notifica o adapter das alterações na lista
     * de filmes
     */
    private void notificarAtualizacoesAdapter() {
        if (recyclerView != null) {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Criar adapter e inicializar lista de filmes no Recycler View
     *
     * @param novosFilmes
     */
    private void inicializarDadosRecyclerView(List<Filme> novosFilmes) {
        if (recyclerView != null && recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(new FilmeAdapter(getContext(), novosFilmes, onClickFilme()));
            this.filmes = novosFilmes;
        }
    }

    /**
     * Altera as propriedades de visibilidade
     * para visível do Recycler View e esconde o Progress bar
     */
    private void mostrarListaFilmes() {
        if (recyclerView != null && progressRecyclerView != null) {
            progressRecyclerView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Altera as propriedades de visibilidade
     * para invísivel, do Recycler View, e mostra
     * o Progress bar
     */
    private void esconderListaFilmes() {
        if (recyclerView != null && progressRecyclerView != null) {
            recyclerView.setVisibility(View.GONE);
            progressRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Controla evento de clique em itens da lista de filmes
     **/
    private FilmeAdapter.FilmeOnClickListener onClickFilme() {
        return new FilmeAdapter.FilmeOnClickListener() {
            @Override
            public void onClickFilme(View view, int idx) {
                Filme filme = filmes.get(idx);
                Log.d(TAG, filme.getTitulo());
                Intent intent = new Intent(getContext(), FilmeActivity.class);
                intent.putExtra(Constantes.FILME, filme);
                startActivity(intent);

            }
        };
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(gerenciadorFavoritos);
    }

    @Override
    public void onResume() {
        super.onResume();
        atualizarListaFilmesFavoritos();
    }


    private void atualizarListaFilmesFavoritos() {
        if (atualizarFavoritos) {
            verificarResultadoConsultaFilmes(this.filmes);
            atualizarFavoritos = Boolean.FALSE;
        }
    }

    private GerenciadorFavoritos registrarGerenciadorFavoritos() {
        GerenciadorFavoritos gerenciadorFavoritos = new GerenciadorFavoritos();
        IntentFilter intentFilter = new IntentFilter(INTENT_ATUALIZAR_FILME);
        getActivity().registerReceiver(gerenciadorFavoritos, intentFilter);
        return gerenciadorFavoritos;
    }

    public class GerenciadorFavoritos extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Filme filme = (Filme) intent.getSerializableExtra(FILME_ATUALIZADO);
            Integer acao = intent.getIntExtra(ACAO_FILME_ATUALIZADO, -1);
            escolherAcaoFilme(filme, acao);

        }

        public void escolherAcaoFilme(Filme filme, int acao) {
            if (filmes == null) {
                filmes = new ArrayList<>();
            }
            switch (acao) {
                case ADICIONAR_FAVORITO:
                    filmes.add(filme);
                    break;
                case REMOVER_FAVORITO:
                    filmes.remove(filme);
                    break;
            }
            atualizarFavoritos = Boolean.TRUE;
        }
    }

}
