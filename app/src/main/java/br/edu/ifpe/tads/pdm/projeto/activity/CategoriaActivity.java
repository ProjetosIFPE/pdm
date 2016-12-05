package br.edu.ifpe.tads.pdm.projeto.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import br.edu.ifpe.tads.pdm.projeto.R;
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
        String categoria = getIntent()
                .getStringExtra(CATEGORIA_FILME);

        getSupportActionBar().setTitle(categoria);

        if (firstSearch) {
            realizarPrimeiraPesquisaPorCategoria(categoria);
        } else {
            realizarNovaPesquisaPorCategoria(categoria);
        }
    }

    public void realizarPrimeiraPesquisaPorCategoria(String categoria) {
        Bundle arguments = new Bundle();
        arguments.putString(FilmesFragment.FILMES_POR_CATEGORIA, categoria);
        adicionarFilmesFragment(arguments);
    }

    public void realizarNovaPesquisaPorCategoria(String categoria) {
        FilmesFragment filmesFragment = getFilmesFragment();
        if (filmesFragment != null) {
            filmesFragment.reiniciarListaFilmes();
            filmesFragment.consultarFilmesPorCategoria(categoria);
        }
    }

    public FilmesFragment getFilmesFragment() {
        return (FilmesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_filmes);
    }

    public void adicionarFilmesFragment(Bundle arguments) {
        FilmesFragment filmesFragment = FilmesFragment.newInstance(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_filmes, filmesFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.createSearchWidget(menu);
        return Boolean.TRUE;
    }
}
