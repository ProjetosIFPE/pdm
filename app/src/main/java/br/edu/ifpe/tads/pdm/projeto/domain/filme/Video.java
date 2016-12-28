package br.edu.ifpe.tads.pdm.projeto.domain.filme;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Edmilson Santana on 26/12/2016.
 */

@Entity
public class Video implements Serializable {

    public static final String TRAILER_TIPO = "Trailer";
    public static final String YOUTUBE_SITE = "YouTube";
    private static final long serialVersionUID = -529323259760492941L;

    @Id
    @SerializedName("id")
    private String id;

    @SerializedName("site")
    private String site;

    @SerializedName("type")
    private String tipo;

    @SerializedName("name")
    private String descricao;

    @SerializedName("key")
    private String chave;

    @SerializedName("iso_639_1")
    private String linguagem;

    @SerializedName("iso_3166_1")
    private String pais;

    private Long filmeId;

    public final static String ROOT_JSON_OBJECT = "results";


    @Generated(hash = 2014191101)
    public Video(String id, String site, String tipo, String descricao,
            String chave, String linguagem, String pais, Long filmeId) {
        this.id = id;
        this.site = site;
        this.tipo = tipo;
        this.descricao = descricao;
        this.chave = chave;
        this.linguagem = linguagem;
        this.pais = pais;
        this.filmeId = filmeId;
    }

    @Generated(hash = 237528154)
    public Video() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getLinguagem() {
        return linguagem;
    }

    public void setLinguagem(String linguagem) {
        this.linguagem = linguagem;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean isTrailerFromYoutube() {
        return YOUTUBE_SITE.equals(site) && TRAILER_TIPO.equals(tipo);
    }

    public Long getFilmeId() {
        return this.filmeId;
    }

    public void setFilmeId(Long filmeId) {
        this.filmeId = filmeId;
    }

}
