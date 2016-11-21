package br.edu.ifpe.tads.pdm.projeto.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.edu.ifpe.tads.pdm.projeto.R;
import br.edu.ifpe.tads.pdm.projeto.fragment.FilmeFragment;
import br.edu.ifpe.tads.pdm.projeto.fragment.MusicasFragment;

/**
 * Created by Edmilson on 20/11/2016.
 */

public class FilmePagerAdapter extends FragmentPagerAdapter {

    private Context context;

    private Bundle arguments;

    public FilmePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    public FilmePagerAdapter(Context context, FragmentManager fm, Bundle arguments) {
        super(fm);
        this.context = context;
        this.arguments = arguments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.musicas_filme);
        } else {
            return context.getString(R.string.sobre_filme);
        }
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = MusicasFragment.newInstance(arguments);
                break;
            case 1:
                fragment = FilmeFragment.newInstance(arguments);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
