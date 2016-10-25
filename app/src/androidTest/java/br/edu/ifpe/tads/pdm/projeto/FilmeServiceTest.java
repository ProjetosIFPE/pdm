package br.edu.ifpe.tads.pdm.projeto;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.domain.filme.Categoria;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.Filme;
import br.edu.ifpe.tads.pdm.projeto.domain.filme.FilmeService;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


/**
 * Created by Edmilson Santana on 26/09/2016.
 */

public class FilmeServiceTest extends BaseTestCase {

    private FilmeService filmeService;

    @Before
    public void setUp() {
        filmeService = new FilmeService();
    }

    @Test
    public void testGetFilmes() {
        String querySearch = "Matrix Revolutions";
        List<Filme> filmes = filmeService.getFilmes(getContext(), querySearch);
        assertTrue(filmes.size() == 1);

        Filme filme = filmes.get(0);
        assertEquals(querySearch, filme.getTitulo());
     }

    @Test
    public void testGetCategorias() {
        List<Categoria> categorias = filmeService.getCategorias(getContext());
        assertFalse(categorias.isEmpty());
        Categoria categoria = categorias.get(0);
        assertEquals(Long.valueOf(28), categoria.getId());
        assertEquals("Ação", categoria.getDescricao());
    }

    @Test
    public void testGetFilmesPorCategoria() {

        List<Categoria> categorias = filmeService.getCategorias(getContext());
        Categoria categoria = categorias.get(0);

        List<Filme> filmes = filmeService.getFilmes(getContext(), categoria);
        assertFalse(filmes.isEmpty());


    }

}
