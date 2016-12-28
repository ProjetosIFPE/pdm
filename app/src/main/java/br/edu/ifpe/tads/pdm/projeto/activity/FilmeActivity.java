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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.apache.commons.lang.StringUtils;

import java.io.File;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.adapter.FilmePagerAdapter;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.util.Constantes;
import br.edu.ifpe.tads.pdm.projeto.util.FileUtil;
import br.edu.ifpe.tads.pdm.projeto.util.NetworkUtil;

public class FilmeActivity extends BaseActivity {

    private ShareActionProvider shareActionProvider;

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


        TextView filmeTitulo = (TextView) findViewById(R.id.filme_titulo);
        TextView filmeTituloOriginal = (TextView) findViewById(R.id.filme_titulo_original);

        filmeTitulo.setText(filme.getTitulo());
        filmeTituloOriginal.setText(filme.getTituloOriginal());

        carregarImagensFilme(filme);

    }

    public void carregarImagensFilme(Filme filme) {
        ImageView filmePlanoFundo = (ImageView) findViewById(R.id.filme_plano_fundo);
        ImageView filmePoster = (ImageView) findViewById(R.id.filme_poster);

        Boolean arquivo = !NetworkUtil.isConnected(this);
        String urlPlanoFundo;
        File arquivoPlanoFundo;
        String urlPoster = "";
        File arquivoPoster = null;
        if (arquivo) {
            arquivoPoster = FileUtil.getArquivoImagem(getContext(), filme.getUrlPoster(arquivo));
            if (arquivoPoster != null) {
                Picasso.with(getContext()).load(arquivoPoster).fit().into(filmePoster);

            }
            arquivoPlanoFundo = FileUtil.getArquivoImagem(getContext(), filme.getUrlPlanoFundo(arquivo));
            if (arquivoPlanoFundo != null) {
                Picasso.with(getContext()).load(arquivoPlanoFundo).fit().into(filmePlanoFundo);

            }

        } else {
            urlPoster = filme.getUrlPoster(arquivo);
            if (!TextUtils.isEmpty(urlPoster)) {
                Picasso.with(getContext()).load(urlPoster).fit().into(filmePoster);

            }
            urlPlanoFundo = filme.getUrlPlanoFundo(arquivo);
            if (!TextUtils.isEmpty(urlPlanoFundo)) {
                Picasso.with(getContext()).load(urlPlanoFundo).fit().into(filmePlanoFundo);

            }
        }
    }

    public void compartilharFilme(Filme filme) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_intent_text, filme.getTitulo()));
        setShareIntent(shareIntent);
    }


    private void setShareActionProvider(Menu menu) {

        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
    }

    private void setShareIntent(Intent shareIntent) {
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(shareIntent);
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
