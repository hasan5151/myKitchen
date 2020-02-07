package com.yaros.kitchen.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.yaros.kitchen.R
import com.yaros.kitchen.adapter.MenuAdapter
import com.yaros.kitchen.adapter.MenuItemAdapter
import com.yaros.kitchen.adapter.SearchAdapter
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.DishesModel
import com.yaros.kitchen.room.entity.PrintersModel
import com.yaros.kitchen.utils.DialogUtil
import com.yaros.kitchen.utils.TVDrawable
import com.yaros.kitchen.viewModel.factory.PaginationFactory
import com.yaros.kitchen.viewModel.PaginationVM

class MenuFragment : BaseFragment() {
    override fun getName(): String = "Меню"
    override fun getDrawable(): Int = 0

    lateinit var searchRV : RecyclerView
    lateinit var menuRV: RecyclerView
    lateinit var paginationVM: PaginationVM
    lateinit var snackbarView : View
    var searchList = listOf(PrintersModel("-1","Все блюда",false))
    lateinit var tvDrawable: TVDrawable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.menu_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvDrawable = TVDrawable(context!!)
        searchRV = view.findViewById(R.id.searchRV)
        menuRV= view.findViewById(R.id.menuRV)
        snackbarView= view.findViewById(R.id.snackbarView)

        val paginationFactory =
            PaginationFactory(
                RoomDb(context!!), RxSchedulers.DEFAULT,
                Api(context!!).getApi()
            )
        paginationVM = ViewModelProvider(this,paginationFactory).get(PaginationVM::class.java)

