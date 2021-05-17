package com.common.commondata.dataSource.common

import com.common.commondata.dataStore.ICommonDataStore
import javax.inject.Inject

class CommonDataFactory @Inject constructor(private val commonRemoteDatastore: ICommonRemoteDataSource) {
    fun retrieveDataSource(): ICommonDataStore {
        return commonRemoteDatastore
    }
}