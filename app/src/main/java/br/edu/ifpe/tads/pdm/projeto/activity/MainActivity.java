package br.edu.ifpe.tads.pdm.projeto.activity;

import android.os.Bundle;
import android.util.Log;
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
import br.edu.ifpe.tads.pdm.projeto.util.TaskListener;


public class MainActivity extends BaseActivity {


    private FilmeService filmeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();

        filmeService = new FilmeService();

        String titulo = "Matrix";

        super.startTask(this.getFilmes(titulo));
    }


    public TaskListener<Filme[]> getFilmes(final String titulo) {

        return new TaskListener<Filme[]>() {
            @Override
            public Filme[] execute() throws Exception {
                Filme[] filmes = new Filme[0];
                 if ( filmeService != null ) {
                    List<Filme> listaFilmes = filmeService.getFilmes(getContext(),
                            titulo);
                     Log.d(TAG, "Lista Filmes:  " + listaFilmes.size());
                     filmes = listaFilmes.toArray(new Filme[listaFilmes.size()]);
                }
                return  filmes;
            }

            @Override
            public void updateView(final Filme[] response) {
                Log.d(TAG, "Resposta: " + response.length);
                ListView listView = (ListView) findViewById(R.id.list_view);

                listView.setAdapter( new FilmeAdapter(getContext(),
                        R.layout.filme_listitem, response ));
                listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(parent.getContext(), "Filme selecionado: " +
                                response[position].getTitulo(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(Exception exception) {

            }
            @Override
            public void onCancelled(String cod) {

            }
        };
    }

}
