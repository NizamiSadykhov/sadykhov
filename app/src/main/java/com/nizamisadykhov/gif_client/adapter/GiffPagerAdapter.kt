package com.nizamisadykhov.gif_client.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nizamisadykhov.gif_client.R
import com.nizamisadykhov.gif_client.fragment.PageFragment

class GiffPagerAdapter(fm: FragmentManager, context: Context) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val titles: Array<String> = context.resources.getStringArray(R.array.tab_titles)

    override fun getCount(): Int {
        return titles.size
    }

    override fun getItem(position: Int): Fragment {
        return PageFragment.newInstance(position)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }
}