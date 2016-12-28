package br.edu.ifpe.tads.pdm.projeto.domain.filme;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;
import org.parceler.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.edu.ifpe.tads.pdm.projeto.domain.musica.Musica;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by Edmilson Santana on 26/09/2016.
 */

@Entity(active = true, nameInDb = "FILME")
public class Filme implements Serializable {

    private static final long serialVersionUID = -6773928201271485864L;
    @Id
    @SerializedName("id")
    private Long id;

    @Index(unique = true)
    @SerializedName("title")
    private String titulo;

    @SerializedName("original_title")
    private String tituloOriginal;

    @SerializedName("overview")
    private String sinopse;

    @Transient
    @SerializedName("release_date")
    private Date dataLancamento;

    @SerializedName("poster_path")
    private String urlPoster;

    @SerializedName("backdrop_path")
    private String urlPlanoFundo;

    @ToMany(referencedJoinProperty = "filmeId")
    @Transient
    private List<Video> videos;

    @SerializedName("vote_average")
    private Double classificacao;

    @Transient
    @SerializedName("genre_ids")
    private List<Long> categoriaIds;


    @Transient
    private List<Categoria> categorias;

    @Transient
    private List<Musica> musicas;

    private final static String BASE_URL_POSTER = "https://image.tmdb.org/t/p/w342";

    private final static String BASE_URL_PLANO_FUNDO = "https://image.tmdb.org/t/p/w780";

    public final static String ROOT_JSON_OBJECT = "results";
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 117942895)
    private transient FilmeDao myDao;

    public Filme() {
    }

    public Filme(String titulo, String tituloOriginal) {
        this.titulo = titulo;
        this.tituloOriginal = tituloOriginal;
    }

    @Generated(hash = 656179483)
    public Filme(Long id, String titulo, String tituloOriginal, String sinopse,
            String urlPoster, String urlPlanoFundo, Double classificacao) {
        this.id = id;
        this.titulo = titulo;
        this.tituloOriginal = tituloOriginal;
        this.sinopse = sinopse;
        this.urlPoster = urlPoster;
        this.urlPlanoFundo = urlPlanoFundo;
        this.classificacao = classificacao;
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

    public String getUrlPoster(Boolean arquivo) {
        String url = "";
        if (StringUtils.isNotEmpty(urlPoster)) {
            if (!arquivo) {
                url = BASE_URL_POSTER.concat(urlPoster);
            } else {
                url = urlPoster;
            }
        }
        return url;

    }

    public Double getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(Double classificacao) {
        this.classificacao = classificacao;
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

    public String getUrlPlanoFundo(Boolean arquivo) {
        String url = "";
        if (StringUtils.isNotEmpty(urlPlanoFundo)) {
            if (!arquivo) {
                url = BASE_URL_PLANO_FUNDO.concat(urlPlanoFundo);
            } else {
                url = urlPlanoFundo;
            }
        }
        return url;
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

    public void addVideo(Video video) {
        if (videos == null) {
            videos = new ArrayList<>();
        }
        video.setFilmeId(getId());
        videos.add(video);
    }

    public Video getVideo(int index) {
        if (videos == null) {
            videos = new ArrayList<>();
        }
        return videos.get(index);
    }

    public List<Video> getVideos() {
        if (videos == null) {
            videos = new ArrayList<>();
        }
        return videos;
    }

    public void removerVideos() {
        getVideos().clear();
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void atualizarCategorias(Map<Long, Categoria> categoriasPorId) {
        if (categoriasPorId != null && categoriaIds != null) {
            for (Long idCategoria : categoriaIds) {
                this.addCategoria(categoriasPorId.get(idCategoria));
            }
        }
    }

    public void adicionarVideos(List<Video> videos) {

        for (Video video : videos) {
            if (video.isTrailerFromYoutube()) {
                this.addVideo(video);
            }
        }
    }

    public String getNomeArquivoPoster() {
        return getId() + urlPoster;
    }

    public String getNomeArquivoPlanoFundo() {
        return getId() + urlPlanoFundo;
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }


    public String getUrlPoster() {
        return this.urlPoster;
    }

    public String getUrlPlanoFundo() {
        return this.urlPlanoFundo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Filme filme = (Filme) o;

        return id.equals(filme.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1614378937)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFilmeDao() : null;
    }
}
