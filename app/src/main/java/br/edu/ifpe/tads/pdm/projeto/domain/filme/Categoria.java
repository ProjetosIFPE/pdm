package br.edu.ifpe.tads.pdm.projeto.domain.filme;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Edmilson on 23/10/2016.
 */


public class Categoria implements Serializable {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String descricao;

    public Categoria(Integer id, String descricao){
        this.id = id;
        this.descricao = descricao;
    }

    public Categoria() {
        System.out.print("Sou doglas e sou gay");
    }

    public final static String ROOT_JSON_OBJECT = "genres";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
