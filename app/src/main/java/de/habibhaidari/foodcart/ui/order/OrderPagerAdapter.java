package de.habibhaidari.foodcart.ui.order;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.model.Order;
import de.habibhaidari.foodcart.ui.order.position.PositionFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class OrderPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_order, R.string.tab_text_positions};
    private final Context context;
    private final Order order;

    public OrderPagerAdapter(Context context, FragmentManager fm, Order order) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.order = order;
        this.context = context;
    }

    @Override
    public Fragment getItem(int fragment) {
        if (fragment == OrderFragment.ID) {
            return OrderFragment.newInstance(order);
        }
        return PositionFragment.newInstance(order.getPositions());
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}