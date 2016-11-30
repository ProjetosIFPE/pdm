package br.edu.ifpe.tads.pdm.projeto.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.apache.commons.lang.StringUtils;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.adapter.FilmePagerAdapter;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.util.Constantes;

public class FilmeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filme);

        Toolbar toolbar = setUpToolbar();
        toolbar.setNavigationOnClickListener(onToolbarNavigationClick());
        getSupportActionBar().setDisplayHomeAsUpEnabled(Boolean.TRUE);

        final Filme filme = (Filme) getIntent().getSerializableExtra(Constantes.FILME);

        carregarDetalhesFilme(filme);

        Bundle arguments = new Bundle();
        arguments.putSerializable(Constantes.FILME, filme);
        FilmePagerAdapter filmePagerAdapter = new FilmePagerAdapter(getContext(),
                getSupportFragmentManager(), arguments);

        setUpViewPagerTabs(filmePagerAdapter);
    }

    public void carregarDetalhesFilme(Filme filme) {

        ImageView filmePlanoFundo = (ImageView) findViewById(R.id.filme_plano_fundo);
        ImageView filmePoster = (ImageView) findViewById(R.id.filme_poster);
        TextView filmeTitulo = (TextView) findViewById(R.id.filme_titulo);
        TextView filmeTituloOriginal = (TextView) findViewById(R.id.filme_titulo_original);

        filmeTitulo.setText(filme.getTitulo());
        filmeTituloOriginal.setText(filme.getTituloOriginal());

        if (StringUtils.isNotEmpty(filme.getUrlPlanoFundo())) {
            Picasso.with(getContext()).load(filme.getUrlPlanoFundo()).fit().into(filmePlanoFundo);
        }

        if (StringUtils.isNotEmpty(filme.getUrlPoster())) {
            Picasso.with(getContext()).load(filme.getUrlPoster()).fit().into(filmePoster);
        }
    }


    public View.OnClickListener onToolbarNavigationClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };
    }

}
