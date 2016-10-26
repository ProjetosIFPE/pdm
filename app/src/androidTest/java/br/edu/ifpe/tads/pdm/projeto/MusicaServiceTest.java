package br.edu.ifpe.tads.pdm.projeto;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeService;
import br.edu.ifpe.tads.pdm.projeto.domain.musica.Musica;
import br.edu.ifpe.tads.pdm.projeto.domain.musica.MusicaService;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 * Created by Edmilson on 23/10/2016.
 */

public class MusicaServiceTest extends BaseTestCase{

    private MusicaService musicaService;
    private FilmeService filmeService;

    @Before
    public void setUp() {
        musicaService = new MusicaService();
        filmeService = new FilmeService();
    }

    @Test
    public void testGetMusicas() {
        String querySearch = "Matrix";
        List<Filme> filmes = filmeService.getFilmes(getContext(), querySearch);
        Filme filme = filmes.get(0);

        List<Musica> musicas = musicaService.getMusicas(getContext(), filme);
        assertFalse(musicas.isEmpty());

        Musica musica = musicas.get(0);
        assertEquals("My Own Summer (Shove It)", musica.getTitulo());
    }

    @Test
    public void testGetMusicasFilmeComCaracterEspecial() {
        String querySearch = "50/50";
        List<Filme> filmes = filmeService.getFilmes(getContext(), querySearch);
        Filme filme = filmes.get(0);

        List<Musica> musicas = musicaService.getMusicas(getContext(), filme);
        assertFalse(musicas.isEmpty());

    }


}
