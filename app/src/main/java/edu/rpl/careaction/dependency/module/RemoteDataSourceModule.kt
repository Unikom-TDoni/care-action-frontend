package edu.rpl.careaction.dependency.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import edu.rpl.careaction.feature.task.domain.TaskApi
import edu.rpl.careaction.module.api.RetrofitBuilder
import edu.rpl.careaction.feature.user.data.dao.UserRemoteDataSource

@Module
@InstallIn(ViewModelComponent::class)
object RemoteDataSourceModule {
    @Provides
    fun provideUserRemoteDataSource() =
        RetrofitBuilder.buildService(UserRemoteDataSource::class.java)

    @Provides
    fun provideTaskRemoteDataSource() =
        RetrofitBuilder.buildService(TaskApi::class.java)
}