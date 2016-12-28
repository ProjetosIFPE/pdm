package br.edu.ifpe.tads.pdm.projeto.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.fragment.FilmesFragment;

/**
 * Created by Edmilson Santana on 30/09/2016.
 */
public class PesquisaActivity extends BaseActivity {


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        doSearch(Boolean.FALSE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);
        setUpToolbar();
        setUpMenuNavegacao(savedInstanceState);

        if (savedInstanceState == null) {
            doSearch(Boolean.TRUE);
        }

    }

    private void doSearch(Boolean firstSearch) {

        String titulo = "";
        Intent intent = getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            titulo = intent.getStringExtra(SearchManager.QUERY);
        }

        if (firstSearch) {
            realizarPrimeiraPesquisaPorTitulo(titulo);
        } else {
            realizarNovaPesquisaPorTitulo(titulo);
        }
    }

    private void realizarPrimeiraPesquisaPorTitulo(String titulo) {
        Bundle arguments = new Bundle();
        arguments.putString(FilmesFragment.FILMES_POR_TITULO, titulo);
        adicionarFilmesFragment(arguments);
    }

    private void realizarNovaPesquisaPorTitulo(String titulo) {
        FilmesFragment filmesFragment = getFilmesFragment();
        if (filmesFragment != null) {
            filmesFragment.reiniciarListaFilmes();
            filmesFragment.consultarFilmes(titulo);
        }
    }

    private void adicionarFilmesFragment(Bundle arguments) {
        FilmesFragment filmesFragment = FilmesFragment.newInstance(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.filmes_fragment, filmesFragment).commit();
    }

    private FilmesFragment getFilmesFragment() {
        return (FilmesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.filmes_fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.createSearchWidget(menu);
        return Boolean.TRUE;
    }


}
