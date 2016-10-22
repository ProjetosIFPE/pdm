package br.edu.ifpe.tads.pdm.projeto.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.activity.FilmeActivity;
import br.edu.ifpe.tads.pdm.projeto.adapter.FilmeAdapter;
import br.edu.ifpe.tads.pdm.projeto.domain.Filme;
import br.edu.ifpe.tads.pdm.projeto.domain.FilmeService;
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;


public class FilmesFragment extends BaseFragment {

    protected RecyclerView recyclerView;

    protected ProgressBar progressRecyclerView;

    public static final String FILMES_POR_TITULO = "FILMES_POR_TITULO";

    private List<Filme> filmes;

    private String titulo;

    private FilmeService filmeService;

    public  static FilmesFragment newInstance(Bundle bundle) {
        FilmesFragment filmesFragment = new FilmesFragment();
        filmesFragment.setArguments(bundle);
        return filmesFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filmeService = new FilmeService();
        if ( getArguments() != null ) {
            titulo = getArguments().getString(FILMES_POR_TITULO);
        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filmes, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressRecyclerView = (ProgressBar) view.findViewById(R.id.progressRecyclerView);
        recyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(Boolean.TRUE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listarFilmes(this.titulo);
    }

    /**
     * Inicia a tarefa assíncrona que realiza a pesquisa de filmes
     * **/
    public void listarFilmes(String titulo) {
        startTask(getFilmes(titulo));
    }


    /**
     * Realiza a pesquisa de filmes por título atualizando a Recycler View
     * **/
    public TaskListener<List<Filme>> getFilmes(final String titulo) {
        return new TaskListener<List<Filme>>() {
            @Override
            public List<Filme> execute() throws Exception {
                return filmeService.getFilmes(getContext(), titulo);
            }

            @Override
            public void updateView(List<Filme> response) {
                if ( recyclerView.getAdapter() == null ) {
                    recyclerView.setAdapter(new FilmeAdapter(getContext(), response, onClickFilme()));
                    filmes = response;
                } else {
                    filmes.clear();
                    filmes.addAll(response);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Exception exception) {

            }
            @Override
            public void onCancelled(String cod) {

            }
        };

    }

    private FilmeAdapter.FilmeOnClickListener onClickFilme() {
        return new FilmeAdapter.FilmeOnClickListener() {
            @Override
            public void onClickFilme(View view, int idx) {
                Filme filme = filmes.get(idx);
                Log.d(TAG, filme.getTitulo());
                Intent intent = new Intent(getContext(), FilmeActivity.class);
                intent.putExtra(FilmeActivity.FILME, filme);
                startActivity(intent);

            }
        };
    }


}
