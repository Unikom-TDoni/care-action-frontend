package edu.rpl.careaction.core.dependency

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.rpl.careaction.feature.news.data.dao.NewsRemoteDataSource
import edu.rpl.careaction.feature.support.motivation.data.dao.MotivationRemoteDataSource
import edu.rpl.careaction.feature.activity_tracker.data.dao.ActivityTrackerRemoteDataSource
import edu.rpl.careaction.feature.user.data.dao.UserRemoteDataSource
import edu.rpl.careaction.core.builder.RetrofitBuilder

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {
    @Provides
    fun provideUserRemoteDataSource() =
        RetrofitBuilder.buildService(UserRemoteDataSource::class.java)

    @Provides
    fun provideNewsRemoteDataSource() =
        RetrofitBuilder.buildService(NewsRemoteDataSource::class.java)

    @Provides
    fun provideActivityTrackerRemoteDataSource() =
        RetrofitBuilder.buildService(ActivityTrackerRemoteDataSource::class.java)

    @Provides
    fun provideMotivationRemoteDataSource() =
        RetrofitBuilder.buildService(MotivationRemoteDataSource::class.java)
}