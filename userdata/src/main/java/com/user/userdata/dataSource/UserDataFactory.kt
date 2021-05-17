package com.user.userdata.dataSource

import com.user.userdata.remote.IDataStore
import javax.inject.Inject

class UserDataFactory @Inject constructor(private val remoteDataStore: RemoteDataStore) {
    fun retrieveDataSource(): IDataStore {
        return remoteDataStore
    }
}