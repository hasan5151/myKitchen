package com.yaros.kitchen.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yaros.kitchen.R
import com.yaros.kitchen.adapter.CheckBoxAdapter
import com.yaros.kitchen.adapter.ChipAdapter
import com.yaros.kitchen.adapter.KitchenOrderAdapter
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.RxSingleSchedulers
import com.yaros.kitchen.models.CheckBoxModel
import com.yaros.kitchen.models.KitchenOrderModel
import com.yaros.kitchen.models.KitchenItemModel
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.utils.DialogUtil
import com.yaros.kitchen.viewModel.PaginationFactory
import com.yaros.kitchen.viewModel.PaginationVM
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.arrayListOf
import kotlin.collections.distinct
import kotlin.collections.listOf

class OrderFragment : BaseFragment(){

    lateinit var kitchen : RecyclerView
    lateinit var empty : TextView
    lateinit var chips : RecyclerView
    val checkBoxHash:HashMap<Int,CheckBoxModel> = HashMap()
    lateinit var paginationVM: PaginationVM
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
        val paginationFactory = PaginationFactory(RoomDb(context!!), RxSingleSchedulers.DEFAULT,
            Api("","",context!!).getApi()
        )
        paginationVM = ViewModelProviders.of(this,paginationFactory).get(PaginationVM::class.java)

        paginationVM.loadOrders()
        paginationVM.order.observe(this, androidx.lifecycle.Observer {
            System.out.println("sdfsdf")
        })

        setTypeOfKitchens()
        setChipAdapter(getListOfChips(listOf(checkBoxAdd())))
    }

    private fun setTypeOfKitchens() {
        val checkBoxModel = CheckBoxModel()
        checkBoxModel.isChecked=false
        checkBoxModel.id= 0
        checkBoxModel.name = "Test"

        val checkBoxModel2 = CheckBoxModel()
        checkBoxModel2.isChecked=false
        checkBoxModel2.id= 1
        checkBoxModel2.name = "Test2"

        val checkBoxModel3 = CheckBoxModel()
        checkBoxModel3.isChecked=false
        checkBoxModel3.id= 2
        checkBoxModel3.name = "Test3"

        checkBoxHash.put(0,checkBoxModel)
        checkBoxHash.put(1,checkBoxModel2)
        checkBoxHash.put(2,checkBoxModel3)
    }

    private fun setChipAdapter(string : List<CheckBoxModel>) {
        val chipAdapter = object : ChipAdapter(string) {
            override fun clickListener(chip: String?, pos: Int) {
                //show orders
                if (pos==1){
                    val orders = showKitchenItems()
                    val kitchenOrderAdapter=KitchenOrderAdapter(orders,context!!)
                    showEmpty(true,"")
                    kitchen.adapter= kitchenOrderAdapter

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

    fun showKitchenItems(): ArrayList<KitchenOrderModel>{
        val item1 = KitchenItemModel(
            "1",
            "Salat",
            "ris",
            "14:25",
            "11:00",
            1
        )
        val order1 = KitchenOrderModel(1,"Айжамал", arrayListOf(item1))

        val item2 = KitchenItemModel(
            "2",
            "Salat2",
            "",
            "04:05",
            "11:20",
            1
        )
        val item3 = KitchenItemModel(
            "3",
            "Salat3",
            "ris3", //
            "21:25",
            "11:18",
            2
        )
        val item4 = KitchenItemModel(
            "4",
            "Salat4",
            "ris3", //
            "17:25",
            "11:18",
            5
        )

        val order2 = KitchenOrderModel(2,"Елена", arrayListOf(item2,item3,item4))

        return arrayListOf(order1,order2)
    }

    private fun selectKitchens(){
        val dialog  = DialogUtil.bottom(R.layout.select_kitchen,context!!)
        val recyclerView:RecyclerView= dialog!!.findViewById(R.id.items)
        val button :Button= dialog.findViewById(R.id.button)
        val valueList: List<CheckBoxModel> = ArrayList(checkBoxHash.values)

        ArrayList(checkBoxHash.values).filter { x->x.isChecked }.size.let {
            if(it>0){
                button.text = String.format("${context!!.resources.getString(R.string.selectKitchen)} (${it})")
            }else
                button.text = context!!.resources.getString(R.string.selectKitchen)
        }

        val check =  object: CheckBoxAdapter(valueList,context!!){
            override fun getSelectItems(isChecked: Boolean, checkBoxModel: CheckBoxModel) {
                checkBoxHash.put(checkBoxModel.id,checkBoxModel)
                ArrayList(checkBoxHash.values).filter { x->x.isChecked }.size.let {
                    if(it>0){
                        button.text = String.format("${context.resources.getString(R.string.selectKitchen)} (${it})")
                    }else
                        button.text = context.resources.getString(R.string.selectKitchen)
                }
            }
        }

        button.setOnClickListener {
            ArrayList(checkBoxHash.values).filter { x->x.isChecked }.plus(checkBoxAdd()).let {
                setChipAdapter(it.distinct())
                showEmpty(it.size>1,resources.getString(R.string.selectKitchen))
            }
            dialog.dismiss()
        }
        recyclerView.adapter = check
        dialog.show()
    }

    private fun getListOfChips(string : List<CheckBoxModel>): List<CheckBoxModel> {
        System.out.println("list Size ${string.size}")
        showEmpty(string.size>1,resources.getString(R.string.selectKitchen))
        return string
    }

    private fun showEmpty(hasItem : Boolean, string: String){
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

    fun  checkBoxAdd() : CheckBoxModel{
        val checkBoxModel = CheckBoxModel()
        checkBoxModel.isChecked = false
        checkBoxModel.id = -1
        checkBoxModel.name= "add"
        return  checkBoxModel
    }
}