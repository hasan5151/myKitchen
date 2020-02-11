package com.yaros.kitchen.adapter

import androidx.paging.DataSource
import androidx.paging.PositionalDataSource

class RoomFactoryWrapper<T>(val m_wrappedFactory: DataSource.Factory<Int, T>) :
    DataSource.Factory<Int, T>() {
    override fun create(): DataSource<Int, T> {
        return DataSourceWrapper(m_wrappedFactory.create() as PositionalDataSource<T>)
    }

    internal class DataSourceWrapper<T>(val m_wrappedSource: PositionalDataSource<T>) :
        PositionalDataSource<T>() {
        override fun addInvalidatedCallback(onInvalidatedCallback: InvalidatedCallback) {
            m_wrappedSource.addInvalidatedCallback(onInvalidatedCallback)
        }

        override fun removeInvalidatedCallback(
            onInvalidatedCallback: InvalidatedCallback
        ) {
            m_wrappedSource.removeInvalidatedCallback(onInvalidatedCallback)
        }

        override fun invalidate() {
            m_wrappedSource.invalidate()
        }

        override fun isInvalid(): Boolean {
            return m_wrappedSource.isInvalid
        }

        override fun loadInitial(
            params: LoadInitialParams,
            callback: LoadInitialCallback<T>
        ) { // Workaround for paging bug: https://issuetracker.google.com/issues/123834703
// edit initial load position to start 1/2 load ahead of requested position
            val newStartPos =
                if (params.placeholdersEnabled) params.requestedStartPosition else Math.max(
                    0,
                    params.requestedStartPosition - params.requestedLoadSize / 2
                )
            m_wrappedSource.loadInitial(
                LoadInitialParams(
                    newStartPos,
                    params.requestedLoadSize,
                    params.pageSize,
                    params.placeholdersEnabled
                ), callback
            )
        }

        override fun loadRange(
            params: LoadRangeParams,
            callback: LoadRangeCallback<T>
        ) {
            m_wrappedSource.loadRange(params, callback)
        }

    }

}