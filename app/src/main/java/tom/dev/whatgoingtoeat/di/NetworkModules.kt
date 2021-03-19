package tom.dev.whatgoingtoeat.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import tom.dev.whatgoingtoeat.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object NetworkModules {

    @Provides
    fun provideBaseUrl() = "http://de8917581bb8.ngrok.io"

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
        ).build()
    } else OkHttpClient.Builder().build()

//    @Singleton
//    @Provides
//    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
//        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
//    }
//
//    @Singleton
//    @Provides
//    fun provideUserService(retrofit: Retrofit) = retrofit.create()
//
//    @Provides
//    @Singleton
//    fun provideRetrofitRepository(repository: ) =
}