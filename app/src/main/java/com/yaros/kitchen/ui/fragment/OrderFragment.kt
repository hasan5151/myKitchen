package com.yaros.kitchen.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.yaros.kitchen.R
import com.yaros.kitchen.adapter.*
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.apiModels.DishCookedModel
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.KitchenModel
import com.yaros.kitchen.room.entity.PrintersModel
import com.yaros.kitchen.utils.DateUtil
import com.yaros.kitchen.utils.DialogUtil
import com.yaros.kitchen.utils.DishCookedWM
import com.yaros.kitchen.utils.Preferences
import com.yaros.kitchen.viewModel.MainActivityVM
import com.yaros.kitchen.viewModel.PaginationVM
import com.yaros.kitchen.viewModel.factory.MainActivityFactory
import com.yaros.kitchen.viewModel.factory.PaginationFactory
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import java.util.HashMap

class OrderFragment  : BaseFragment(){
    lateinit var kitchen : RecyclerView
    lateinit var empty : TextView
    lateinit var chips : RecyclerView

    val printersHash: HashMap<String, PrintersModel> = HashMap()
    lateinit var paginationVM: PaginationVM
    lateinit var mainActivityVM: MainActivityVM
    val countDownHash: HashMap<Int, CountDownTimer> = HashMap()
    lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chips = view.findViewById(R.id.chips)
        kitchen = view.findViewById(R.id.kitchen)
        empty = view.findViewById(R.id.empty)
        progressBar = view.findViewById(R.id.progressBar)
        val paginationFactory =
            PaginationFactory(
                RoomDb(context!!), RxSchedulers.DEFAULT,
                Api(context!!).getApi()
            )
        paginationVM = ViewModelProvider(this, paginationFactory).get(PaginationVM::class.java)

        val mainActivityFactory =
            MainActivityFactory(
                RoomDb(context!!), RxSchedulers.DEFAULT,
                Api(context!!).getApi()
            )
        mainActivityVM =
            ViewModelProvider(activity!!, mainActivityFactory).get(MainActivityVM::class.java)

