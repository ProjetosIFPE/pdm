package br.edu.ifpe.tads.pdm.projeto.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.squareup.otto.Subscribe;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.activity.FilmeActivity;
import br.edu.ifpe.tads.pdm.projeto.adapter.FilmeAdapter;
import br.edu.ifpe.tads.pdm.projeto.application.ApplicationService;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Categoria;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeDB;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeService;
import br.edu.ifpe.tads.pdm.projeto.util.Constantes;
import br.edu.ifpe.tads.pdm.projeto.util.NetworkUtil;
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;

public class FilmesFragment extends BaseFragment implements AlertConnectivityFragment.AlertConnectivityListener {

    public static final String FILMES_POR_TITULO = "FILMES_POR_TITULO";

    public static final String FILMES_POR_CATEGORIA = "FILMES_POR_CATEGORIA";

    public static final String FILMES_POR_DATA_LANCAMENTO = "FILMES_POR_DATA_LANCAMENTO";

    public static final String FILMES_POR_POPULARIDADE = "FILMES_POR_POPULARIDADE";

    public static final String FILMES_FAVORITOS = "FILMES_FAVORITOS";

    protected RecyclerView recyclerView;

    protected ProgressBar progressRecyclerView;

    private String titulo = "";

    private String descricaoCategoria = "";

    private Boolean popularidade = Boolean.FALSE;

    private Boolean lancamento = Boolean.FALSE;

    private Boolean favoritos = Boolean.FALSE;

    private List<Filme> filmes;

    private FilmeService filmeService;

    public static FilmesFragment newInstance(Bundle bundle) {
        FilmesFragment filmesFragment = new FilmesFragment();
        filmesFragment.setArguments(bundle);
        return filmesFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationService.getInstance().getBus().register(this);
        filmeService = ApplicationService.getInstance().getFilmeService();

        Bundle arguments = getArguments();
        if (arguments != null) {
            titulo = arguments.getString(FILMES_POR_TITULO, "");
            descricaoCategoria = arguments.getString(FILMES_POR_CATEGORIA, "");
            popularidade = arguments.getBoolean(FILMES_POR_POPULARIDADE, Boolean.FALSE);
            lancamento = arguments.getBoolean(FILMES_POR_DATA_LANCAMENTO, Boolean.FALSE);
            favoritos = arguments.getBoolean(FILMES_FAVORITOS, Boolean.FALSE);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filmes, container, Boolean.FALSE);

        configurarListaFilmes(view);

        if (NetworkUtil.isConnected(getContext())) {
            esconderListaFilmes();
            consultarFilmes();
        } else {
            adicionarAlertaConexaoIndisponivel();
        }

        return view;
    }


    /**
     * Realiza as configurações de layout do Recycler View e
     * do progress bar
     *
     * @param fragmentView
     */
    private void configurarListaFilmes(View fragmentView) {
        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recyclerView);
        progressRecyclerView = (ProgressBar) fragmentView.findViewById(R.id.progressRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(Boolean.TRUE);
    }

    public void consultarFilmes() {
        if (popularidade) {
            consultarFilmesPopulares();
        } else if (lancamento) {
            consultarLancamentos();
        } else if (!descricaoCategoria.isEmpty()) {
            consultarFilmesPorCategoria(descricaoCategoria);
        } else if (!titulo.isEmpty()) {
            consultarFilmes(titulo);
        } else if (favoritos) {
            consultarFilmesFavoritos();
        }
    }

