package com.danielmilano.data.di

import com.apollographql.apollo.ApolloClient
import com.danielmilano.data.BuildConfig
import com.danielmilano.data.repositories.CountriesRepositoryImpl
import com.danielmilano.data.repositories.remote.countries.CountriesRemoteDataSourceImpl
import com.danielmilano.domain.usecases.GetCountriesUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val dataModule = module {
    single { provideHttpLoggingInterceptor() }
    single { provideHttpClient(get()) }
    single { provideApolloClient(get()) }
    single { provideCountriesRepository(get()) }
    factory { provideGetCountriesUseCase(get()) }
}

private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    return logging
}

private fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient {
    return ApolloClient.builder()
        .serverUrl(BuildConfig.APIS_ENDPOINT)
        .okHttpClient(okHttpClient)
        .build()
}

private fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

private fun provideCountriesRepository(apolloClient: ApolloClient): CountriesRepositoryImpl {
    return CountriesRepositoryImpl(
        CountriesRemoteDataSourceImpl(apolloClient)
    )
}

private fun provideGetCountriesUseCase(repository: CountriesRepositoryImpl): GetCountriesUseCase {
    return GetCountriesUseCase(repository)
}
