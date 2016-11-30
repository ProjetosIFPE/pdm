package br.edu.ifpe.tads.pdm.projeto.domain.filme;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.edu.ifpe.tads.pdm.projeto.domain.musica.Musica;

/**
 * Created by Edmilson Santana on 26/09/2016.
 */

public class Filme implements Serializable {

    @SerializedName("id")
    private Long id;

    @SerializedName("title")
    private String titulo;

    @SerializedName("original_title")
    private String tituloOriginal;

    @SerializedName("overview")
    private String sinopse;

    @SerializedName("release_date")
    private Date dataLancamento;

    @SerializedName("poster_path")
    private String urlPoster;

    @SerializedName("backdrop_path")
    private String urlPlanoFundo;

    @SerializedName("genre_ids")
    private List<Integer> categoriaIds;

    private List<Categoria> categorias;

    private List<Musica> musicas;

    private final static String BASE_URL_POSTER = "https://image.tmdb.org/t/p/w500";

    public final static String ROOT_JSON_OBJECT = "results";

    public Filme(){}

    public Filme(String titulo, String tituloOriginal){
        this.titulo = titulo;
        this.tituloOriginal = tituloOriginal;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getTituloOriginal() {
        return tituloOriginal;
    }

    public void setTituloOriginal(String tituloOriginal) {
        this.tituloOriginal = tituloOriginal;
    }

    public String getUrlPoster() {
        return StringUtils.isNotEmpty(urlPoster)? BASE_URL_POSTER.concat(urlPoster) : "";
    }

    public void setUrlPoster(String urlPoster) {
        this.urlPoster = urlPoster;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlPlanoFundo() {
        return StringUtils.isNotEmpty(urlPlanoFundo)? BASE_URL_POSTER.concat(urlPlanoFundo) : "";
    }

    public void setUrlPlanoFundo(String urlPlanoFundo) {
        this.urlPlanoFundo = urlPlanoFundo;
    }

    public Musica getMusica(int index) {
        if (musicas == null) {
            musicas = new ArrayList<>();
        }
        return musicas.get(index);
    }

    public void addMusica(Musica musica) {
        if (musicas == null) {
            musicas = new ArrayList<>();
        }
        musicas.add(musica);
    }

    public void addCategoria(Categoria categoria) {
        if (categorias == null) {
            categorias = new ArrayList<>();
        }
        categorias.add(categoria);
    }

    public Categoria getCategoria(int index) {
        if (categorias == null) {
            categorias = new ArrayList<>();
        }
        return categorias.get(index);
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void atualizarCategorias(Map<Integer, Categoria> categoriasPorId) {
        if ( categoriasPorId != null && categoriaIds != null ) {
            for (Integer idCategoria : categoriaIds) {
                this.addCategoria(categoriasPorId.get(idCategoria));
            }
        }
    }




}
