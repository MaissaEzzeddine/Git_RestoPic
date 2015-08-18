package tn.codeit.restopic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabsAdapter extends FragmentStatePagerAdapter {
    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new MesPhotosFragment();

            case 1:
                return new AllPhotosFragment();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 2;
    }
}
