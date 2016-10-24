package br.edu.ifpe.tads.pdm.projeto.domain.filme;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Edmilson on 23/10/2016.
 */


public class Categoria implements Serializable {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String descricao;

    public final static String ROOT_JSON_OBJECT = "genres";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Categoria categoria = (Categoria) o;

        return getDescricao() != null ? getDescricao().equals(categoria.getDescricao()) : categoria.getDescricao() == null;

    }

    @Override
    public int hashCode() {
        return getDescricao() != null ? getDescricao().hashCode() : 0;
    }
}
