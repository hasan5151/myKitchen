package com.yaros.kitchen.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.yaros.kitchen.R
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.bottomModel.*
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.ui.fragment.BaseFragment
import com.yaros.kitchen.utils.Preferences
import com.yaros.kitchen.viewModel.MainActivityVM
import com.yaros.kitchen.viewModel.factory.MainActivityFactory

class MainActivity : AppCompatActivity() {
    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout
    lateinit var viewPagerAdapter: ViewPagerAdaper
    lateinit var bottomInterface : BottomInterface
    lateinit var bottom_navigation : BottomNavigationView
    lateinit var mainActivityVM: MainActivityVM
    var isViewed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        setViewPagerAdapter()
        setTabLayout()
        setBottomNavigationView()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setOnClickListener({
            isViewed= !isViewed
            if (!isViewed){
                mainActivityVM.setIsFullScreen(false)

                bottom_navigation.setVisibility(View.VISIBLE)
                tabLayout.setVisibility(View.VISIBLE)
            }
            else{
                mainActivityVM.setIsFullScreen(true)
                bottom_navigation.setVisibility(View.GONE)
                tabLayout.setVisibility(View.GONE)
            }
        })



        mainActivityVM.checkDishes()
        mainActivityVM.checkWaiters()
        mainActivityVM.checkPrinters()
        mainActivityVM.getHashes()
        setHash()
    }

    private fun setHash() {
        mainActivityVM.getHashes()
        mainActivityVM.hash.observe(this, androidx.lifecycle.Observer {
            val oldTimeStamp = Preferences.getPref("timeStamp","",this)
            if(!oldTimeStamp!!.contentEquals(it.time_server.toString())){
                Preferences.savePref("timeStamp","${it.time_server}",this)
                val diff = System.currentTimeMillis()-it.time_server
                Preferences.savePref("diff","${diff}",this)
                System.out.println("timeHash ${it.time_server}")
            }

            val oldCatalogHash= Preferences.getPref("catalogHash","",this)
            if(!oldCatalogHash!!.contentEquals(it.catalog_hash.toString())){
                Preferences.savePref("catalogHash",it.catalog_hash,this)
                System.out.println("catalogHash ${it.catalog_hash}")
                mainActivityVM.fetchPrinters()
                mainActivityVM.fetchDishes()
                mainActivityVM.fetchWaiters()  //fetch datas with Work Manager
            }

            val oldOrderHash = Preferences.getPref("orderHash","",this)
            if(!oldOrderHash!!.contentEquals(it.orders_hash.toString())){
                Preferences.savePref("orderHash",it.orders_hash,this)
                System.out.println("orderHash ${it.orders_hash}")
                // if (::printerList.isInitialized)
                mainActivityVM.isDishesCreated.observe(this, androidx.lifecycle.Observer {dish->
                    mainActivityVM.isWaitersCreated.observe(this, androidx.lifecycle.Observer {waiters->
                        mainActivityVM.isPrintersCreated.observe(this, androidx.lifecycle.Observer {printers->
                            if (dish&&waiters&&printers) {
                            //    mainActivityVM.setOrderUpdate()
                                mainActivityVM.getOrderItems(null) //fetch datas with Work Manager
                            }
                        })
                    })
                })
            }
        })
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
                tabLayout.getTabAt(i)!!.icon = ContextCompat.getDrawable(this, viewPagerAdapter.fragmentList.get(i).getDrawable())
            tabLayout.getTabAt(i)?.text= viewPagerAdapter.getPageTitle(i)
//            tabLayout.getTabAt(i)?.badge = ;
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
