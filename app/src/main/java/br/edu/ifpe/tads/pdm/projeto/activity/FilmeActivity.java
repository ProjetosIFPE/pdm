package br.edu.ifpe.tads.pdm.projeto.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.parceler.apache.commons.lang.StringUtils;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.fragment.FilmeFragment;

public class FilmeActivity extends BaseActivity {

    public static final String FILME = "filme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filme);
        setUpToolbar();
        setUpNavDrawer();

        Filme filme = (Filme) getIntent().getSerializableExtra(FilmeActivity.FILME);

        ImageView appBarImg = (ImageView) findViewById(R.id.appBarImg);
        if (StringUtils.isNotEmpty(filme.getUrlPoster())) {
            Picasso.with(getContext()).load(filme.getUrlPoster()).fit().into(appBarImg);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(FilmeActivity.FILME, filme);
            FilmeFragment filmeFragment = FilmeFragment.newInstance(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.filme_fragment, filmeFragment).commit();
        }

    }
}
