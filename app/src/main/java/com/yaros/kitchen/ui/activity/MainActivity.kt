package com.yaros.kitchen.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.yaros.kitchen.R
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.bottomModel.*
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.ui.fragment.BaseFragment
import com.yaros.kitchen.viewModel.MainActivityVM
import com.yaros.kitchen.viewModel.PaginationVM
import com.yaros.kitchen.viewModel.factory.MainActivityFactory
import com.yaros.kitchen.viewModel.factory.PaginationFactory


class MainActivity : AppCompatActivity() {
    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout
    lateinit var viewPagerAdapter: ViewPagerAdaper
    lateinit var bottomInterface : BottomInterface
    lateinit var bottom_navigation : BottomNavigationView
    lateinit var mainActivityVM: MainActivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        setViewPagerAdapter()
        setTabLayout()
        setBottomNavigationView()
    }

    private fun setBottomNavigationView() {
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.order -> bottomInterface = OrderBottom()
                R.id.stoplist -> bottomInterface = StopListBottom()
                R.id.chat->bottomInterface = ChatBottom()
                R.id.envanter->bottomInterface = EnvanterBottom()
            }
            setViewPagerAdapter()
            setTabLayout()
            goToMenuFragment()
            true
        }
    }

    private fun goToMenuFragment() {
        mainActivityVM.isClicked.observe(this, Observer {
            if (it)
                viewPager.setCurrentItem(1)
        })
    }

    private fun init() {
        viewPager = findViewById(R.id.viewPager)
        viewPager.offscreenPageLimit = 2
        tabLayout = findViewById(R.id.tabs)
        bottomInterface = OrderBottom()
        bottom_navigation = findViewById(R.id.bottom_navigation)
        val mainActivityFactory =
            MainActivityFactory(
                RoomDb(this), RxSchedulers.DEFAULT,
                Api(this).getApi()
            )
        mainActivityVM = ViewModelProvider(this,mainActivityFactory ).get(MainActivityVM::class.java)
    }

    private fun setTabLayout() {
        tabLayout.setupWithViewPager(viewPager)
        for (i in 0 until viewPagerAdapter.getCount()) { // show steps
            if (viewPagerAdapter.fragmentList.get(i).getDrawable()!=0)
           // if (viewPagerAdapter.fragmentList.get(i).getDrawable!=0)
                tabLayout.getTabAt(i)!!.icon = ContextCompat.getDrawable(this, viewPagerAdapter.fragmentList.get(i).getDrawable())
            tabLayout.getTabAt(i)?.text= viewPagerAdapter.getPageTitle(i)
           // tabLayout.getTabAt(i)?.showBadge().setNumber(1);
        }
    }

    private fun setViewPagerAdapter() {
        viewPagerAdapter = ViewPagerAdaper(supportFragmentManager,bottomInterface.getItems())
        viewPager.adapter = viewPagerAdapter
    }

    class ViewPagerAdaper internal constructor(fm: FragmentManager?, val fragmentList : List<BaseFragment>) :
        FragmentStatePagerAdapter(
            fm!!,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
        override fun getItem(position: Int): Fragment = fragmentList[position]
        override fun getCount(): Int = fragmentList.size
        override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE
        override fun getPageTitle(position: Int): CharSequence? = fragmentList.get(position).getName()
    }
}
