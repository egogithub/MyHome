package net.ego.myhome.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import net.ego.myhome.fragment.TempFragment;

import java.util.List;

public class TempPagerAdapter extends FragmentPagerAdapter {
    List<TempFragment> mFragList;

    public TempPagerAdapter(@NonNull FragmentManager fm, int behavior, List<TempFragment> fragList) {
        super(fm, behavior);
        mFragList=fragList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragList.get(position);
    }

    @Override
    public int getCount() {
        return mFragList.size();
    }
}
