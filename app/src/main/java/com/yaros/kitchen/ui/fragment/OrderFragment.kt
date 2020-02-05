package com.yaros.kitchen.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.yaros.kitchen.R
import com.yaros.kitchen.adapter.CheckBoxAdapter
import com.yaros.kitchen.adapter.ItemPageAdapter
import com.yaros.kitchen.adapter.OrderPageAdapter
import com.yaros.kitchen.adapter.PrinterAdapter
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.apiModels.DishCookedModel
import com.yaros.kitchen.room.entity.PrintersModel
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.KitchenItemModel
import com.yaros.kitchen.room.entity.KitchenOrderModel
import com.yaros.kitchen.utils.DateUtil
import com.yaros.kitchen.utils.DialogUtil
import com.yaros.kitchen.utils.MyWorkManager
import com.yaros.kitchen.utils.Preferences
import com.yaros.kitchen.viewModel.PaginationFactory
import com.yaros.kitchen.viewModel.PaginationVM
import java.util.*
import kotlin.collections.ArrayList


class OrderFragment : BaseFragment(){
    lateinit var kitchen : RecyclerView
    lateinit var empty : TextView
    lateinit var chips : RecyclerView

    val printersHash:HashMap<String, PrintersModel> = HashMap()
    lateinit var paginationVM: PaginationVM
    lateinit var printerList: List<String>
    val countDownHash: HashMap<Int, CountDownTimer> = HashMap()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chips  = view.findViewById(R.id.chips)
        kitchen  = view.findViewById(R.id.kitchen)
        empty = view.findViewById(R.id.empty)

        val paginationFactory = PaginationFactory(RoomDb(context!!), RxSchedulers.DEFAULT,
            Api(context!!).getApi()
        )
        paginationVM = ViewModelProvider(this,paginationFactory).get(PaginationVM::class.java)
        paginationVM.loadOrders()

