package br.edu.ifpe.tads.pdm.projeto.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.parceler.apache.commons.lang.StringUtils;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeDB;
import br.edu.ifpe.tads.pdm.projeto.fragment.FilmeFragment;
import br.edu.ifpe.tads.pdm.projeto.fragment.MusicasFragment;

public class FilmeActivity extends BaseActivity {

    public static final String FILME = "filme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filme);
        setUpToolbar();
        setUpNavDrawer();
        final FilmeDB filmeDB = new FilmeDB(this);

        final Filme filme = (Filme) getIntent().getSerializableExtra(FilmeActivity.FILME);
        getSupportActionBar().setTitle(filme.getTitulo());

        ImageView appBarImg = (ImageView) findViewById(R.id.appBarImg);
        if (StringUtils.isNotEmpty(filme.getUrlPlanoFundo())) {
            Picasso.with(getContext()).load(filme.getUrlPlanoFundo()).fit().into(appBarImg);
        }

        if (savedInstanceState == null) {

            Bundle filmeFragmentArgs = new Bundle();
            filmeFragmentArgs.putSerializable(FilmeActivity.FILME, filme);
            FilmeFragment filmeFragment = FilmeFragment.newInstance(filmeFragmentArgs);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_filme, filmeFragment).commit();


            final FloatingActionButton fAbutton = (FloatingActionButton) findViewById(R.id.fab);
            fAbutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //filmeDB.save(filme);
                    Toast.makeText(FilmeActivity.this, "'" + filme.getTitulo() + "' foi salvo.", Toast.LENGTH_SHORT).show();
                }
            });



        }
    }
}
