package br.edu.ifpe.tads.pdm.projeto;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

/**
 * Created by Edmilson on 23/10/2016.
 */

@RunWith(AndroidJUnit4.class)
public class BaseTestCase  {
    public Context getContext() {
        return InstrumentationRegistry.getTargetContext();
    }

}
