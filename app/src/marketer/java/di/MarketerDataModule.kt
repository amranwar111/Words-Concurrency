package di


import com.marketer.marketerdata.RepositoryImp
import com.marketer.marketerdata.dataSource.IRemote
import com.marketer.marketerdata.remote.MarketerRemoteService
import com.marketer.marketerdata.remote.RemoteImp
import com.marketer.marketerdomain.IMarketerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit

@InstallIn(ApplicationComponent::class)
@Module
class MarketerDataModule {

//    @Provides
//    fun provideThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor {
//        return jobExecutor
//    }
//
//    @Provides
//    fun providePostExecutionThread(uiThread: UiThread): PostExecutionThread {
//        return uiThread
//    }

    @Provides
    fun provideIUserRepository(commonRepository: RepositoryImp): IMarketerRepository {
        return commonRepository
    }

    @Provides
    fun provideUserRemote(commonRemoteImp: RemoteImp): IRemote {
        return commonRemoteImp
    }

    @Provides
    fun provideService(retrofit: Retrofit): MarketerRemoteService {
        return retrofit.create(MarketerRemoteService::class.java)
    }


}