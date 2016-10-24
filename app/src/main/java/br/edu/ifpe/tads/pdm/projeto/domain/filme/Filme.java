package br.edu.ifpe.tads.pdm.projeto.domain.filme;

import com.google.gson.annotations.SerializedName;

import org.parceler.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.domain.musica.Musica;

/**
 * Created by Edmilson Santana on 26/09/2016.
 */

public class Filme implements Serializable {

    @SerializedName("id")
    private Long id;

    @SerializedName("title")
    private String titulo;

    @SerializedName("overview")
    private String sinopse;

    @SerializedName("release_date")
    private Date dataLancamento;

    @SerializedName("poster_path")
    private String urlPoster;

    private List<Categoria> categorias;

    private List<Musica> musicas;

    private final static String BASE_URL_POSTER = "https://image.tmdb.org/t/p/w500";

    public final static String ROOT_JSON_OBJECT = "results";

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

    public Musica getMusica(int index) {
        if (musicas != null) {
            musicas = new ArrayList<>();
        }
        return musicas.get(index);
    }

    public void addMusica(Musica musica) {
        if (musicas != null) {
            musicas = new ArrayList<>();
        }
        musicas.add(musica);
    }

}
