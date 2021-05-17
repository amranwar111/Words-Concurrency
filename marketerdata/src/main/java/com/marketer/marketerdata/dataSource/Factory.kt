package com.marketer.marketerdata.dataSource

import com.marketer.marketerdata.IDataStore
import javax.inject.Inject

class Factory @Inject constructor(private val remoteDataStore: RemoteDataStore) {
    fun retrieveDataSource(): IDataStore {
        return remoteDataStore
    }
}