package com.yaros.kitchen

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.yaros.kitchen.api.Api
import com.yaros.kitchen.api.ApiService
import com.yaros.kitchen.api.RxSingleSchedulers
import com.yaros.kitchen.room.db.RoomDb
import com.yaros.kitchen.room.entity.KitchenItemModel
import com.yaros.kitchen.viewModel.PaginationVM
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.Callable


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
        paginationVM = PaginationVM(roomDb, RxSingleSchedulers.TEST_SCHEDULER,apiService)
    }

}