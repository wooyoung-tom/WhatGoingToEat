package tom.dev.whatgoingtoeat.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import tom.dev.whatgoingtoeat.BuildConfig
import tom.dev.whatgoingtoeat.repository.*
import tom.dev.whatgoingtoeat.service.RestaurantService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModules {

    private const val BASE_URL = "http://c3cb493bcd94.ngrok.io"

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
    fun provideMoshiBuilder(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRestaurantService(retrofit: Retrofit): RestaurantService = retrofit.create(RestaurantService::class.java)

//    @Provides
//    @Singleton
//    fun provideHistoryService(retrofit: Retrofit): HistoryService = retrofit.create(HistoryService::class.java)

//    @Provides
//    @Singleton
//    fun provideSearchService(retrofit: Retrofit): SearchService = retrofit.create(SearchService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModules {

    @Binds
    abstract fun bindRestaurantRepository(restaurantRepositoryImpl: RestaurantRepositoryImpl): RestaurantRepository

//    @Binds
//    abstract fun bindHistoryRepository(historyRepositoryImpl: HistoryRepositoryImpl): HistoryRepository

//    @Binds
//    abstract fun bindSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository
}