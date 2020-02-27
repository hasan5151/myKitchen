package com.yaros.kitchen.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.yaros.kitchen.R
import com.yaros.kitchen.adapter.StopListAdapter
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.models.StopListModel
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.utils.DialogUtil
import com.yaros.kitchen.viewModel.MainActivityVM
import com.yaros.kitchen.viewModel.factory.MainActivityFactory
import kotlinx.android.synthetic.main.stop_list_adapter.*

class StopListFragment : BaseFragment() {
    override fun getName(): String = "Стоп-лист"
    override fun getDrawable(): Int = 0

    lateinit var emptyLayout: LinearLayout
    lateinit var emptyButton : Button
    lateinit var mainActivityVM: MainActivityVM
    lateinit var layout: RelativeLayout
    lateinit var stop: TextView
    lateinit var clearStop : TextView
    lateinit var stopListRV: RecyclerView
    lateinit var countOrder: TextView
    lateinit var countClear: TextView
    lateinit var countListRV: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.stop_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emptyLayout= view.findViewById(R.id.emptyLayout)
        emptyButton = view.findViewById(R.id.emptyButton)
        stop= view.findViewById(R.id.stop)
        clearStop= view.findViewById(R.id.clearStop)
        stopListRV= view.findViewById(R.id.stopListRV)
        countOrder= view.findViewById(R.id.countOrder)
        countClear= view.findViewById(R.id.countClear)
        countListRV= view.findViewById(R.id.countListRV)
        layout= view.findViewById(R.id.layout)

        layout.visibility = View.INVISIBLE // change this

        val mainActivityFactory =
            MainActivityFactory(
                RoomDb(context!!), RxSchedulers.DEFAULT,
                Api(context!!).getApi()
            )
        mainActivityVM = ViewModelProvider(activity!!,mainActivityFactory ).get(MainActivityVM::class.java)
        emptyButtonOnClick()

        clearStop.setOnClickListener({
            deleteList()
        })

        countClear.setOnClickListener({
            deleteList(false)
        })
    }

    private fun emptyButtonOnClick() {
        emptyButton.setOnClickListener({
            mainActivityVM.isStopListAddButtonClick()
            //for test
            emptyLayout.visibility = View.INVISIBLE
            layout.visibility = View.VISIBLE
            setStopListRV(getStopList())
            setCountListRV(getCountList())

            countOrder.visibility =View.VISIBLE
            countClear.visibility =View.VISIBLE
            stop.visibility =View.VISIBLE
            clearStop.visibility =View.VISIBLE

        })
    }

    fun getCountList() : ArrayList<StopListModel> =
        arrayListOf(StopListModel("Стейк из семги с ризотто","x1",false),
            StopListModel("Манты с мясом","x5",false),
            StopListModel("жаренная чучвара","x2",false),
            StopListModel("Стейк из семги с овощами","x4",false))

    fun getStopList() : ArrayList<StopListModel> =  arrayListOf(StopListModel("плов той аши","x0",true),
        StopListModel("беш-бармак с кониной","59:40",true),
        StopListModel("манты с тыквой","до 14:30",true))

    private fun setCountListRV(countList : ArrayList<StopListModel>) {
        countList.let {stop->
            object : StopListAdapter(stop,context!!){
                override fun onClick(stop: ArrayList<StopListModel>,position: Int) {
                    deleteItem(stop,position,false)
                }

            }.let {
                countListRV.adapter= it
            }
        }
    }

    private fun setStopListRV(stopList : ArrayList<StopListModel>) {
        stopList.let {stop->
            object : StopListAdapter(stop,context!!){
                override fun onClick(stop: ArrayList<StopListModel>,position: Int) {
                    deleteItem(stop,position)
                }

            }.let {
                stopListRV.adapter= it
            }
        }
    }

    private fun deleteItem(stopList: ArrayList<StopListModel>, position: Int,isStop : Boolean = true){
        val dialog  = DialogUtil.bottom(R.layout.reneworder,context!!)
        val name: TextView = dialog!!.findViewById(R.id.name)
        val time: TextView = dialog!!.findViewById(R.id.time)
        name.text = stopList.get(position).name
        time.text = stopList.get(position).type
        val cancel: TextView = dialog!!.findViewById(R.id.cancel)
        val ok: TextView = dialog!!.findViewById(R.id.ok)

        cancel.setOnClickListener({
            dialog.dismiss()
        })

        ok.setOnClickListener({
            stopList.removeAt(position)

            if (isStop){
                if(stopList.size==0){
                    stop.visibility =View.INVISIBLE
                    clearStop.visibility =View.INVISIBLE
                }
                setStopListRV(stopList)
            }

            else{
                if (stopList.size==0){
                    countOrder.visibility =View.INVISIBLE
                    countClear.visibility =View.INVISIBLE
                }
                setCountListRV(stopList)
            }
            dialog.dismiss()
        })

        dialog?.show()
    }


    private fun deleteList(isStop: Boolean = true) {
        val dialog  = DialogUtil.bottom(R.layout.deletelist,context!!)
        dialog?.show()
        val name: TextView = dialog!!.findViewById(R.id.name)
        val check: CheckBox = dialog!!.findViewById(R.id.check)
        val cancel: TextView = dialog!!.findViewById(R.id.cancel)
        val ok: TextView = dialog!!.findViewById(R.id.ok)

        var isCheck =false

        check.setOnCheckedChangeListener({ compoundButton, b ->
            isCheck = b
        })

        if (isStop){
            name.text =context!!.resources.getString(R.string.clearStopList)
            check.text =context!!.resources.getString(R.string.clearCheckCount)
            ok.setOnClickListener({
                if (isCheck) {
                    setCountListRV(arrayListOf())
                    emptyLayout.visibility = View.VISIBLE
                    layout.visibility = View.INVISIBLE
                }
                setStopListRV(arrayListOf())
                stop.visibility =View.INVISIBLE
                clearStop.visibility =View.INVISIBLE
                dialog.dismiss()
            })


        }else{
            name.text =context!!.resources.getString(R.string.clearCountList)
            check.text =context!!.resources.getString(R.string.clearStop)

            ok.setOnClickListener({
                if (isCheck) {
                    setCountListRV(arrayListOf())
                    emptyLayout.visibility = View.VISIBLE
                    layout.visibility = View.INVISIBLE
                }
                countOrder.visibility =View.INVISIBLE
                countClear.visibility =View.INVISIBLE

                setCountListRV(arrayListOf())
                dialog.dismiss()
            })


        }

        cancel.setOnClickListener({
            dialog.dismiss()
        })




    }


}




