package de.habibhaidari.foodcart.ui.region;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.model.Region;
import de.habibhaidari.foodcart.ui.region.postcode.PostcodeListFragment;
import de.habibhaidari.foodcart.ui.region.rate.RateListFragment;


public class RegionPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES_REGION = new int[]{R.string.costs_tab_text_1, R.string.costs_tab_text_2};
    private final Context mContext;
    private final Region r;

    public RegionPagerAdapter(Context context, FragmentManager fm, Region r) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.r = r;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return PostcodeListFragment.newInstance(r.getId(), r.getPostcodes());
        }
        return RateListFragment.newInstance(r.getId(), r.getRates());
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES_REGION[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}