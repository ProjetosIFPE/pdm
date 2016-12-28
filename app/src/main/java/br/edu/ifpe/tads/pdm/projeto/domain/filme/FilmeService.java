package br.edu.ifpe.tads.pdm.projeto.domain.filme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.domain.BaseService;
import br.edu.ifpe.tads.pdm.projeto.util.DateUtil;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Edmilson Santana on 26/09/2016.
 */
public class FilmeService extends BaseService {

    private static final String URL_PESQUISA_FILME_POR_TITULO = "https://api.themoviedb.org/3/search/movie?api_key={key}&language={language}&query={titulo}&page={page}";
    private static final String URL_PESQUISA_FILME_POR_CATEGORIA = "https://api.themoviedb.org/3/genre/{genre}/movies?api_key={key}&language={language}&sort_by={sort}&page={page}";
    private static final String URL_PESQUISA_CATEGORIA = "https://api.themoviedb.org/3/genre/movie/list?api_key={key}&language={language}";
    private static final String URL_PESQUISA_FILME_POR_POPULARIDADE = "https://api.themoviedb.org/3/discover/movie?api_key={key}&language={language}&sort_by={sort}&page={page}";
    private static final String URL_PESQUISA_FILME_POR_LANCAMENTO = "https://api.themoviedb.org/3/discover/movie?api_key={key}&language={language}&sort_by={sort}&page={page}&primary_release_date.lte={release-date}";
    private static final String URL_PESQUISA_VIDEOS_FILME = "https://api.themoviedb.org/3/movie/{id}/videos?api_key={key}";


    /**
     * Busca os videos de um filme no TMDB
     *
     * @param context
     * @param idFilme
     * @return
     */
    public List<Video> getVideosFilme(Context context, Long idFilme) {
        final String API_KEY = context.getString(R.string.API_KEY_TMDB);
        String url = URL_PESQUISA_VIDEOS_FILME
                .replace("{key}", API_KEY)
                .replace("{id}", String.valueOf(idFilme));
        return super.parseJsonArray(Video[].class, get(url), Video.ROOT_JSON_OBJECT);
    }

    /**
     * Realiza a consulta dos géneros que estão disponíveis no TMDB
     *
     * @param context
     * @return
     */
    public List<Categoria> getCategorias(Context context) {
        final String API_KEY = context.getString(R.string.API_KEY_TMDB);
        String url = URL_PESQUISA_CATEGORIA
                .replace("{key}", API_KEY)
                .replace("{language}", FilmeService.TMDBParameters.LANGUAGE_PT_BR);
        return super.parseJsonArray(Categoria[].class, get(url), Categoria.ROOT_JSON_OBJECT);
    }

    /**
     * Obtém a categoria por descrição
     *
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
     * Obtém os filmes que foram adicionados recentemente,
     * permitindo paginação.
     *
     * @param context
     * @return
     */
    public List<Filme> getLancamentos(Context context, int page) {
        final String API_KEY = context.getString(R.string.API_KEY_TMDB);
        String url = URL_PESQUISA_FILME_POR_LANCAMENTO
                .replace("{language}", FilmeService.TMDBParameters.LANGUAGE_PT_BR)
                .replace("{key}", API_KEY)
                .replace("{release-date}", DateUtil.dateToString(Calendar.getInstance()))
                .replace("{sort}", TMDBParameters.ORDER_BY_RELEASE_DATE_DESC)
                .replace("{page}", String.valueOf(page));
        return parseJsonFilmes(context, url);
    }


    /**
     * Obtém os filmes por popularidade, permitindo paginação
     *
     * @param context
     * @param page
     * @return
     */
    public List<Filme> getPopulares(Context context, int page) {
        final String API_KEY = context.getString(R.string.API_KEY_TMDB);
        String url = URL_PESQUISA_FILME_POR_POPULARIDADE
                .replace("{language}", FilmeService.TMDBParameters.LANGUAGE_PT_BR)
                .replace("{key}", API_KEY)
                .replace("{sort}", FilmeService.TMDBParameters.ORDER_BY_POPULARITY_DESC)
                .replace("{page}", String.valueOf(page));
        return parseJsonFilmes(context, url);
    }


    /**
     * Realiza a consulta de filmes no TMDB, por categoria,
     * permitindo paginação
     *
     * @param context
     * @param categoria
     * @return
     */
    public List<Filme> getFilmes(Context context, Categoria categoria, int page) {
        final String API_KEY = context.getString(R.string.API_KEY_TMDB);
        String url = URL_PESQUISA_FILME_POR_CATEGORIA
                .replace("{language}", FilmeService.TMDBParameters.LANGUAGE_PT_BR)
                .replace("{key}", API_KEY)
                .replace("{sort}", FilmeService.TMDBParameters.ORDER_BY_POPULARITY_DESC)
                .replace("{page}", String.valueOf(page))
                .replace("{genre}", String.valueOf(categoria.getId()));
        return parseJsonFilmes(context, url);
    }


    /**
     * Realiza a consulta de filmes no TMDB, por título,
     * permitindo paginação
     *
     * @param context
     * @param titulo
     * @return
     */
    public List<Filme> getFilmes(Context context, String titulo, int page) {
        final String API_KEY = context.getString(R.string.API_KEY_TMDB);
        String url = URL_PESQUISA_FILME_POR_TITULO
                .replace("{key}", API_KEY)
                .replace("{titulo}", titulo)
                .replace("{page}", String.valueOf(page))
                .replace("{language}", TMDBParameters.LANGUAGE_PT_BR);
        return parseJsonFilmes(context, url);
    }

    /**
     * Realiza o parse do json dos filmes
     *
     * @param context
     * @param url
     * @return
     */
    private List<Filme> parseJsonFilmes(Context context, String url) {
        List<Filme> filmes = super.parseJsonArray(Filme[].class, get(url), Filme.ROOT_JSON_OBJECT);
        return preencherCategoriasFilmes(context, filmes);
    }

    /**
     * Atualiza as categorias de uma lista de filmes
     *
     * @param context
     * @param filmes
     * @return
     */
    public List<Filme> preencherCategoriasFilmes(Context context, List<Filme> filmes) {
        List<Categoria> categorias = getCategorias(context);
        Map<Long, Categoria> categoriasPorId = categoriasPorId(categorias);
        List<Filme> filmesAtualizados = new ArrayList<>(filmes);
        for (Filme filme : filmes) {
            filme.atualizarCategorias(categoriasPorId);
        }
        return filmesAtualizados;
    }


    /**
     * Cria um mapa de categorias por id de categoria
     *
     * @param categorias
     * @return
     */
    public Map<Long, Categoria> categoriasPorId(List<Categoria> categorias) {
        Map<Long, Categoria> categoriasPorId = new HashMap<>();
        for (Categoria categoria : categorias) {
            if (!categoriasPorId.containsKey(categoria.getId())) {
                categoriasPorId.put(categoria.getId(), categoria);
            }
        }
        return categoriasPorId;
    }


    public class TMDBParameters {
        public static final String ORDER_BY_POPULARITY_DESC = "popularity.desc";
        public static final String LANGUAGE_PT_BR = "pt-BR";
        public static final String FIRST_PAGE = "1";
        public static final String ORDER_BY_RELEASE_DATE_DESC = "release_date.desc";
    }

}
