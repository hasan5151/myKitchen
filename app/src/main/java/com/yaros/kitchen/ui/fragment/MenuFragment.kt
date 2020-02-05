package com.yaros.kitchen.ui.fragment

import android.app.Dialog
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.yaros.kitchen.viewModel.PaginationFactory
import com.yaros.kitchen.viewModel.PaginationVM
import kotlinx.android.synthetic.main.kitchen_order_adapter.view.*
import kotlinx.android.synthetic.main.search_adapter.view.*

class MenuFragment : BaseFragment() {
    override fun getName(): String = "Меню"
    override fun getDrawable(): Int = 0

    lateinit var searchRV : RecyclerView
    lateinit var menuRV: RecyclerView
    lateinit var paginationVM: PaginationVM
    var searchList = listOf(PrintersModel("-1","Все блюда",false))


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.menu_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        searchRV = view.findViewById(R.id.searchRV)
        menuRV= view.findViewById(R.id.menuRV)

        val paginationFactory = PaginationFactory(
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
                    setMemnuItemRV()
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
                    setMemnuItemRV()
                }
                searchList=  searchList.dropLast(searchList.size-position-1)
                setSearchRV()

            }
        }.let { searchRV.adapter = it }
        searchRV.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setMemnuItemRV() {
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
        val stopOrder: TextView = dialog!!.findViewById(R.id.stopOrder)
        val indicateBalance: TextView = dialog!!.findViewById(R.id.indicateBalance)
        name.text = dish.name

        stopOrder.setOnClickListener({
            stopOrderPopup(dish)
            dialog.dismiss()
        })
        indicateBalance.setOnClickListener({
            indicateBalancePopup(dish)
            dialog.dismiss()
        })

        dialog?.show()
    }

    private fun indicateBalancePopup(dish: DishesModel) {
        val dialog  = DialogUtil.bottom(R.layout.indicate_balance_popup,context!!)
        dialog?.show()
    }

    private fun stopOrderPopup(dish: DishesModel) {
        val dialog  = DialogUtil.bottom(R.layout.stop_order_popup,context!!)
        dialog?.show()
        val name: TextView = dialog!!.findViewById(R.id.name)
        val fifteenmin: TextView = dialog!!.findViewById(R.id.fifteenmin)
        val thirtymin: TextView = dialog!!.findViewById(R.id.thirtymin)
        val onehour: TextView = dialog!!.findViewById(R.id.onehour)
        val turnback: TextView = dialog!!.findViewById(R.id.turnback)
        val indicateTime : TextView = dialog!!.findViewById(R.id.indicateTime)
        val cancel: TextView = dialog!!.findViewById(R.id.cancel)
        val ok: TextView = dialog!!.findViewById(R.id.ok)

        name.text = dish.name

        fifteenmin.setOnClickListener({
            fifteenmin.setTextColor(ContextCompat.getColor(context!!,R.color.colorPrimary))
            thirtymin.setTextColor(ContextCompat.getColor(context!!,R.color.popupcolor))
            onehour.setTextColor(ContextCompat.getColor(context!!,R.color.popupcolor))
            ok.visibility = View.VISIBLE
        })

        thirtymin.setOnClickListener({
            thirtymin.setTextColor(ContextCompat.getColor(context!!,R.color.colorPrimary))
            fifteenmin.setTextColor(ContextCompat.getColor(context!!,R.color.popupcolor))
            onehour.setTextColor(ContextCompat.getColor(context!!,R.color.popupcolor))
            ok.visibility = View.VISIBLE
        })


        onehour.setOnClickListener({
            onehour.setTextColor(ContextCompat.getColor(context!!,R.color.colorPrimary))
            fifteenmin.setTextColor(ContextCompat.getColor(context!!,R.color.popupcolor))
            thirtymin.setTextColor(ContextCompat.getColor(context!!,R.color.popupcolor))
            ok.visibility = View.VISIBLE
        })

        turnback.setOnClickListener({
            showPopup(dish)
            dialog.dismiss()
        })

        cancel.setOnClickListener({
            dialog.dismiss()
        })

        ok.setOnClickListener({
            setStopOrder(dialog)
        })

    }

    private fun setStopOrder(dialog: Dialog) {
        //TODO send server info
        //TODO update adapter
    }
}