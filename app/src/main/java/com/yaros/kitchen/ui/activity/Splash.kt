package com.yaros.kitchen.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.yaros.kitchen.BuildConfig
import com.yaros.kitchen.R
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.api.TokenService
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.utils.*
import com.yaros.kitchen.utils.CatalogWM.Companion.WAITERS
import java.util.*

class Splash : AppCompatActivity() {
    lateinit var progressBar : ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)
        WorkManager.getInstance(this).cancelAllWork()


        val ip : EditText? = findViewById(R.id.ip)
        val folder : EditText? = findViewById(R.id.folder)
        val login : EditText? = findViewById(R.id.login)
        val password : EditText? = findViewById(R.id.password)
        val ok: Button? = findViewById(R.id.ok)
        val cancel : Button? = findViewById(R.id.cancel)
        progressBar  = findViewById(R.id.progressBar)

        val bundle = intent.extras

        val ipStr= Preferences.getPref("ip","",this)
        val folderStr= Preferences.getPref("folder","",this)
        val loginStr= Preferences.getPref("loginStr","",this)
        val passwordStr= Preferences.getPref("passwordStr","",this)

        if (bundle?.getBoolean("isMain",false)!=true)
        if (!ipStr?.contentEquals("")!!
            &&!folderStr?.contentEquals("")!!
            &&!loginStr?.contentEquals("")!!
            &&!passwordStr?.contentEquals("")!!){
            goMain()
        }

        ip?.text = ipStr?.toEditable()
        folder?.text = folderStr?.toEditable()
        login?.text = loginStr?.toEditable()
        password?.text = passwordStr?.toEditable()

        cancel?.setOnClickListener {
            if (bundle?.getBoolean("isMain",false)==true){
                goMain()
            }
        }
        ok?.setOnClickListener {
            //  mainActivityVM.clear()
            progressBar?.visibility = View.VISIBLE
            Preferences.savePref("ip",ip?.text.toString(),this)
            Preferences.savePref("folder",folder?.text.toString(),this)
            Preferences.savePref("loginStr",login?.text.toString(),this)
            Preferences.savePref("passwordStr",password?.text.toString(),this)
            initAll()
        }
    }

    private fun initAll() {
        WorkManager.getInstance(this).cancelAllWork()
        Preferences.savePref("first","false",this)
        Preferences.savePref("orderHash","",this)
        Preferences.savePref("catalogHash","",this)
        Preferences.savePref("timeStamp","",this)
        Preferences.savePref("waiter_token","",this)

        val room = RoomDb(this)

        room.DishesDAO().deleteAll().compose(RxSchedulers.DEFAULT.applyCompletable()).subscribe()
        room.KitchenDAO().deleteAll().compose(RxSchedulers.DEFAULT.applyCompletable()).subscribe()
        room.PrintersDAO().deleteAll().compose(RxSchedulers.DEFAULT.applyCompletable()).subscribe()
        room.WaiterDAO().deleteAll().compose(RxSchedulers.DEFAULT.applyCompletable()).subscribe({
                sendToServer(WAITERS)
        },{})
    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    private fun sendToServer(type: Int){
        System.out.println("selam delete me")
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
            .observe(this, androidx.lifecycle.Observer { workInfo: WorkInfo ->
                System.out.println("selam napion an1 ${workInfo.state}")
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val data = workInfo.outputData
                    val type= data.getInt("type",-1)
                    System.out.println("selam napion an2 ${type}")
                    when(type){
                        WAITERS -> {
                            RoomDb(this).WaiterDAO().isWaitersCreated().observe(this, androidx.lifecycle.Observer {
                                if (it){
                                    val waiterId = RoomDb(this).WaiterDAO().getWaiter().id
                                        TokenService(this).getApi()
                                            .login(waiterId,"123",DeviceName.get(), "mobile_kitchen",
                                                UniqueId.get(this), BuildConfig.VERSION_NAME, IpAddress(this).fromWifi())
                                            ?.compose(RxSchedulers.DEFAULT.applyObservable())
                                            ?.filter { it?.meta?.code?.contentEquals("0")!! }
                                            ?.subscribe({
                                                Preferences.savePref("waiter_token",it?.data?.waiter_token,this)

                                                progressBar?.visibility = View.GONE
                                                goMain()

                                            },{
                                                it.printStackTrace()
                                            })
                                }
                            })
                        }
                    }
                }
            })
    }

    private fun goMain(){
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}