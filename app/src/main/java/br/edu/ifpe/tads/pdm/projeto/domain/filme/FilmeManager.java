package br.edu.ifpe.tads.pdm.projeto.domain.filme;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.util.DateUtil;
import br.edu.ifpe.tads.pdm.projeto.util.FileUtil;
import br.edu.ifpe.tads.pdm.projeto.util.NetworkUtil;

/**
 * Created by Edmilson Santana on 27/12/2016.
 */

public class FilmeManager {
    private FilmeService filmeService;
    private FilmeDao filmeDao;
    private CategoriaDao categoriaDao;
    private VideoDao videoDao;


    private final int FIRST_PAGE = 1;

    public FilmeManager(VideoDao videoDao, CategoriaDao categoriaDao, FilmeDao filmeDao, FilmeService service) {
        this.filmeDao = filmeDao;
        this.filmeService = service;
        this.categoriaDao = categoriaDao;
        this.videoDao = videoDao;
    }

    /**
     * Busca os videos de um filme
     *
     * @param context
     * @param filme
     * @return
     */
    public Filme carregarVideosFilme(Context context, Filme filme) {
        List<Video> videos = filmeService.getVideosFilme(context, filme.getId());
        filme.adicionarVideos(videos);
        return filme;
    }

    /**
     * Realiza a listagem de todos os filmes favoritos
     *
     * @return
     */
    public List<Filme> getFavoritos() {
        return filmeDao.loadAll();
    }

    /**
     * Salva um novo filme
     *
     * @param filme
     * @return
     */
    public Boolean novoFavorito(Context context, Filme filme) {
        inserirVideosFilme(filme);
        converterImagensFilme(context, filme);
        return filmeDao.insert(filme) > 0;
    }

    private void converterImagensFilme(Context context, Filme filme) {
        FileUtil.salvarImagem(context, filmeService.getBitmap(
                filme.getUrlPoster(Boolean.FALSE)), filme.getUrlPoster(Boolean.TRUE));
        FileUtil.salvarImagem(context, filmeService.getBitmap(
                filme.getUrlPlanoFundo(Boolean.FALSE)), filme.getUrlPlanoFundo(Boolean.TRUE));
    }


    private void inserirVideosFilme(Filme filme) {
        videoDao.insertInTx(filme.getVideos());
    }

    /**
     * Verifica se o filme é um favorito
     *
     * @param filme
     * @return
     */
    public Boolean existeFilmeFavorito(Filme filme) {
        return filmeDao.load(filme.getId()) != null;
    }

    /**
     * Remove um filme dos favoritos
     *
     * @param filme
     */
    public void removerFavorito(Context context, Filme filme) {
        removerVideosFilme(filme);
        removerImagensFilme(context, filme);
        filmeDao.delete(filme);
    }

    private void removerImagensFilme(Context context, Filme filme) {
        FileUtil.removerImagem(context, filme.getUrlPoster(Boolean.TRUE));
        FileUtil.removerImagem(context, filme.getUrlPlanoFundo(Boolean.TRUE));
    }

    private void removerVideosFilme(Filme filme) {

        videoDao.deleteInTx(filme.getVideos());
        filme.removerVideos();
    }


    /**
     * Realiza a consulta dos géneros dos filmes
     *
     * @param context
     * @return
     */
    public List<Categoria> getCategorias(Context context) {
        List<Categoria> categoriasExistentes = categoriaDao.loadAll();
        List<Categoria> novasCategorias = new ArrayList<>();
        if (NetworkUtil.isConnected(context)) {
            novasCategorias.addAll(filmeService.getCategorias(context));
        }
        novasCategorias.removeAll(categoriasExistentes);
        if (!novasCategorias.isEmpty()) {
            categoriaDao.insertInTx(novasCategorias);
        }
        novasCategorias.addAll(categoriasExistentes);

        return novasCategorias;
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
     * Obtém os filmes que foram adicionados recentemente
     *
     * @param context
     * @return
     */
    public List<Filme> getLancamentos(Context context) {
        return getLancamentos(context, FIRST_PAGE);
    }


    /**
     * Obtém os filmes que foram adicionados recentemente,
     * permitindo paginação.
     *
     * @param context
     * @return
     */
    public List<Filme> getLancamentos(Context context, int page) {

        return filmeService.getLancamentos(context, page);
    }


    /**
     * Obtém os filmes por popularidade
     *
     * @param context
     * @return
     */
    public List<Filme> getPopulares(Context context) {
        return getPopulares(context, FIRST_PAGE);
    }

    /**
     * Obtém os filmes por popularidade, permitindo paginação
     *
     * @param context
     * @param page
     * @return
     */
    public List<Filme> getPopulares(Context context, int page) {

        return filmeService.getPopulares(context, page);
    }


    /**
     * Realiza a consulta de filmes, por categoria
     *
     * @param context
     * @param categoria
     * @return
     */
    public List<Filme> getFilmes(Context context, Categoria categoria) {
        return this.getFilmes(context, categoria, FIRST_PAGE);
    }

    /**
     * Realiza a consulta de filmes, por categoria,
     * permitindo paginação
     *
     * @param context
     * @param categoria
     * @return
     */
    public List<Filme> getFilmes(Context context, Categoria categoria, int page) {

        return filmeService.getFilmes(context, categoria, page);
    }


    /**
     * Realiza a consulta de filmes, por título
     *
     * @param context
     * @param titulo
     * @return
     */
    public List<Filme> getFilmes(Context context, String titulo) {
        return this.getFilmes(context, titulo, FIRST_PAGE);
    }

    /**
     * Realiza a consulta de filmes, por título,
     * permitindo paginação
     *
     * @param context
     * @param titulo
     * @return
     */
    public List<Filme> getFilmes(Context context, String titulo, int page) {

        return filmeService.getFilmes(context, titulo, page);
    }


}