        setSearchRV()
        setMenuRV()
    }

    private fun setMenuRV() {
        paginationVM.getPrinters()
        paginationVM.printersList.observe(viewLifecycleOwner, Observer {
            object: MenuAdapter(it,context!!){
                override fun onClick(printer: PrintersModel) {
                    searchList = searchList + listOf(printer)
                    setSearchRV()
                    setMenuItemRV()
                }
            }.let { menuRV.adapter = it }

            menuRV.layoutManager = GridLayoutManager(context!!,2)
        })
    }

    private fun setSearchRV() {
        System.out.println("search size ${searchList.size}")
        object  : SearchAdapter(searchList){
            override fun onClick(printersModel: PrintersModel,position : Int) {
                if (position==0){
                    setMenuRV()

                }else{
                    setMenuItemRV()
                }
                searchList=  searchList.dropLast(searchList.size-position-1)
                setSearchRV()

            }
        }.let { searchRV.adapter = it }
        searchRV.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setMenuItemRV() {
        paginationVM.getAllDishes()
        paginationVM.dishesInfo.observe(viewLifecycleOwner, Observer {
            object : MenuItemAdapter(it){
                override fun onClick(dish: DishesModel) {
                    showPopup(dish)
                }
            }.let {
                menuRV.adapter = it
            }
        })
    }

    private fun showPopup(dish: DishesModel) {
        val dialog  = DialogUtil.bottom(R.layout.menu_main_popup,context!!)
        val name: TextView = dialog!!.findViewById(R.id.name)
        val stopOrder: TextView = dialog.findViewById(R.id.stopOrder)
        val indicateCount: TextView = dialog.findViewById(R.id.indicateCount)
        name.text = dish.name

        tvDrawable.drawSize(stopOrder,R.drawable.keyboard_arrow_rightt,0.35,false)
        tvDrawable.drawSize(indicateCount,R.drawable.keyboard_arrow_rightt,0.35,false )

        stopOrder.setOnClickListener({
            stopOrderPopup(dish)
            dialog.dismiss()
        })
        indicateCount.setOnClickListener({
            indicateCount(dish)
            dialog.dismiss()
        })

        dialog.show()
    }

    private fun indicateCount(dish: DishesModel) {
        val dialog  = DialogUtil.bottom(R.layout.indicate_balance_popup,context!!)
        dialog?.show()
        val name: TextView = dialog!!.findViewById(R.id.name)
        name.text = dish.name
        val cancel: TextView = dialog.findViewById(R.id.cancel)
        val ok: TextView = dialog.findViewById(R.id.ok)
        val turnback: TextView = dialog.findViewById(R.id.turnback)
        val increase: TextView = dialog.findViewById(R.id.increase)
        val decrease: TextView = dialog.findViewById(R.id.decrease)
        val count: TextView = dialog.findViewById(R.id.count)

        tvDrawable.drawSize(turnback,R.drawable.arrow_backk,0.90)

        turnback.setOnClickListener({
            showPopup(dish)
            dialog.dismiss()
        })

        increase.setOnClickListener({
            var countInt = count.text.toString().toInt()
            countInt = countInt +1
            count.text = "${countInt}"
        })

        decrease.setOnClickListener({
            var countInt = count.text.toString().toInt()
            if (countInt!=0)
                countInt = countInt - 1
            count.text = "${countInt}"
        })

        cancel.setOnClickListener({
            dialog.dismiss()
        })

        ok.setOnClickListener {
            var countInt = count.text.toString().toInt()
            dialog.dismiss()
            val snackbar = Snackbar.make(snackbarView, "${dish.name} -  ${context!!.resources.getString(R.string.remain)} ${countInt}", Snackbar.LENGTH_LONG)
            snackbar.anchorView = snackbarView
            snackbar.show() //TODO send server indicate count
        }
    }

    private fun stopOrderPopup(dish: DishesModel) {
        val dialog  = DialogUtil.bottom(R.layout.stop_order_popup,context!!)
        dialog?.show()
        val name: TextView = dialog!!.findViewById(R.id.name)
        val fifteenmin: TextView = dialog.findViewById(R.id.fifteenmin)
        val thirtymin: TextView = dialog.findViewById(R.id.thirtymin)
        val onehour: TextView = dialog.findViewById(R.id.onehour)
        val turnback: TextView = dialog.findViewById(R.id.turnback)
        val indicateTime : TextView = dialog.findViewById(R.id.indicateTime)
        val cancel: TextView = dialog.findViewById(R.id.cancel)
        val ok: TextView = dialog.findViewById(R.id.ok)

        tvDrawable.drawSize(turnback,R.drawable.arrow_backk,0.90)

        name.text = dish.name
        var time = "0"

        fifteenmin.setOnClickListener({
            fifteenmin.setTextColor(ContextCompat.getColor(context!!,R.color.colorPrimary))
            thirtymin.setTextColor(ContextCompat.getColor(context!!,R.color.popupcolor))
            onehour.setTextColor(ContextCompat.getColor(context!!,R.color.popupcolor))
            time = context!!.resources.getString(R.string.fifteenmin)
            ok.visibility = View.VISIBLE
        })

        thirtymin.setOnClickListener({
            thirtymin.setTextColor(ContextCompat.getColor(context!!,R.color.colorPrimary))
            fifteenmin.setTextColor(ContextCompat.getColor(context!!,R.color.popupcolor))
            onehour.setTextColor(ContextCompat.getColor(context!!,R.color.popupcolor))
            time = context!!.resources.getString(R.string.thirtymin)
            ok.visibility = View.VISIBLE
        })

        onehour.setOnClickListener({
            onehour.setTextColor(ContextCompat.getColor(context!!,R.color.colorPrimary))
            fifteenmin.setTextColor(ContextCompat.getColor(context!!,R.color.popupcolor))
            thirtymin.setTextColor(ContextCompat.getColor(context!!,R.color.popupcolor))
            time = context!!.resources.getString(R.string.onehour)
            ok.visibility = View.VISIBLE
        })

        indicateTime.setOnClickListener({
            dialog.dismiss()
            setStopInTime(dish)
        })

        turnback.setOnClickListener({
            showPopup(dish)
            dialog.dismiss()
        })

        cancel.setOnClickListener({
            dialog.dismiss()
        })

        ok.setOnClickListener {
            dialog.dismiss()
             val snackbar = Snackbar.make(snackbarView, "${dish.name} -  ${context!!.resources.getString(R.string.stopText)} ${time}", Snackbar.LENGTH_LONG)
            snackbar.anchorView = snackbarView
            snackbar.show() //TODO send server 15min 30min or 1hour
        }
    }

    private fun setStopInTime(dish: DishesModel) {
        val dialog  = DialogUtil.bottom(R.layout.stop_order_intime_popup,context!!)
        dialog?.show()
        val name: TextView = dialog!!.findViewById(R.id.name)
        val cancel: TextView = dialog.findViewById(R.id.cancel)
        val turnback: TextView = dialog.findViewById(R.id.turnback)
        val ok: TextView = dialog.findViewById(R.id.ok)
        val timePicker: TimePicker = dialog.findViewById(R.id.timePicker)
        name.text = dish.name

        tvDrawable.drawSize(turnback,R.drawable.arrow_backk,0.90)

        turnback.setOnClickListener({
            showPopup(dish)
            dialog.dismiss()
        })

        timePicker.setIs24HourView(true)
        var hour =0
        var min = 0
        timePicker.setOnTimeChangedListener({picker, i, i2 ->
            hour = i
            min = i2

            System.out.println("hour ${hour} min ${min}")
        })

        cancel.setOnClickListener({
            dialog.dismiss()
        })

        ok.setOnClickListener {
            dialog.dismiss()
            val snackbar = Snackbar.make(snackbarView, "${dish.name} -  ${context!!.resources.getString(R.string.stopTimeText)} ${hour}:${min}", Snackbar.LENGTH_LONG)
            snackbar.anchorView = snackbarView
            snackbar.show() //TODO send server indicate time
        }
    }
}