    /**
     * Adiciona o fragmento com um alerta de conexão indisponível
     */
    public void adicionarAlertaConexaoIndisponivel() {
        AlertConnectivityFragment alertConnectivityFragment = AlertConnectivityFragment.newInstance(this);
        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment_filmes, alertConnectivityFragment,
                        AlertConnectivityFragment.ALERT_CONNECTIVITY_FRAGMENT).commit();
    }

    /**
     * Inicia a tarefa assíncrona que realiza a pesquisa de filmes por ordem de lancamento
     **/
    public void consultarLancamentos() {
        startTask(getFilmesLancadosRecentemente());
    }

    /**
     * Realiza a pesquisa de filmes por data de lancamento
     **/
    private TaskListener<List<Filme>> getFilmesLancadosRecentemente() {
        return new TaskListener<List<Filme>>() {
            @Override
            public List<Filme> execute() throws Exception {
                return filmeService.getLancamentos(getContext());
            }

            @Override
            public void updateView(List<Filme> filmes) {
                atualizarRecyclerView(filmes);
            }

        };
    }

    /**
     * Inicia a tarefa assíncrona que realiza a pesquisa de filmes por popularidade
     **/
    public void consultarFilmesPopulares() {
        startTask(getFilmesPorPopularidade());
    }

    /**
     * Realiza a pesquisa de filmes por título atualizando a Recycler View
     **/
    private TaskListener<List<Filme>> getFilmesPorPopularidade() {
        return new TaskListener<List<Filme>>() {
            @Override
            public List<Filme> execute() throws Exception {
                return filmeService.getPopulares(getContext());
            }

            @Override
            public void updateView(List<Filme> filmes) {
                atualizarRecyclerView(filmes);
            }

        };
    }


    /**
     * Inicia a tarefa assíncrona que realiza a pesquisa de filmes por titulo
     **/
    public void consultarFilmes(String titulo) {
        startTask(getFilmes(titulo));
    }

    /**
     * Realiza a pesquisa de filmes por titulo
     **/
    private TaskListener<List<Filme>> getFilmes(final String titulo) {
        return new TaskListener<List<Filme>>() {
            @Override
            public List<Filme> execute() throws Exception {
                return filmeService.getFilmes(getContext(), titulo);
            }

            @Override
            public void updateView(List<Filme> filmes) {
                atualizarRecyclerView(filmes);
            }

        };
    }

    /**
     * Inicia a tarefa assíncrona que realiza a pesquisa de filmes por categoria
     **/
    public void consultarFilmesPorCategoria(String categoria) {
        startTask(getFilmesPorCategoria(categoria));
    }

    /**
     * Realiza a pesquisa de filmes por categoria
     **/
    private TaskListener<List<Filme>> getFilmesPorCategoria(final String descricaoCategoria) {
        return new TaskListener<List<Filme>>() {
            @Override
            public List<Filme> execute() throws Exception {
                Categoria categoria = filmeService.getCategoria(getContext(), descricaoCategoria);
                return filmeService.getFilmes(getContext(), categoria);
            }

            @Override
            public void updateView(List<Filme> filmes) {
                atualizarRecyclerView(filmes);
            }

        };
    }

    /**
     * Inicia a tarefa assíncrona que realiza a pesquisa de filmes favoritos
     **/
    public void consultarFilmesFavoritos() {
        startTask(getFilmesFavoritos());
    }

    /**
     * Realiza a pesquisa de filmes por categoria
     **/
    private TaskListener<List<Filme>> getFilmesFavoritos() {
        return new TaskListener<List<Filme>>() {
            @Override
            public List<Filme> execute() throws Exception {
                FilmeDB filmeDB = new FilmeDB(getContext());
                return filmeDB.getFavoritos();
            }

            @Override
            public void updateView(List<Filme> filmes) {
                atualizarRecyclerView(filmes);
            }

        };
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
     * Atualiza a lista de filmes do Recycler View
     *
     * @param novosFilmes
     */
    private void atualizarDadosRecyclerView(List<Filme> novosFilmes) {
        if (this.filmes != null && recyclerView != null) {
            filmes.clear();
            filmes.addAll(novosFilmes);
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
        ApplicationService.getInstance().getBus().unregister(this);
    }

    @Subscribe
    public void atualizarListaFilmes(String refresh) {
        consultarFilmes();
    }


    @Override
    public void onConnectivityChange() {
        if (NetworkUtil.isConnected(getContext())) {
            removerAlertaConexaoIndisponivel();
            consultarFilmes();
        }
    }

    /**
     * Remover fragmento de alerta de conexão indisponível
     */
    public void removerAlertaConexaoIndisponivel() {
        Fragment fragment = getChildFragmentManager().findFragmentByTag(
                AlertConnectivityFragment.ALERT_CONNECTIVITY_FRAGMENT);
        getChildFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .remove(fragment).commit();
    }

}
