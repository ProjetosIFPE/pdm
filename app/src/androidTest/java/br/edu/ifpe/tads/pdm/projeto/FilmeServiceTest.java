package br.edu.ifpe.tads.pdm.projeto;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.domain.Filme;
import br.edu.ifpe.tads.pdm.projeto.domain.FilmeService;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


/**
 * Created by Edmilson Santana on 26/09/2016.
 */
@RunWith(AndroidJUnit4.class)
public class FilmeServiceTest {


    @Test
    public void testGetFilmes() {
        FilmeService filmeService = new FilmeService();
        String querySearch = "Matrix Revolutions";
        List<Filme> filmes = filmeService.getFilmes(InstrumentationRegistry.getTargetContext(), querySearch);
        assertTrue(filmes.size() == 1);

        Filme filme = filmes.remove(0);
        assertEquals(querySearch, filme.getTitulo());

     }


}
