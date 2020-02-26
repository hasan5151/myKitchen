package com.yaros.kitchen.ui.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
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
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.yaros.kitchen.R
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.bottomModel.*
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.ui.fragment.BaseFragment
import com.yaros.kitchen.utils.CatalogWM
import com.yaros.kitchen.utils.CatalogWM.Companion.DISHES
import com.yaros.kitchen.utils.CatalogWM.Companion.ORDERS
import com.yaros.kitchen.utils.CatalogWM.Companion.PRINTERS
import com.yaros.kitchen.utils.CatalogWM.Companion.WAITERS
import com.yaros.kitchen.utils.Preferences
import com.yaros.kitchen.viewModel.MainActivityVM
import com.yaros.kitchen.viewModel.factory.MainActivityFactory
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout
    lateinit var viewPagerAdapter: ViewPagerAdaper
    lateinit var bottomInterface : BottomInterface
    lateinit var bottom_navigation : BottomNavigationView
    lateinit var mainActivityVM: MainActivityVM
    var isViewed: Boolean = false
    val NOTSTARTED = 0
    val STARTED = 1
    val FINISHED = 2
    val NOREQTIME = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (isTablet()){
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        init()
        setViewPagerAdapter()
        setTabLayout()
        setBottomNavigationView()
        orderFetched()
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

   /*     mainActivityVM.waitersFetched.observe(this, Observer {
            if (it){
                mainActivityVM.setApiService(Api(this))
                mainActivityVM.getHashes()
            }
        })*/

        checkFirstFetch()
        setHash()
    }


    private fun setHash() {
        System.out.println("selam Hash")

        mainActivityVM.hash.observe(this, androidx.lifecycle.Observer {
            val oldTimeStamp = Preferences.getPref("timeStamp","",this)
            if(!oldTimeStamp!!.contentEquals(it.time_server.toString())){
                System.out.println("selam2 Hash")
                Preferences.savePref("timeStamp","${it.time_server}",this)
                val diff = System.currentTimeMillis()-it.time_server
                Preferences.savePref("diff","${diff}",this)
                System.out.println("timeHash ${it.time_server}")
            }


            val oldCatalogHash= Preferences.getPref("catalogHash","",this)


            System.out.println("selam Hash1 ${it.catalog_hash.toString()}")
            System.out.println("selam Hash2 ${oldCatalogHash}")

            if(!oldCatalogHash!!.contentEquals(it.catalog_hash.toString())){
                System.out.println("selam3 Hash")
                Preferences.savePref("catalogHash",it.catalog_hash,this)
                System.out.println("catalogHash ${it.catalog_hash}")
                sendToServer(DISHES)
                sendToServer(PRINTERS)
                sendToServer(WAITERS)
            }

            val oldOrderHash = Preferences.getPref("orderHash","",this)
            if(!oldOrderHash!!.contentEquals(it.orders_hash.toString())){
                System.out.println("selam4")
                Preferences.savePref("orderHash",it.orders_hash,this)
                System.out.println("orderHash ${it.orders_hash}")

                mainActivityVM.isInstallationComplete.observe(this, androidx.lifecycle.Observer {
                    if (it) {
                        System.out.println("selam5")
                        sendToServer(ORDERS)
                    }
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

    private fun sendToServer(type: Int){
        val data: Data = Data.Builder()
            .putInt("type",type)
            .build()
        val catalogReq= OneTimeWorkRequest.Builder(CatalogWM::class.java)
            .setInputData(data)
            .build()
        val operation = WorkManager.getInstance(this).enqueue(catalogReq)

         getStatusOfManager(catalogReq.id)

    }

    private fun getStatusOfManager(id: UUID) {
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(id)
            .observe(this, Observer { workInfo: WorkInfo ->
                System.out.println("selam napion ana1 ${workInfo.state}")
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val data = workInfo.outputData
                    val type= data.getInt("type",-1)
                    System.out.println("selam napion ana ${type}")
                    when(type){
                        ORDERS -> mainActivityVM.setIsOrderFetched()
                        PRINTERS -> mainActivityVM.setIsPrinterFetch(true)
                        DISHES -> mainActivityVM.setIsDishFetch(true)
                        WAITERS -> mainActivityVM.setIsWaiterFetch(true)
                    }
                }
            })
    }

    private fun orderFetched() {
        mainActivityVM.isOrderFetched.observe(this, Observer {
            mainActivityVM.getNewOrders().forEach {
                if(it.reqTime>0){
                     mainActivityVM.changeCountDownStatus(it.id,STARTED)
                }else{
                    mainActivityVM.changeCountDownStatus(it.id,NOREQTIME)
                }
            }
        })
    }

    private fun checkFirstFetch() {
        val first= Preferences.getPref("first","false",this)
        if (first!!.contentEquals("true")){
            System.out.println(" selam anina")
            mainActivityVM.getHashes()
            mainActivityVM.setInstallation(true)
        }
        else {
            System.out.println(" selam anina2")
            mainActivityVM.getHashes()
            mainActivityVM.dishFetched.observe(this, Observer { dish ->
                mainActivityVM.printerFetched.observe(this, Observer { printer ->
                    mainActivityVM.waitersFetched.observe(this, Observer { waiter ->
                        run {
                            if (dish && printer && waiter) {
                                if (first!!.contentEquals("false")) {
                                    mainActivityVM.setInstallation(true)
                                    Preferences.savePref("first", "true", this)
                                }

                            }
                            System.out.println("init Dish ${dish} ")
                            System.out.println("init Printer ${printer} ")
                            System.out.println("init Waiters ${waiter} ")
                        }
                    })
                })
            })
        }

        mainActivityVM.isInstallationComplete.observe(this, Observer {
            System.out.println(" naber lan ${it}")
        })
    }

    fun isTablet() : Boolean {
        return getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings -> showSettings()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSettings() {
        mainActivityVM.stopCountTimer()
        val intent = Intent(this, Splash::class.java)
        val bundle = Bundle()
        bundle.putBoolean("isMain", true)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()

        /* val dialog  = DialogUtil.bottom(R.layout.settings_layout,this)
           val ip : EditText? = dialog?.findViewById(R.id.ip)
           val folder : EditText? = dialog?.findViewById(R.id.folder)
           val login : EditText? = dialog?.findViewById(R.id.login)
           val password : EditText? = dialog?.findViewById(R.id.password)
           val ok: Button? = dialog?.findViewById(R.id.ok)
           val cancel : Button? = dialog?.findViewById(R.id.cancel)

           dialog?.show()

           val ipStr= Preferences.getPref("ip","",this)
           val folderStr= Preferences.getPref("folder","",this)
           val loginStr= Preferences.getPref("loginStr","",this)
           val passwordStr= Preferences.getPref("passwordStr","",this)

           ip?.text = ipStr?.toEditable()
           folder?.text = folderStr?.toEditable()
           login?.text = loginStr?.toEditable()
           password?.text = passwordStr?.toEditable()

           cancel?.setOnClickListener { dialog.dismiss() }
           ok?.setOnClickListener {
               dialog.dismiss()

               //  mainActivityVM.clear()
               Preferences.savePref("ip",ip?.text.toString(),this)
               Preferences.savePref("folder",folder?.text.toString(),this)
               Preferences.savePref("loginStr",login?.text.toString(),this)
               Preferences.savePref("passwordStr",password?.text.toString(),this)
               initAll()
           }*/
    }

    private fun initAll() {
        WorkManager.getInstance(this).cancelAllWork()
        mainActivityVM.setInstallation(false)
        mainActivityVM.setIsDishFetch(false)
        mainActivityVM.setIsWaiterFetch(false)
        mainActivityVM.setIsPrinterFetch(false)
        Preferences.savePref("first","false",this)
        Preferences.savePref("orderHash","",this)
        Preferences.savePref("catalogHash","",this)
        Preferences.savePref("timeStamp","",this)
        Preferences.savePref("waiter_token","",this)

        val room = RoomDb(this)

        room.DishesDAO().deleteAll().compose(RxSchedulers.DEFAULT.applyCompletable()).subscribe()
        room.KitchenDAO().deleteAll().compose(RxSchedulers.DEFAULT.applyCompletable()).subscribe()
        room.PrintersDAO().deleteAll().compose(RxSchedulers.DEFAULT.applyCompletable()).subscribe()
        room.WaiterDAO().deleteAll().compose(RxSchedulers.DEFAULT.applyCompletable()).subscribe()
        checkFirstFetch()
    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
}