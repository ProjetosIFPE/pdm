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
import br.edu.ifpe.tads.pdm.projeto.listener.EndlessRecyclerViewScrollListener;
import br.edu.ifpe.tads.pdm.projeto.util.Constantes;
import br.edu.ifpe.tads.pdm.projeto.util.NetworkUtil;
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;

public class FilmesFragment extends BaseConnectivityFragment {

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

    private EndlessRecyclerViewScrollListener scrollListaFilmes;

    private int searchPageIndex = 1;


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

    @Override
    View createFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_filmes, container, Boolean.FALSE);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressRecyclerView = (ProgressBar) view.findViewById(R.id.progressRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        scrollListaFilmes = criarScrollListaFilmes();
        recyclerView.addOnScrollListener(scrollListaFilmes);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(Boolean.TRUE);


        return view;
    }

    @Override
    void startFragmentTask() {
        esconderListaFilmes();
        consultarFilmes();
    }


    /**
     * Cria o listener para o scroll da lista de filmes
     *
     * @return
     */
    private EndlessRecyclerViewScrollListener criarScrollListaFilmes() {
        return new EndlessRecyclerViewScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount, RecyclerView recyclerView) {
                searchPageIndex = page;
                return consultarFilmes();
            }
        };
    }

    public Boolean consultarFilmes() {
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
        return false;
    }



    /**
     * Adiciona o fragmento com um alerta de nenhum resultado disponível
     */
    private void adicionarAlertaNenhumResultadoDisponível() {
        Bundle arguments = new Bundle();
        AlertNoResultsFragment alertNoResultsFragment = AlertNoResultsFragment.newInstance(arguments);
        if (getActivity() != null) {
            getChildFragmentManager().beginTransaction()
                    .add(R.id.fragment_filmes, alertNoResultsFragment,
                            AlertNoResultsFragment.ALERT_NO_RESULTS_FRAGMENT).commit();
        }
    }

    /**
     * Inicia a tarefa assíncrona que realiza a pesquisa de filmes por ordem de lancamento
     **/
    private void consultarLancamentos() {
        startTask(getFilmesLancadosRecentemente());
    }

    /**
     * Realiza a pesquisa de filmes por data de lancamento
     **/
    private TaskListener<List<Filme>> getFilmesLancadosRecentemente() {
        return new TaskListener<List<Filme>>() {
            @Override
            public List<Filme> execute() throws Exception {
                return filmeService.getLancamentos(getContext(), searchPageIndex);
            }

            @Override
            public void updateView(List<Filme> filmes) {
                verificarResultadoConsultaFilmes(filmes);
            }

        };
    }

    /**
     * Inicia a tarefa assíncrona que realiza a pesquisa de filmes por popularidade
     **/
    private void consultarFilmesPopulares() {
        startTask(getFilmesPorPopularidade());
    }

    /**
     * Realiza a pesquisa de filmes por título atualizando a Recycler View
     **/
    private TaskListener<List<Filme>> getFilmesPorPopularidade() {
        return new TaskListener<List<Filme>>() {
            @Override
            public List<Filme> execute() throws Exception {
                return filmeService.getPopulares(getContext(), searchPageIndex);
            }

            @Override
            public void updateView(List<Filme> filmes) {
                verificarResultadoConsultaFilmes(filmes);
            }

        };
    }


    /**
     * Inicia a tarefa assíncrona que realiza a pesquisa de filmes por titulo
     **/
    public void consultarFilmes(String titulo) {
        this.titulo = titulo;
        startTask(getFilmes(this.titulo));
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
                verificarResultadoConsultaFilmes(filmes);
            }

        };
    }

    /**
     * Inicia a tarefa assíncrona que realiza a pesquisa de filmes por categoria
     **/
    public void consultarFilmesPorCategoria(String categoria) {
        this.descricaoCategoria = categoria;
        startTask(getFilmesPorCategoria(this.descricaoCategoria));
    }

    /**
     * Realiza a pesquisa de filmes por categoria
     **/
    private TaskListener<List<Filme>> getFilmesPorCategoria(final String descricaoCategoria) {
        return new TaskListener<List<Filme>>() {
            @Override
            public List<Filme> execute() throws Exception {
                Categoria categoria = filmeService.getCategoria(getContext(), descricaoCategoria);
                return filmeService.getFilmes(getContext(), categoria, searchPageIndex);
            }

            @Override
            public void updateView(List<Filme> filmes) {
                verificarResultadoConsultaFilmes(filmes);
            }

        };
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
                FilmeDB filmeDB = new FilmeDB(getContext());
                return filmeDB.getFavoritos();
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
            adicionarAlertaNenhumResultadoDisponível();
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
     * Atualiza a lista de filmes do Recycler View
     *
     * @param novosFilmes
     */
    private void atualizarDadosRecyclerView(List<Filme> novosFilmes) {
        if (this.filmes != null && recyclerView != null) {
            filmes.addAll(novosFilmes);
            notificarAtualizacoesAdapter();
        }
    }

    public void reiniciarListaFilmes() {
        if (filmes != null && scrollListaFilmes != null) {
            filmes.clear();
            notificarAtualizacoesAdapter();
            scrollListaFilmes.resetState();
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
        ApplicationService.getInstance().getBus().unregister(this);
    }

    @Subscribe
    public void atualizarListaFilmes(String refresh) {
        consultarFilmes();
    }



    /**
     * Remover fragmento de alerta de nenhum resultado disponível
     */
    private void removerAlertaNenhumResultado() {
        Fragment fragment = getChildFragmentManager().findFragmentByTag(
                AlertNoResultsFragment.ALERT_NO_RESULTS_FRAGMENT);
        if (fragment != null) {
            getChildFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .remove(fragment).commit();
        }
    }

}
