package br.edu.ifpe.tads.pdm.projeto.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.parceler.apache.commons.lang.StringUtils;

import java.io.File;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.adapter.FilmeAdapter;
import br.edu.ifpe.tads.pdm.projeto.adapter.FilmePagerAdapter;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.util.Constantes;
import br.edu.ifpe.tads.pdm.projeto.util.FileUtil;
import br.edu.ifpe.tads.pdm.projeto.util.NetworkUtil;

public class FilmeActivity extends BaseActivity {


    protected ProgressBar progressBarImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filme);

        Toolbar toolbar = setUpToolbar();
        toolbar.setNavigationOnClickListener(onToolbarNavigationClick());
        getSupportActionBar().setDisplayHomeAsUpEnabled(Boolean.TRUE);

        final Filme filme = (Filme) getIntent().getSerializableExtra(Constantes.FILME);
        progressBarImg = (ProgressBar) findViewById(R.id.progressRecyclerViewPlanoFundo);
        progressBarImg.setVisibility(View.VISIBLE);

        carregarDetalhesFilme(filme);

        Bundle arguments = new Bundle();
        arguments.putSerializable(Constantes.FILME, filme);
        FilmePagerAdapter filmePagerAdapter = new FilmePagerAdapter(getContext(),
                getSupportFragmentManager(), arguments);

        setUpViewPagerTabs(filmePagerAdapter);

    }

    public void carregarDetalhesFilme(Filme filme) {

        TextView filmeTitulo = (TextView) findViewById(R.id.filme_titulo);
        TextView filmeTituloOriginal = (TextView) findViewById(R.id.filme_titulo_original);


        filmeTitulo.setText(filme.getTitulo());
        filmeTituloOriginal.setText(filme.getTituloOriginal());

        carregarImagensFilme(filme);

    }

    @Deprecated
    public void carregarImagensFilme(Filme filme) {
        ImageView filmePlanoFundo = (ImageView) findViewById(R.id.filme_plano_fundo);
        ImageView filmePoster = (ImageView) findViewById(R.id.filme_poster);
        Boolean arquivo = !NetworkUtil.isConnected(this);
        String urlPlanoFundo = "";
        File arquivoPlanoFundo = null;
        String urlPoster = "";
        File arquivoPoster = null;
        if (arquivo) {
            arquivoPoster = FileUtil.getArquivoImagem(getContext(), filme.getUrlPoster(arquivo));
            if (arquivoPoster != null) {
                Picasso.with(getContext()).load(arquivoPoster).fit().into(filmePoster);
            }
            arquivoPlanoFundo = FileUtil.getArquivoImagem(getContext(), filme.getUrlPlanoFundo(arquivo));
            if (arquivoPlanoFundo != null) {
                Picasso.with(getContext()).load(arquivoPlanoFundo).fit().into(filmePlanoFundo, getImageLoadCallback());
            }

        } else {
            urlPoster = filme.getUrlPoster(arquivo);
            if (!TextUtils.isEmpty(urlPoster)) {
                Picasso.with(getContext()).load(urlPoster).fit().into(filmePoster);

            }
            urlPlanoFundo = filme.getUrlPlanoFundo(arquivo);
            if (!TextUtils.isEmpty(urlPlanoFundo)) {
                Picasso.with(getContext()).load(urlPlanoFundo).fit().into(filmePlanoFundo, getImageLoadCallback());
            }
        }

        if (StringUtils.isEmpty(urlPlanoFundo) && arquivoPlanoFundo == null) {
            Picasso.with(getContext()).cancelRequest(filmePlanoFundo);
            Picasso.with(getContext()).load(R.drawable.placeholder).fit().into(filmePlanoFundo);
            mostrarPlanoFundo();
        }

        if (StringUtils.isEmpty(urlPoster) && arquivoPoster == null) {
            Picasso.with(getContext()).cancelRequest(filmePoster);
            Picasso.with(getContext()).load(R.drawable.placeholder).fit().into(filmePoster);
        }
    }

    public Callback getImageLoadCallback() {
        return new Callback() {
            @Override
            public void onSuccess() {
                mostrarPlanoFundo();

            }

            @Override
            public void onError() {
                mostrarPlanoFundo();
            }
        };
    }

    public void mostrarPlanoFundo() {
        progressBarImg.setVisibility(View.GONE);

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
