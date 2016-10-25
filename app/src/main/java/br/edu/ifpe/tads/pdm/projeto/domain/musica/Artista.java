package br.edu.ifpe.tads.pdm.projeto.domain.musica;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Edmilson on 23/10/2016.
 */

public class Artista {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
