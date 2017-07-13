package com.example.admin.emojime.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import com.example.admin.emojime.Fragments.TabViewSubFragments.EmojiMeFragment;
import com.example.admin.emojime.Fragments.TabViewSubFragments.ExpressionsFragment;
import com.example.admin.emojime.Fragments.TabViewSubFragments.ObjectsFragment;
import com.example.admin.emojime.Fragments.TabViewSubFragments.SettingsFragment;
import com.example.admin.emojime.R;
import com.example.admin.emojime.Utils.PagerSlidingTabStrip;

public class TabViewPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider
{
    private String tabTitles[];
    private int icons[] = {R.drawable.tabicon12,R.drawable.tabicon21,R.drawable.tabicon31, R.drawable.tabicon411};
    Context mContext;

    public TabViewPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        tabTitles = context.getResources().getStringArray(R.array.tab_titles);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return new ExpressionsFragment();
            case 1:
                return new ObjectsFragment();
            case 2:
                return new EmojiMeFragment();
            case 3:
                return new SettingsFragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount()
    {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        // Generate title based on item position
        Drawable image = mContext.getResources().getDrawable(icons[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());

        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("   " + tabTitles[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    @Override
    public int getPageIconResId(int position)
    {
        return icons[position];
    }
}
