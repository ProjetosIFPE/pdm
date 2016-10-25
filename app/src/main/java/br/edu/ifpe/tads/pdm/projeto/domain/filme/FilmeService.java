package br.edu.ifpe.tads.pdm.projeto.domain.filme;

import android.content.Context;

import org.parceler.apache.commons.lang.time.DateFormatUtils;

import java.util.Calendar;
import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.domain.BaseService;
import br.edu.ifpe.tads.pdm.projeto.util.Util;

/**
 * Created by Edmilson Santana on 26/09/2016.
 */
public class FilmeService extends BaseService {

    private static final String URL_PESQUISA_FILME_POR_TITULO = "https://api.themoviedb.org/3/search/movie?api_key={key}&language={language}&query={titulo}";
    private static final String URL_PESQUISA_FILME_POR_CATEGORIA = "https://api.themoviedb.org/3/discover/movie?api_key={key}&language={language}&sort_by={sort}&page={page}&with_genres={genres}";
    private static final String URL_PESQUISA_CATEGORIA = "https://api.themoviedb.org/3/genre/movie/list?api_key={key}&language={language}";
    private static final String URL_PESQUISA_FILME_POR_POPULARIDADE = "https://api.themoviedb.org/3/discover/movie?api_key={key}&language={language}&sort_by={sort}&page={page}";
    private static final String URL_PESQUISA_FILME_POR_LANCAMENTO = "https://api.themoviedb.org/3/discover/movie?api_key={key}&language={language}&sort_by={sort}&page={page}&primary_release_date.lte={release-date}";

    /**
     * Realiza a consulta dos géneros que estão disponíveis no TMDB
     * @param context
     * @return
     */
    public List<Categoria> getCategorias(Context context) {
        final String API_KEY = context.getString(R.string.API_KEY_TMDB);
        String url = URL_PESQUISA_CATEGORIA
                .replace("{key}", API_KEY)
                .replace("{language}", FilmeService.TMDBParameters.LANGUAGE_PT_BR);
        return super.parseJson(Categoria[].class, get(url), Categoria.ROOT_JSON_OBJECT);
    }

    /**
     * Obtém a categoria por descrição
     * @param context
     * @return
     */
    public Categoria getCategoria(Context context, String descricaoCategoria) {
        List<Categoria> categorias = getCategorias(context);
        Categoria categoria = new Categoria();
        categoria.setDescricao(descricaoCategoria);
        return categorias.get(categorias.indexOf(categoria));
    }

    /**
     * Obtém os filmes que foram adicionados recentemente
     * @param context
     * @return
     */
    public List<Filme> getLancamentos(Context context) {
        final String API_KEY = context.getString(R.string.API_KEY_TMDB);
        String url = URL_PESQUISA_FILME_POR_LANCAMENTO
                .replace("{language}", FilmeService.TMDBParameters.LANGUAGE_PT_BR)
                .replace("{key}", API_KEY)
                .replace("{release-date}", Util.dateToString(Calendar.getInstance()))
                .replace("{sort}", TMDBParameters.ORDER_BY_RELEASE_DATE_DESC)
                .replace("{page}", FilmeService.TMDBParameters.FIRST_PAGE);
        return super.parseJson(Filme[].class, get(url), Filme.ROOT_JSON_OBJECT);
    }

    /**
     * Obtém os filmes por popularidade
     * @param context
     * @return
     */
    public List<Filme> getPopulares(Context context) {
        final String API_KEY = context.getString(R.string.API_KEY_TMDB);
        String url = URL_PESQUISA_FILME_POR_POPULARIDADE
                .replace("{language}", FilmeService.TMDBParameters.LANGUAGE_PT_BR)
                .replace("{key}", API_KEY)
                .replace("{sort}", FilmeService.TMDBParameters.ORDER_BY_POPULARITY_DESC)
                .replace("{page}", FilmeService.TMDBParameters.FIRST_PAGE);
        return super.parseJson(Filme[].class, get(url), Filme.ROOT_JSON_OBJECT);
    }

    /**
     * Obtém os filmes que foram adicionados recentemente
     * @param context
     * @return
     */
    public List<Filme> getFilmes(Context context) {
        final String API_KEY = context.getString(R.string.API_KEY_TMDB);
        String url = URL_PESQUISA_FILME_POR_CATEGORIA
                .replace("{language}", FilmeService.TMDBParameters.LANGUAGE_PT_BR)
                .replace("{key}", API_KEY)
                .replace("{sort}", FilmeService.TMDBParameters.ORDER_BY_POPULARITY_DESC)
                .replace("{page}", FilmeService.TMDBParameters.FIRST_PAGE);
        return super.parseJson(Filme[].class, get(url), Filme.ROOT_JSON_OBJECT);
    }

    /**
     * Realiza a consulta de filmes no TMDB, por categoria
     * @param context
     * @param categoria
     * @return
     */
    public List<Filme> getFilmes(Context context, Categoria categoria)  {
        final String API_KEY = context.getString(R.string.API_KEY_TMDB);
        String url = URL_PESQUISA_FILME_POR_CATEGORIA
                .replace("{language}", FilmeService.TMDBParameters.LANGUAGE_PT_BR)
                .replace("{key}", API_KEY)
                .replace("{sort}", FilmeService.TMDBParameters.ORDER_BY_POPULARITY_DESC)
                .replace("{page}", FilmeService.TMDBParameters.FIRST_PAGE)
                .replace("{genres}", String.valueOf(categoria.getId()));
        return super.parseJson(Filme[].class, get(url), Filme.ROOT_JSON_OBJECT);
    }


    /**
     * Realiza a consulta de filmes no TMDB, por título
     * @param context
     * @param titulo
     * @return
     */
    public  List<Filme> getFilmes(Context context, String titulo){
        final String API_KEY = context.getString(R.string.API_KEY_TMDB);
        String url = URL_PESQUISA_FILME_POR_TITULO
                .replace("{key}", API_KEY)
                .replace("{titulo}", titulo)
                .replace("{language}", TMDBParameters.LANGUAGE_PT_BR);
        return super.parseJson(Filme[].class, get(url), Filme.ROOT_JSON_OBJECT);
    }



    public class TMDBParameters {
        public static final String ORDER_BY_POPULARITY_DESC = "popularity.desc";
        public static final String LANGUAGE_PT_BR = "pt-BR";
        public static final String FIRST_PAGE = "1";
        public static final String ORDER_BY_RELEASE_DATE_DESC = "release_date.desc";
    }

}
