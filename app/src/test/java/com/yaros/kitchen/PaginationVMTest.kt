package com.yaros.kitchen

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSchedulers
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.KitchenItemModel
import com.yaros.kitchen.viewModel.PaginationVM
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations


class PaginationVMTest {

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var paginationVM: PaginationVM

    @Mock
    lateinit var roomDb: RoomDb

    lateinit var context: Context

    @Mock
    lateinit var apiService: ApiService

    @Mock
    val itemObserver = mock(Observer::class.java) as Observer<PagedList<KitchenItemModel>>

    @Before
    fun before(){
        MockitoAnnotations.initMocks(this)
        context= mock(Context::class.java)
        roomDb = RoomDb(context)
        apiService = Api("","",context).getApi()
        paginationVM = PaginationVM(roomDb, RxSchedulers.TEST_SCHEDULER,apiService)
    }

}