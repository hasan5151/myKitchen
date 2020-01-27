package com.yaros.kitchen.api

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


interface RxSchedulers {
    fun <T> applySingle(): SingleTransformer<T, T>?
    fun <T> applyObservable(): ObservableTransformer<T, T>
    fun <T> applyFlowable(): FlowableTransformer<T, T>
    fun applyCompletable(): CompletableTransformer?

    companion object {
        val DEFAULT: RxSchedulers = object : RxSchedulers {
            override fun <T> applySingle(): SingleTransformer<T, T> =
                SingleTransformer { single: Single<T> ->
                    single
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                }

            override fun <T> applyObservable(): ObservableTransformer<T, T> =
                ObservableTransformer { upstream ->
                    upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                }

            override fun <T> applyFlowable(): FlowableTransformer<T, T> =
                FlowableTransformer { upstream ->
                    upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                }


            override fun applyCompletable():  CompletableTransformer {
                return CompletableTransformer { upstream ->
                    upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                }
            }
        }

        val TEST_SCHEDULER: RxSchedulers = object : RxSchedulers {
            override fun <T> applySingle(): SingleTransformer<T, T> =
                SingleTransformer { single: Single<T> ->
                    single
                        .subscribeOn(Schedulers.trampoline())
                        .observeOn(Schedulers.trampoline())

                }

            override fun <T> applyObservable(): ObservableTransformer<T, T> =
                ObservableTransformer { upstream ->
                    upstream
                        .subscribeOn(Schedulers.trampoline())
                        .observeOn(Schedulers.trampoline())
                }

            override fun  applyCompletable():  CompletableTransformer =
                CompletableTransformer { upstream ->
                    upstream
                        .subscribeOn(Schedulers.trampoline())
                        .observeOn(Schedulers.trampoline())
                }

            override fun <T> applyFlowable(): FlowableTransformer<T, T> =
                FlowableTransformer { upstream ->
                    upstream
                        .subscribeOn(Schedulers.trampoline())
                        .observeOn(Schedulers.trampoline())
                }


        }
    }
}