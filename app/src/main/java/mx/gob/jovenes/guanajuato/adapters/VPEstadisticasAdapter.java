package mx.gob.jovenes.guanajuato.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import mx.gob.jovenes.guanajuato.fragments.UsuariosGraficaFragment;
import mx.gob.jovenes.guanajuato.fragments.UsuariosInteresadosFragment;
import mx.gob.jovenes.guanajuato.fragments.UsuariosRegistradosFragment;

/**
 * Created by codigus on 11/08/2017.
 */

public class VPEstadisticasAdapter extends FragmentPagerAdapter {

    public VPEstadisticasAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new UsuariosRegistradosFragment();
                break;
            case 1:
                fragment = new UsuariosInteresadosFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
