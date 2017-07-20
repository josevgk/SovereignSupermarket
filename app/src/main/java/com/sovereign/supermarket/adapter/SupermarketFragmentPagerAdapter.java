package com.sovereign.supermarket.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sovereign.supermarket.fragment.SupermarketHomeFragment;
import com.sovereign.supermarket.fragment.SupermarketProductsFragment;
import com.sovereign.supermarket.fragment.SupermarketReviewFragment;

public class SupermarketFragmentPagerAdapter extends FragmentPagerAdapter {

    private String keySupermarket;
    private String tabTitles[] =
            new String[] { "INFORMATION", "OUR PRODUCTS", "REVIEWS"};
    final int PAGE_COUNT = tabTitles.length;

    public SupermarketFragmentPagerAdapter(FragmentManager fm, String keySupermarket) {
        super(fm);
        this.keySupermarket = keySupermarket;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        switch(position) {
            case 2:
                f= SupermarketReviewFragment.newInstance(keySupermarket);
                break;
            case 0:
                f = SupermarketHomeFragment.newInstance(keySupermarket);
                break;
            case 1:
                f = SupermarketProductsFragment.newInstance(keySupermarket, null);
                break;
        }

        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}