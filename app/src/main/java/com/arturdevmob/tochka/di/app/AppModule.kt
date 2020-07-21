package com.arturdevmob.tochka.di.app

import android.content.Context
import android.content.SharedPreferences
import com.arturdevmob.tochka.data.network.UserApi
import com.arturdevmob.tochka.data.repositories.AccountRepositoryImpl
import com.arturdevmob.tochka.data.repositories.UserRepositoryImpl
import com.arturdevmob.tochka.domain.repositories.AccountRepository
import com.arturdevmob.tochka.domain.repositories.UserRepository
import com.arturdevmob.tochka.presentation.utils.rx.SchedulerProvider
import com.arturdevmob.tochka.presentation.utils.rx.SchedulerProviderImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {
    companion object {
        const val BASE_URL = "https://api.github.com"
        const val PREF_FILE_NAME = "tochkatest_pref"
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit) = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideUserRepository(userApi: UserApi): UserRepository {
        return UserRepositoryImpl(userApi)
    }

    @Provides
    @Singleton
    fun provideAccountRepository(preferences: SharedPreferences): AccountRepository {
        return AccountRepositoryImpl(preferences)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider {
        return SchedulerProviderImpl()
    }
}