package br.edu.ifpe.tads.pdm.projeto.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.activity.CategoriaActivity;
import br.edu.ifpe.tads.pdm.projeto.activity.FilmeActivity;
import br.edu.ifpe.tads.pdm.projeto.adapter.CategoriaAdapter;
import br.edu.ifpe.tads.pdm.projeto.adapter.FilmeAdapter;
import br.edu.ifpe.tads.pdm.projeto.application.ApplicationService;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Categoria;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeDB;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeService;
import br.edu.ifpe.tads.pdm.projeto.util.Constantes;
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;


public class FilmeFragment extends BaseFragment {

    public static final int SPAN_COUNT = 1;
    private Filme filme;

    private FilmeService filmeService;

    private FloatingActionButton fab;

    public static FilmeFragment newInstance(Bundle args) {
        FilmeFragment filmeFragment = new FilmeFragment();
        filmeFragment.setArguments(args);
        return filmeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filmeService = ApplicationService.getInstance().getFilmeService();
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.filme = (Filme) arguments.getSerializable(Constantes.FILME);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {

        View view = inflater.inflate(R.layout.fragment_filme, container, false);
        TextView filmeDescricao = (TextView) view.findViewById(R.id.filme_descricao);

        RecyclerView listaCategorias = (RecyclerView) view.findViewById(R.id.lista_categorias);
        listaCategorias.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT, GridLayoutManager.HORIZONTAL, Boolean.FALSE));
        listaCategorias.setItemAnimator(new DefaultItemAnimator());
        listaCategorias.setHasFixedSize(Boolean.TRUE);

        listaCategorias.setAdapter(new CategoriaAdapter(getContext(), filme.getCategorias(), onClickCategoria()));

        filmeDescricao.setText(filme.getSinopse());

        getFavoriteButton();
        checkFavorito();

        return view;
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


    public void checkFavorito() {
        startTask(getTaskCheckFavorito());
    }


    private TaskListener<Boolean> getTaskCheckFavorito() {
        return new TaskListener<Boolean>() {
            @Override
            public Boolean execute() throws Exception {
                FilmeDB filmeDB = new FilmeDB(getContext());
                return filmeDB.existe(filme);
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
                FilmeDB filmeDB = new FilmeDB(getContext());
                Boolean existe = filmeDB.existe(filme);
                if (!existe) {
                    filmeDB.salvar(filme);
                } else {
                    filmeDB.remover(filme);
                }
                return !existe;
            }

            @Override
            public void updateView(Boolean favoritou) {
                if (favoritou) {
                    toast("Filme adicionado aos favoritos");
                } else {
                    toast("Filme removido dos favoritos");
                }

                ApplicationService.getInstance().getBus().post(Constantes.ATUALIZAR_LISTA);

                setFabColor(favoritou);
            }
        };
    }

    public void setFabColor(Boolean favorito) {
        if (favorito) {
            fab.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.yellow));
        } else {
            fab.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.accent));
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
}
