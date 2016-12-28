package br.edu.ifpe.tads.pdm.projeto.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.DatabaseMetaData;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.activity.CategoriaActivity;
import br.edu.ifpe.tads.pdm.projeto.adapter.CategoriaAdapter;
import br.edu.ifpe.tads.pdm.projeto.adapter.VideoAdapter;
import br.edu.ifpe.tads.pdm.projeto.application.ApplicationService;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Categoria;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.DaoSession;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeDao;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeManager;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeService;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Video;
import br.edu.ifpe.tads.pdm.projeto.util.Constantes;
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;


public class FilmeFragment extends BaseFragment {

    private Filme filme;

    private FilmeManager filmeManager;

    private RecyclerView listaVideosFilme;

    private RecyclerView listaCategoriasFilme;

    private FloatingActionButton fab;

    public static FilmeFragment newInstance(Bundle args) {
        FilmeFragment filmeFragment = new FilmeFragment();
        filmeFragment.setArguments(args);
        return filmeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filmeManager = ApplicationService.getInstance().getFilmeManager();
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.filme = (Filme) arguments.getSerializable(Constantes.FILME);
            detalharFilme();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {

        View view = inflater.inflate(R.layout.fragment_filme, container, false);

        TextView filmeDescricao = (TextView) view.findViewById(R.id.filme_descricao);
        TextView filmeClassificacao = (TextView) view.findViewById(R.id.filme_classificacao);
        filmeDescricao.setText(filme.getSinopse());
        filmeClassificacao.setText(String.valueOf(filme.getClassificacao()));

        criarListaCategorias(view);
        criarListaVideos(view);

        getFavoriteButton();
        checkFavorito();

        return view;
    }

    public void criarListaCategorias(View fragmentView) {
        this.listaCategoriasFilme = (RecyclerView) fragmentView.findViewById(R.id.lista_categorias);
        listaCategoriasFilme.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        listaCategoriasFilme.setItemAnimator(new DefaultItemAnimator());
        listaCategoriasFilme.setHasFixedSize(Boolean.TRUE);
        inicializarListaCategorias();
    }

    public void inicializarListaCategorias() {

        if (listaCategoriasFilme != null) {
            listaCategoriasFilme.setAdapter(new CategoriaAdapter(getContext(),
                    filme.getCategorias(), onClickCategoria()));
        }
    }

    public void criarListaVideos(View fragmentView) {
        this.listaVideosFilme = (RecyclerView) fragmentView.findViewById(R.id.lista_videos);
        listaVideosFilme.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        listaVideosFilme.setItemAnimator(new DefaultItemAnimator());
        listaVideosFilme.setHasFixedSize(Boolean.TRUE);
    }

    public void verificarResultadoConsultaDetalhesFilme() {
        inicializarListaVideos();
    }

    public void inicializarListaVideos() {
        if (listaVideosFilme != null) {
            listaVideosFilme.setAdapter(new VideoAdapter(getContext(), filme.getVideos(), onClickVideo()));
        }
    }


    public void getFavoriteButton() {
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(onClickFavoriteButton());
    }


    public View.OnClickListener onClickFavoriteButton() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoritarFilme();
            }
        };
    }

    public void detalharFilme() {
        startTask(getTaskDetalharFilme());
    }


    private TaskListener<Filme> getTaskDetalharFilme() {
        return new TaskListener<Filme>() {
            @Override
            public Filme execute() throws Exception {
                if (filme.getVideos().isEmpty()) {
                    filme = filmeManager.carregarVideosFilme(getContext(), filme);
                }
                return filme;
            }

            @Override
            public void updateView(Filme existe) {
                verificarResultadoConsultaDetalhesFilme();
            }
        };
    }


    public void checkFavorito() {
        startTask(getTaskCheckFavorito());
    }


    private TaskListener<Boolean> getTaskCheckFavorito() {
        return new TaskListener<Boolean>() {
            @Override
            public Boolean execute() throws Exception {
                return filmeManager.existeFilmeFavorito(filme);
            }

            @Override
            public void updateView(Boolean existe) {
                setFabColor(existe);
            }
        };
    }


    public void favoritarFilme() {

        startTask(getTaskFavoritar());
    }

    private TaskListener<Boolean> getTaskFavoritar() {
        return new TaskListener<Boolean>() {
            @Override
            public Boolean execute() throws Exception {
                Boolean existe = filmeManager.existeFilmeFavorito(filme);
                if (!existe) {
                    filmeManager.novoFavorito(getContext(), filme);
                    notificarNovoFavorito(filme);
                } else {
                    filmeManager.removerFavorito(getContext(), filme);
                    notificarFavoritoRemovido(filme);
                }
                return !existe;
            }

            @Override
            public void updateView(Boolean favoritou) {
                if (favoritou != null) {
                    if (favoritou) {
                        toast(getString(R.string.filme_adicionado_em_favoritos));
                    } else {
                        toast(getString(R.string.filme_removido_dos_favoritos));
                    }
                    setFabColor(favoritou);
                }
            }
        };
    }

    public void notificarNovoFavorito(Filme filme) {
        Intent intent = new Intent(FavoritosFragment.INTENT_ATUALIZAR_FILME);
        intent.putExtra(FavoritosFragment.FILME_ATUALIZADO, filme);
        intent.putExtra(FavoritosFragment.ACAO_FILME_ATUALIZADO, FavoritosFragment.ADICIONAR_FAVORITO);
        getActivity().sendBroadcast(intent);
    }

    public void notificarFavoritoRemovido(Filme filme) {
        Intent intent = new Intent(FavoritosFragment.INTENT_ATUALIZAR_FILME);
        intent.putExtra(FavoritosFragment.FILME_ATUALIZADO, filme);
        intent.putExtra(FavoritosFragment.ACAO_FILME_ATUALIZADO, FavoritosFragment.REMOVER_FAVORITO);
        getActivity().sendBroadcast(intent);
    }


    public void setFabColor(Boolean favorito) {
        try {
            if (favorito != null && fab != null) {
                if (favorito) {
                    fab.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.yellow));
                } else {
                    fab.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.accent));
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);
        }


    }

    /**
     * Controla evento de clique em categorias de um filme
     **/
    private CategoriaAdapter.CategoriaOnClickListener onClickCategoria() {
        return new CategoriaAdapter.CategoriaOnClickListener() {
            @Override
            public void onClickCategoria(View view, int idx) {
                Categoria categoria = filme.getCategoria(idx);
                Intent intent = new Intent(getContext(), CategoriaActivity.class);
                intent.putExtra(CategoriaActivity.CATEGORIA_FILME, categoria.getDescricao());
                startActivity(intent);

            }
        };
    }

    /**
     * Controla evento de clique em videos de um filme
     **/
    private VideoAdapter.VideoOnClickListener onClickVideo() {
        return new VideoAdapter.VideoOnClickListener() {
            @Override
            public void onClickVideo(View view, int idx) {

                Video video = filme.getVideo(idx);
                Uri videoUrl = Uri.parse(getString(R.string.youtube_link, video.getChave()));
                Intent intent = new Intent(Intent.ACTION_VIEW, videoUrl);
                startActivity(intent);
            }
        };
    }
}
