package com.yaros.kitchen.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yaros.kitchen.R
import com.yaros.kitchen.adapter.*
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.PrintersModel
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.KitchenItemModel
import com.yaros.kitchen.room.entity.KitchenOrderModel
import com.yaros.kitchen.utils.DialogUtil
import com.yaros.kitchen.viewModel.PaginationFactory
import com.yaros.kitchen.viewModel.PaginationVM
import java.util.*
import kotlin.collections.ArrayList

class OrderFragment : BaseFragment(){

    lateinit var kitchen : RecyclerView
    lateinit var empty : TextView
    lateinit var chips : RecyclerView
    val printersHash:HashMap<Int, PrintersModel> = HashMap()
    lateinit var paginationVM: PaginationVM
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
            Api("","",context!!).getApi()
        )
        paginationVM = ViewModelProviders.of(this,paginationFactory).get(PaginationVM::class.java)

        paginationVM.fetchOrders()
        paginationVM.fetchItems()

        paginationVM.loadOrders()

        setTypeOfKitchens()
        setPrinterAdapter(getListOfChips(listOf(checkBoxAdd())))
    }

    private fun setTypeOfKitchens() {
        val checkBoxModel = PrintersModel()
        checkBoxModel.isChecked=false
        checkBoxModel.id= 0
        checkBoxModel.name = "Test"

        val checkBoxModel2 = PrintersModel()
        checkBoxModel2.isChecked=false
        checkBoxModel2.id= 1
        checkBoxModel2.name = "Test2"

        val checkBoxModel3 = PrintersModel()
        checkBoxModel3.isChecked=false
        checkBoxModel3.id= 2
        checkBoxModel3.name = "Test3"

        printersHash.put(0,checkBoxModel)
        printersHash.put(1,checkBoxModel2)
        printersHash.put(2,checkBoxModel3)
    }

    private fun setPrinterAdapter(string : List<PrintersModel>) {

        val chipAdapter = object : PrinterAdapter(string) {
            override fun clickListener(chip: String?, pos: Int) {
                //show orders
                if (pos==1){
                     val orderAdapter =object :  OrderPageAdapter(){
                        override fun setItemAdapter(recyclerView: RecyclerView, orderModel:KitchenOrderModel?) {
                            itemAdapter(recyclerView,orderModel)
                        }
                     }

                    paginationVM.order.observe(this@OrderFragment, androidx.lifecycle.Observer {
                        if (it.size>0)
                            showEmpty(true)
                        else
                            showEmpty(false,"Заказов нет")

                        orderAdapter.submitList(it)
                    })
                    kitchen.adapter =orderAdapter
                }else
                    showEmpty(false,"Заказов нет ")
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
                    paginationVM.updateElapsedTime("${milisUntilFinish}", item.id)
                }else{
                    destroyCountDown(item.id)
                    countDownHash.remove(item.id)
                }
            }

            override fun startCountDown(item: KitchenItemModel, countDownTimer: CountDownTimer) {
                paginationVM.startCountDown(item.id)
                countDownHash.put(item.id, countDownTimer)
             }

            override fun showPopup(item: KitchenItemModel,orderId : Int) {
                val dialog  = DialogUtil.bottomConstraint(R.layout.meal_ready_popup,context)
                val title: TextView = dialog!!.findViewById(R.id.title)
                val description: TextView = dialog.findViewById(R.id.description)
                val badge: TextView = dialog.findViewById(R.id.badge)
                val cancel: TextView = dialog.findViewById(R.id.cancel)
                val ok: TextView = dialog.findViewById(R.id.ok)
                title.text = item.name
                dialog.show()  //duplicate
                badge.text = "${item.count}"
                description.text = "№  ${item.number}    ${orderModel?.waiterName}     ${item.date}    |   ${item.reqTime}"

                cancel.setOnClickListener {
                    paginationVM.updateItemTime(context.resources.getString(R.string.cancel),item.id)
                    dialog.dismiss()
                    stopReqCountDown(item.id)
                    destroyCountDown(item.id)
                }

                ok.setOnClickListener({
                    paginationVM.updateItemTime(context.resources.getString(R.string.ready),item.id)
                    dialog.dismiss()
                    stopReqCountDown(item.id)
                    destroyCountDown(item.id)
                })
            }
        }
        paginationVM.loadItemsByOrderId(orderModel?.order_item!!) //TODO needs to update with api
        paginationVM.item.observe(this, androidx.lifecycle.Observer {
            if (it.size==0) //If order has 0 item, then also order itself
                paginationVM.deleteOrderById(orderModel.order_item)
            itemPageAdapter.submitList(it)
        })

        recyclerView.adapter = itemPageAdapter
    }

    private fun stopReqCountDown(id : Int){
        System.out.println("stopReqCountDown size of ${countDownHash.keys.size}")
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
                button.text = String.format("${context!!.resources.getString(R.string.selectKitchen)} (${it})")
            }else
                button.text = context!!.resources.getString(R.string.selectKitchen)
        }

        val check =  object: CheckBoxAdapter(valueList,context!!){
            override fun getSelectItems(isChecked: Boolean, printersModel: PrintersModel) {
                printersHash.put(printersModel.id,printersModel)
                ArrayList(printersHash.values).filter { x->x.isChecked }.size.let {
                    if(it>0){
                        button.text = String.format("${context.resources.getString(R.string.selectKitchen)} (${it})")
                    }else
                        button.text = context.resources.getString(R.string.selectKitchen)
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

    fun  checkBoxAdd() : PrintersModel {
        val checkBoxModel = PrintersModel()
        checkBoxModel.isChecked = false
        checkBoxModel.id = -1
        checkBoxModel.name= "add"
        return  checkBoxModel
    }
}