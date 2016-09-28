package br.edu.ifpe.tads.pdm.projeto.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.adapter.FilmeAdapter;
import br.edu.ifpe.tads.pdm.projeto.domain.Filme;
import br.edu.ifpe.tads.pdm.projeto.domain.FilmeService;


public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();

        FilmeService service = new FilmeService();
        List<Filme> filmes = service.getFilmes(getApplicationContext(), "Matrix");

        final Filme[] arrayFilmes = filmes.toArray(new Filme[filmes.size()]);

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter( new FilmeAdapter(this, R.layout.filme_listitem, arrayFilmes ));

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(), "Filme selecionado: " + arrayFilmes[position],
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

}
