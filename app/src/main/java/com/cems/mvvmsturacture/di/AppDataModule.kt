package com.cems.mvvmsturacture.di


import com.common.commondata.dataSource.common.ICommonRemote
import com.common.commondata.remote.remoteImp.CommonRemoteImp
import com.common.commondata.remote.repository.CommonRepository
import com.common.commondata.remote.service.CommonService
import com.common.commondomain.repositorys.ICommonRepository
import com.coredata.executor.JobExecutor
import com.coredomain.diinterfaces.excuter.PostExecutionThread
import com.coredomain.diinterfaces.excuter.ThreadExecutor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit

@InstallIn(ApplicationComponent::class)
@Module
class AppDataModule {

    @Provides
    fun provideThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor {
        return jobExecutor
    }

    @Provides
    fun providePostExecutionThread(uiThread: UiThread): PostExecutionThread {
        return uiThread
    }

    @Provides
    fun provideICommonRepository(commonRepository: CommonRepository): ICommonRepository {
        return commonRepository
    }

    @Provides
    fun provideCommonRemote(commonRemoteImp: CommonRemoteImp): ICommonRemote {
        return commonRemoteImp
    }

    @Provides
    fun provideService(retrofit: Retrofit): CommonService {
        return retrofit.create(CommonService::class.java)
    }
}