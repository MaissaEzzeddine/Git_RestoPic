package tn.codeit.restopic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new ClientFragment();

            case 1:
                return new AllPhotos();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
