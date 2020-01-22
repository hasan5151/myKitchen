package com.yaros.kitchen.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.yaros.kitchen.R
import com.yaros.kitchen.ui.fragment.BaseFragment
import com.yaros.kitchen.ui.fragment.ReadyOrdersFragment
import com.yaros.kitchen.ui.fragment.OrderFragment
import com.yaros.kitchen.ui.fragment.SentOrdersFragment

class MainActivity : AppCompatActivity() {
    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout
    lateinit var viewPagerAdapter: ViewPagerAdaper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        setViewPagerAdapter()
        setTabLayout()
    }

    private fun init() {
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabs)
    }

    private fun setTabLayout() {
        tabLayout.setupWithViewPager(viewPager)
        for (i in 0 until viewPagerAdapter.getCount()) { // show steps
            tabLayout.getTabAt(i)!!.icon = ContextCompat.getDrawable(this, viewPagerAdapter.fragmentList!!.get(i).getDrawable())
            tabLayout.getTabAt(i)?.text= viewPagerAdapter.getPageTitle(i)
        }
    }

    private fun setViewPagerAdapter() {
        viewPagerAdapter =
            ViewPagerAdaper(
                supportFragmentManager
            )
        viewPager.adapter = viewPagerAdapter
    }


    class ViewPagerAdaper internal constructor(fm: FragmentManager?) :
        FragmentStatePagerAdapter(
            fm!!,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
        var fragmentList: List<BaseFragment>? = null
        init {
            fragmentList = listOf(OrderFragment(), ReadyOrdersFragment(),SentOrdersFragment())
        }

        override fun getItem(position: Int): Fragment = fragmentList!![position]
        override fun getCount(): Int = fragmentList!!.size
        override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE
        override fun getPageTitle(position: Int): CharSequence? = fragmentList!!.get(position).getName()
    }
}
