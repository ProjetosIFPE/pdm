package br.edu.ifpe.tads.pdm.projeto.domain.musica;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Edmilson on 23/10/2016.
 */

public class Musica {

    @SerializedName("id")
    private Long id;
    @SerializedName("name")
    private String titulo;
    @SerializedName("artist")
    private Artista artista;
    @SerializedName("preview_url")
    private String urlMusica;
    @SerializedName("scene")
    private String descricaoCena;

    public final static String ROOT_JSON_OBJECT = "songs";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    public String getUrlMusica() {
        return urlMusica;
    }

    public void setUrlMusica(String urlMusica) {
        this.urlMusica = urlMusica;
    }

    public String getDescricaoCena() {
        return descricaoCena;
    }

    public void setDescricaoCena(String descricaoCena) {
        this.descricaoCena = descricaoCena;
    }
}
