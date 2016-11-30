package br.edu.ifpe.tads.pdm.projeto.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Categoria;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.fragment.FilmesFragment;

public class CategoriaActivity extends BaseActivity {

    public static final String CATEGORIA_FILME = "CATEGORIA_FILME";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        doSearch(Boolean.FALSE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        setUpToolbar();
        setUpNavDrawer();

        if (savedInstanceState == null) {
            doSearch(Boolean.TRUE);
        }
    }

    public void doSearch(Boolean firstSearch) {
        String categoria =  getIntent()
                .getStringExtra(CATEGORIA_FILME);

        getSupportActionBar().setTitle(categoria);

        if ( firstSearch ) {
            Bundle arguments = new Bundle();
            arguments.putString(FilmesFragment.FILMES_POR_CATEGORIA, categoria);
            FilmesFragment filmesFragment = FilmesFragment.newInstance(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_filmes, filmesFragment).commit();
        } else {
            FilmesFragment filmesFragment = (FilmesFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_filmes);
            filmesFragment.consultarFilmesPorCategoria(categoria);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.createSearchWidget(menu);
        return Boolean.TRUE;
    }
}
