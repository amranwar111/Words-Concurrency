package di


import com.user.userdata.dataSource.IRemote
import com.user.userdata.remote.RemoteImp
import com.user.userdata.remote.RepositoryImp
import com.user.userdata.remote.UserRemoteService
import com.user.userdomain.IUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit

@InstallIn(ApplicationComponent::class)
@Module
class UserDataModule {

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
    fun provideIUserRepository(commonRepository: RepositoryImp): IUserRepository {
        return commonRepository
    }

    @Provides
    fun provideUserRemote(commonRemoteImp: RemoteImp): IRemote {
        return commonRemoteImp
    }

    @Provides
    fun provideService(retrofit: Retrofit): UserRemoteService {
        return retrofit.create(UserRemoteService::class.java)
    }
}