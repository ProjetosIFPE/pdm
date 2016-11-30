package br.edu.ifpe.tads.pdm.projeto.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;

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
        setUpNavDrawer();

        if ( savedInstanceState == null ) {
            doSearch(Boolean.TRUE);
        }

    }

    private void doSearch(Boolean firstSearch) {

        String titulo = "";
        Intent intent = getIntent();

        if ( Intent.ACTION_SEARCH.equals(intent.getAction())) {
            titulo = intent.getStringExtra(SearchManager.QUERY);
        }

        if ( firstSearch ) {
            Bundle arguments = new Bundle();
            arguments.putString(FilmesFragment.FILMES_POR_TITULO, titulo);
            FilmesFragment filmesFragment = FilmesFragment.newInstance(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.filmes_fragment, filmesFragment).commit();
        } else {
            FilmesFragment filmesFragment = (FilmesFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.filmes_fragment);
            filmesFragment.consultarFilmes(titulo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.createSearchWidget(menu);
        return Boolean.TRUE;
    }


}