        isDataInitialize()
        setHash() //dont change
        setPrinterAdapter(getListOfChips(listOf(checkBoxAdd()))) //dont change
    }

    private fun isDataInitialize() { //if init then get orders
        paginationVM.checkDishes()
        paginationVM.checkWaiters()
        paginationVM.checkPrinters()
        paginationVM.isDishesCreated.observe(viewLifecycleOwner, androidx.lifecycle.Observer {dish->
            paginationVM.isWaitersCreated.observe(viewLifecycleOwner, androidx.lifecycle.Observer {waiters->
                paginationVM.isPrintersCreated.observe(viewLifecycleOwner, androidx.lifecycle.Observer {printers->
                    if (dish&&waiters&&printers){
                        setPrinters()
                    }else{
                        //show loading bar
                    }
                })
            })
        })
    }

    private fun setPrinters() {
        paginationVM.getPrinters()
         paginationVM.printersList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.map { it.id }.let {
                printerList = it
                paginationVM.getOrderItems(it,1569867821000,1579867821000)//TODO change dates
            }
            it.forEach {
                printersHash.put(it.id,it)
            }
        })
    }

    private fun setPrinterAdapter(string : List<PrintersModel>) {
        val chipAdapter = object : PrinterAdapter(string) {
            override fun clickListener(chip: String?, pos: String) {
                    val orderAdapter =object :  OrderPageAdapter(){
                        override fun setItemAdapter(recyclerView: RecyclerView, orderModel:KitchenOrderModel?) {
                            itemAdapter(recyclerView,orderModel)
                        }
                    }
                 paginationVM.loadOrdersByPrinter(pos!!)
                    paginationVM.order.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                        if (it.size>0)
                            showEmpty(true)
                        else
                            showEmpty(false,"Заказов нет")
                        orderAdapter.submitList(it)
                    })
                    kitchen.adapter =orderAdapter
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

    fun itemAdapter(recyclerView: RecyclerView, orderModel: KitchenOrderModel?){
        val itemPageAdapter =  object  : ItemPageAdapter(context!!) {
            override fun updateRemainTime(item: KitchenItemModel, milisUntilFinish: Long) {
                if (milisUntilFinish>0){
//                    paginationVM.updateElapsedTime("${milisUntilFinish}", item.id)
                }else{
//                    paginationVM.updateElapsedTime("${0}", item.id)
                    destroyCountDown(item.id)
                    countDownHash.remove(item.id)
                }
            }

            override fun startCountDown(item: KitchenItemModel, countDownTimer: CountDownTimer) {
                //   paginationVM.startCountDown(item.id)
                countDownHash.put(item.id, countDownTimer)
            }

            override fun showPopup(item: KitchenItemModel,orderId : Int) {
                val dialog  = DialogUtil.bottom(R.layout.meal_ready_popup,context)
                val title: TextView = dialog!!.findViewById(R.id.title)
                val description: TextView = dialog.findViewById(R.id.description)
                val badge: TextView = dialog.findViewById(R.id.badge)
                val cancel: TextView = dialog.findViewById(R.id.cancel)
                val ok: TextView = dialog.findViewById(R.id.ok)
                title.text = item.name
                dialog.show()  //duplicate
                badge.text = "${item.count}"
                description.text = "№  ${item.number}    ${orderModel?.waiterName}     ${DateUtil.getHourandMinute(item.date.replace(" ","").toLong())}    |   ${DateUtil.cookTimeDate(item.reqTime)}"

                cancel.setOnClickListener {
                    dialog.dismiss()
                    stopReqCountDown(item.id)
                    destroyCountDown(item.id)
                    paginationVM.updateItemTime(context.resources.getString(R.string.cancel),item.id)
                }

                ok.setOnClickListener({
                    dialog.dismiss()
                    stopReqCountDown(item.id)
                    destroyCountDown(item.id)
                    val serverTime = System.currentTimeMillis() +Preferences.getPref("diff", "0", context)?.toLong()!!
                    val dishCookedModel =
                        DishCookedModel(
                            "",
                            orderModel?.order_item,
                            item.dish,
                            serverTime, //now //this is not now always
                            DateUtil.calculateCookingTime(
                                item.date,
                                Preferences.getPref("timeStamp", "", context)?.toLong()!!
                            ),orderModel?.printerId
                        )
                    sendDishToServer(dishCookedModel)
                    paginationVM.updateItemTime(context.resources.getString(R.string.ready),item.id)
            })
            }
        }

        paginationVM.loadItemsByOrderId(orderModel!!.order_item)
        paginationVM.item.observe(this, androidx.lifecycle.Observer {
            System.out.println("naber size ${it.size}")
            if (it.size==0) //If order has 0 item, then also order itself
                paginationVM.deleteOrderById(orderModel.order_item)
            itemPageAdapter.submitList(it)
        })
        recyclerView.adapter = itemPageAdapter
    }

     private fun sendDishToServer(dishCookedModel: DishCookedModel){
        val data: Data = Data.Builder()
            .putString("orderId",dishCookedModel.order)
            .putString("dishId",dishCookedModel.dish)
            .putString("printer",dishCookedModel.order)
            .putLong("cooking_date",dishCookedModel.cooking_date)
            .putLong("cooking_time",dishCookedModel.cooking_time)
            .build()

        val uploadPhotoRequest = OneTimeWorkRequest.Builder(MyWorkManager::class.java)
            .setInputData(data)
         //   .addTag("cookedDish")
            .build()
        WorkManager.getInstance(context!!).enqueue(uploadPhotoRequest)
    }

    private fun stopReqCountDown(id : Int){
        countDownHash.get(id)?.cancel()
        countDownHash.remove(id)
    }

    private fun destroyCountDown(id : Int){
        object : CountDownTimer(2000, 1000) {
            override fun onFinish() {
                paginationVM.deleteItemById(id)
            }

            override fun onTick(millisUntilFinished: Long) {
                println("%$millisUntilFinished")
            }
        }.start()
    }

    private fun selectKitchens(){
        val dialog  = DialogUtil.bottom(R.layout.select_kitchen,context!!)
        val recyclerView:RecyclerView= dialog!!.findViewById(R.id.items)
        val button :Button= dialog.findViewById(R.id.button)
        val valueList: List<PrintersModel> = ArrayList(printersHash.values)

        ArrayList(printersHash.values).filter { x->x.isChecked }.size.let {
            if(it>0){
                button.text = String.format("${context!!.resources.getString(R.string.select)} (${it})")
            }else
                button.text = context!!.resources.getString(R.string.select)
        }

        val check =  object: CheckBoxAdapter(valueList,context!!){
            override fun getSelectItems(isChecked: Boolean, printersModel: PrintersModel) {
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
            ArrayList(printersHash.values).filter { x->x.isChecked }.plus(checkBoxAdd()).let {
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
    fun  checkBoxAdd() : PrintersModel = PrintersModel("-1","add",false)


    private fun setHash() {
        paginationVM.getHashes()
        paginationVM.hash.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val hashIt = it
            val oldTimeStamp = Preferences.getPref("timeStamp","",context)
            if(!oldTimeStamp!!.contentEquals(it.time_server.toString())){
                Preferences.savePref("timeStamp","${it.time_server}",context)
                val diff = System.currentTimeMillis()-it.time_server
                Preferences.savePref("diff","${diff}",context)
                System.out.println("timeHash ${it.time_server}")
            }

            val oldCatalogHash= Preferences.getPref("catalogHash","",context)
            if(!oldCatalogHash!!.contentEquals(it.catalog_hash.toString())){
                Preferences.savePref("catalogHash",it.catalog_hash,context)
                System.out.println("catalogHash ${it.catalog_hash}")
                paginationVM.fetchPrinters()
                paginationVM.fetchDishes()
                paginationVM.fetchWaiters()
            }

            val oldOrderHash = Preferences.getPref("orderHash","",context)
            if(!oldOrderHash!!.contentEquals(it.orders_hash.toString())){
                Preferences.savePref("orderHash",it.orders_hash,context)
                System.out.println("orderHash ${it.orders_hash}")
                if (printerList.size>0)
                    paginationVM.getOrderItems(printerList)
            }
        })
    }
}