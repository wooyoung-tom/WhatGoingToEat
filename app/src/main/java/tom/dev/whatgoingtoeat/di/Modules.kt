package tom.dev.whatgoingtoeat.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tom.dev.whatgoingtoeat.BuildConfig
import tom.dev.whatgoingtoeat.repository.*
import tom.dev.whatgoingtoeat.service.HistoryService
import tom.dev.whatgoingtoeat.service.SearchService
import tom.dev.whatgoingtoeat.service.UserService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModules {

    private const val BASE_URL = "http://6e32d37db69c.ngrok.io"

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
        ).build()
    } else OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService = retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideHistoryService(retrofit: Retrofit): HistoryService = retrofit.create(HistoryService::class.java)

    @Provides
    @Singleton
    fun provideSearchService(retrofit: Retrofit): SearchService = retrofit.create(SearchService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModules {

    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindHistoryRepository(historyRepositoryImpl: HistoryRepositoryImpl): HistoryRepository

    @Binds
    abstract fun bindSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository
}