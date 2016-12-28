package br.edu.ifpe.tads.pdm.projeto.domain.musica;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Edmilson on 23/10/2016.
 */

public class Musica implements Serializable {

    public final static String ROOT_JSON_OBJECT = "songs";
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

    private Boolean tocando = Boolean.FALSE;

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

    public String getNomesArtistas() {
        return getArtista() != null ? getArtista().getNome() : "";
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


    public Boolean isPlaying() {
        return tocando;
    }

    public void play() {
        tocando = Boolean.TRUE;
    }

    public void stop() {
        tocando = Boolean.FALSE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Musica musica = (Musica) o;

        return id != null ? id.equals(musica.id) : musica.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
