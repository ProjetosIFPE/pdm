package br.edu.ifpe.tads.pdm.projeto.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.fragment.FilmesFragment;

/**
 * Created by Edmilson on 22/10/2016.
 */

public class TabsAdapter extends FragmentPagerAdapter {

    private Context context;

    public TabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.categoria_em_alta);
        } else if (position == 1) {
            return context.getString(R.string.categoria_lancamentos);
        } else {
            return context.getString(R.string.categoria_favoritos);
        }
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arguments = new Bundle();
        switch (position) {
            case 0:
                arguments.putBoolean(FilmesFragment.FILMES_POR_POPULARIDADE, Boolean.TRUE);
                break;
            case 1:
                arguments.putBoolean(FilmesFragment.FILMES_POR_DATA_LANCAMENTO, Boolean.TRUE);
                break;
        }
        return FilmesFragment.newInstance(arguments);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
