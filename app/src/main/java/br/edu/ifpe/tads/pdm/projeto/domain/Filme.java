package br.edu.ifpe.tads.pdm.projeto.domain;

import java.util.Date;

/**
 * Created by EdmilsonS on 26/09/2016.
 */
public class Filme {

    private String titulo;
    private String sinopse;
    private Date dataLancamento;
    private String urlPoster;

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
        return urlPoster;
    }

    public void setUrlPoster(String urlPoster) {
        this.urlPoster = urlPoster;
    }
}