        isDataInitialize()
        observeIsFullScreen()

//        RoomDb(context!!).DishesDAO().updateCookingTime()
        stopCountDown()
    }

    private fun stopCountDown() {
        paginationVM.stopCountDown()
        paginationVM.stopCountDown.observe(viewLifecycleOwner, Observer {
            countDownHash.get(id)?.cancel()
            countDownHash.remove(id)
        })
    }

    private fun observeIsFullScreen() {
        mainActivityVM.isFullScreen.observe(activity!!, androidx.lifecycle.Observer {
            System.out.println("it oglu it")
            if (it){
                chips.visibility = View.GONE
            }else
                chips.visibility = View.VISIBLE
        })
    }

    private fun isDataInitialize() { //if init then get orders
        mainActivityVM.isInstallationComplete.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it){
                paginationVM.setApiService(Api(context!!))
                progressBar.visibility = View.GONE
                chips.visibility = View.VISIBLE

                setPrinters()
                setPrinterChips()
            }else{
                progressBar.visibility = View.VISIBLE
                chips.visibility = View.GONE
            }
        })

    }

    private fun setPrinterChips() {
        paginationVM.getCheckedPrinters()
        paginationVM.printerChipList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            setPrinterAdapter(getListOfChips(it+listOf(checkBoxAll(),checkBoxAdd()))) //dont change
        })
    }

    private fun setPrinters() {
        paginationVM.getPrinters()
        paginationVM.printersList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.forEach {
                printersHash.put(it.id,it)
            }
        })
    }

    private fun setPrinterAdapter(string : List<PrintersModel>) {
        val chipAdapter = object : PrinterAdapter(string) {
            override fun clickListener(chip: String?, pos: String) {

                val kitchenAdapter  = object : KitchenTopAdapter(context!!){
                    override fun setItemAdapter(recyclerView: RecyclerView?,item_order : String?) {
                        setSubAdapter(recyclerView,item_order)
                    }
                }

                if (!pos.contentEquals("-2")) {
                    paginationVM.getAllGroupByPrinter(pos)
                }else{
                    paginationVM.getAllGroupBy()
                }

                paginationVM.itemTopKitchen.observe(
                    viewLifecycleOwner,
                    androidx.lifecycle.Observer {
                        if (it.size > 0)
                            showEmpty(true)
                        else
                            showEmpty(false, "Заказов нет")
                        kitchenAdapter.submitList(it)
                    })
                kitchen.adapter =kitchenAdapter
                val linearLayoutManager = LinearLayoutManager(context!!)
                kitchen.layoutManager= linearLayoutManager
                kitchen.itemAnimator = SlideInRightAnimator()
            }

            override fun showDialog() {
                selectKitchens()
            }
        }

        val mLayoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        chips.layoutManager = mLayoutManager
        chips.adapter = chipAdapter
    }

    private fun setSubAdapter(recyclerView: RecyclerView?, item_order : String?){

        val kitchenSupAdapter = object : KitchenSupAdapter(context!!){
            override fun updateRemainTime(item: KitchenModel, milisUntilFinish: Long) {
                if (milisUntilFinish>0){
//                    paginationVM.updateElapsedTime("${milisUntilFinish}", item.id)
                }else{ // if it is 0 it means countDown is finished
//                         destroyCountDown(item.id) // this mean time is up
                }
            }

            override fun startCountDown(item: KitchenModel, countDownTimer: CountDownTimer) {
                countDownHash.put(item.id, countDownTimer)
            }

            override fun showPopup(item: KitchenModel, orderId: Int) {
                val dialog  = DialogUtil.bottom(R.layout.meal_ready_popup,context)
                val title: TextView = dialog!!.findViewById(R.id.title)
                val description: TextView = dialog.findViewById(R.id.description)
                val badge: TextView = dialog.findViewById(R.id.badge)
                val cancel: TextView = dialog.findViewById(R.id.cancel)
                val ok: TextView = dialog.findViewById(R.id.ok)
                title.text = item.name
                dialog.show()  //duplicate
                badge.text = "${item.count}"
                try {
                    description.text =
                        "№  ${item.number}    ${item?.waiterName}     ${DateUtil.getHourandMinute(
                            item.date.replace(" ", "").toLong()
                        )}    |   ${DateUtil.cookTimeDate(item.reqTime)}"
                }catch (e : NumberFormatException){
                    dialog.dismiss()
                }
                cancel.setOnClickListener {
                    dialog.dismiss()
                    stopReqCountDown(item.id)
                    destroyCountDown(item.id)
                    paginationVM.updateItemTimeKitchen(context.resources.getString(R.string.cancel),item.id)
                }

                ok.setOnClickListener({
                    dialog.dismiss()
                    stopReqCountDown(item.id)
                    destroyCountDown(item.id)
                    val serverTime = System.currentTimeMillis() + Preferences.getPref("diff", "0", context)?.toLong()!!
                    val dishCookedModel =
                        DishCookedModel(
                            "",
                            item?.order_item,
                            item.dish,
                            serverTime,
                            DateUtil.calculateCookingTime(
                                item.date,
                                Preferences.getPref("timeStamp", "", context)?.toLong()!!
                            ),item?.printerId
                        )
                    sendDishToServer(dishCookedModel)
                    paginationVM.updateItemTimeKitchen(context.resources.getString(R.string.ready),item.id)
                })
            }

            override fun hideOrder(item: KitchenModel) {
                destroyCountDown(item.id)
            }
        }


        paginationVM.getItemOrders2(item_order)
        paginationVM.itemSubKitchen2.observe(viewLifecycleOwner, Observer {
            kitchenSupAdapter.submitList(it)
        })
        recyclerView?.adapter =kitchenSupAdapter
        val linearLayoutManager = LinearLayoutManager(context!!)
        recyclerView?.layoutManager= linearLayoutManager
        recyclerView?.itemAnimator = SlideInRightAnimator()

    }

    private fun sendDishToServer(dishCookedModel: DishCookedModel){
        val data: Data = Data.Builder()
            .putString("orderId",dishCookedModel.order)
            .putString("dishId",dishCookedModel.dish)
            .putString("printer",dishCookedModel.printer)
            .putLong("cooking_date",dishCookedModel.cooking_date)
            .putLong("cooking_time",dishCookedModel.cooking_time)
            .build()
        mainActivityVM.setHistoryUpdate()
        val uploadPhotoRequest = OneTimeWorkRequest.Builder(DishCookedWM::class.java)
            .setInputData(data)
            .build()
        WorkManager.getInstance(context!!).enqueue(uploadPhotoRequest)
    }

    private fun stopReqCountDown(id : Int){
        countDownHash.get(id)?.cancel()
        countDownHash.remove(id)
    }

    private fun destroyCountDown(id : Int){
        object : CountDownTimer(1000, 1000) {
            override fun onFinish() {
                paginationVM.deleteItemKitchen(id)
            }

            override fun onTick(millisUntilFinished: Long) {
                println("%$millisUntilFinished")
            }
        }.start()
    }

    private fun selectKitchens(){
        val dialog  = DialogUtil.bottom(R.layout.select_kitchen,context!!)
        val recyclerView: RecyclerView = dialog!!.findViewById(R.id.items)
        val button : Button = dialog.findViewById(R.id.button)
        val valueList: List<PrintersModel> = ArrayList(printersHash.values)

        ArrayList(printersHash.values).filter { x->x.isChecked }.size.let {
            if(it>0){
                button.text = String.format("${context!!.resources.getString(R.string.select)} (${it})")
            }else
                button.text = context!!.resources.getString(R.string.select)
        }

        val check =  object: CheckBoxAdapter(valueList,context!!){
            override fun getSelectItems(isChecked: Boolean, printersModel: PrintersModel) {

                paginationVM.checkPrinter(isChecked,printersModel.id)

                printersHash.put(printersModel.id,printersModel)
                ArrayList(printersHash.values).filter { x->x.isChecked }.size.let {
                    if(it>0){
                        button.text = String.format("${context.resources.getString(R.string.select)} (${it})")
                    }else
                        button.text = context.resources.getString(R.string.select)
                }
            }
        }

        button.setOnClickListener {
            ArrayList(printersHash.values).filter { x->x.isChecked }.plus(checkBoxAll()).plus(checkBoxAdd()).let {
                setPrinterAdapter(it.distinct())
                showEmpty(it.size>1,resources.getString(R.string.selectKitchen))
            }
            dialog.dismiss()
        }
        recyclerView.adapter = check
        recyclerView.layoutManager = GridLayoutManager(context,1)

        dialog.show()
    }

    private fun getListOfChips(string : List<PrintersModel>): List<PrintersModel> {
        System.out.println("list Size ${string.size}")
        showEmpty(string.size>1,resources.getString(R.string.selectKitchen))
        return string
    }

    private fun showEmpty(hasItem : Boolean, string: String=""){
        System.out.println("no item ${hasItem}")
        empty.text=string
        if (hasItem) {
            kitchen.visibility = View.VISIBLE
            empty.visibility = View.INVISIBLE
        } else{
            kitchen.visibility = View.INVISIBLE
            empty.visibility = View.VISIBLE
        }
    }

    override fun getName(): String = "Заказы"
    override fun getDrawable(): Int = R.drawable.order
    private fun  checkBoxAdd() : PrintersModel = PrintersModel("-1","add",false)
    private fun  checkBoxAll() : PrintersModel = PrintersModel("-2","Все",false)
}