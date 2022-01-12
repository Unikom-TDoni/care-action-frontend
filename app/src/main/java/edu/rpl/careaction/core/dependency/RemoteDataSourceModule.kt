package edu.rpl.careaction.core.dependency

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import edu.rpl.careaction.feature.news.data.dao.NewsRemoteDataSource
import edu.rpl.careaction.feature.motivation.data.dao.MotivationRemoteDataSource
import edu.rpl.careaction.feature.task.data.dao.TaskRemoteDataSource
import edu.rpl.careaction.feature.user.data.dao.UserRemoteDataSource
import edu.rpl.careaction.module.api.RetrofitBuilder

@Module
@InstallIn(ViewModelComponent::class)
object RemoteDataSourceModule {
    @Provides
    fun provideUserRemoteDataSource() =
        RetrofitBuilder.buildService(UserRemoteDataSource::class.java)

    @Provides
    fun provideNewsRemoteDataSource() =
        RetrofitBuilder.buildService(NewsRemoteDataSource::class.java)

    @Provides
    fun provideTaskRemoteDataSource() =
        RetrofitBuilder.buildService(TaskRemoteDataSource::class.java)

    @Provides
    fun provideMotivationRemoteDataSource() =
        RetrofitBuilder.buildService(MotivationRemoteDataSource::class.java)
}