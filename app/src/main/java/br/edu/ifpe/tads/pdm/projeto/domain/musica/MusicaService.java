package br.edu.ifpe.tads.pdm.projeto.domain.musica;

import android.content.Context;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.domain.BaseService;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeService;

/**
 * Created by Edmilson on 23/10/2016.
 */

public class MusicaService extends BaseService {

    private final String URL_PESQUISA_MUSICA_POR_FILME = "https://www.tunefind.com/api/v1/movie/{movie}";

    /**
     * Realiza a consulta de musicas por filme
     * @param context
     * @param filme
     * @return
     */
    public List<Musica> getMusicas(Context context, Filme filme){
        final String API_USERNAME = context.getString(R.string.API_USERNAME_TUNEFIND);
        final String API_PASSWORD = context.getString(R.string.API_PASSWORD_TUNEFIND);
        String url = URL_PESQUISA_MUSICA_POR_FILME
                .replace("{movie}", converteTituloFilme(filme.getTitulo()));
        return super.parseJson(Musica[].class,
                get(url, API_USERNAME, API_PASSWORD), Musica.ROOT_JSON_OBJECT);
    }

    /**
     * Converte o titulo do filme para o formato utilizado para pesquisa
     * dde músicas
     * @return
     */
    public String converteTituloFilme(String titulo) {
        return titulo.replace(" ", "-").toLowerCase();
    }


}
