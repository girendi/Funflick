package com.girendi.funflick.core.di

import com.girendi.funflick.BuildConfig
import com.girendi.funflick.core.data.api.ApiService
import com.girendi.funflick.core.data.repository.GenreRepositoryImpl
import com.girendi.funflick.core.data.repository.MovieRepositoryImpl
import com.girendi.funflick.core.data.repository.ReviewRepositoryImpl
import com.girendi.funflick.core.data.repository.VideoRepositoryImpl
import com.girendi.funflick.core.domain.repository.GenreRepository
import com.girendi.funflick.core.domain.repository.MovieRepository
import com.girendi.funflick.core.domain.repository.ReviewRepository
import com.girendi.funflick.core.domain.repository.VideoRepository
import com.girendi.funflick.core.domain.usecase.FetchMovieListUseCase
import com.girendi.funflick.core.domain.usecase.FetchMainUseCase
import com.girendi.funflick.core.domain.usecase.FetchMovieDetailUseCase
import com.girendi.funflick.presentation.detail.MovieDetailViewModel
import com.girendi.funflick.presentation.list.MovieListViewModel
import com.girendi.funflick.presentation.main.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.APP_KEY)
                    .build()

                val requestBuilder = original.newBuilder()
                    .url(url)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single<GenreRepository> { GenreRepositoryImpl(apiService = get()) }
    single<MovieRepository> { MovieRepositoryImpl(apiService = get()) }
    single<ReviewRepository> { ReviewRepositoryImpl(apiService = get()) }
    single<VideoRepository> { VideoRepositoryImpl(apiService = get()) }
}

val factoryModule = module {
    factory { FetchMainUseCase(
        genreRepository = get(),
        movieRepository = get()
    ) }
    factory { FetchMovieListUseCase(
        movieRepository = get()
    ) }
    factory { FetchMovieDetailUseCase(
        movieRepository = get(),
        reviewRepository = get(),
        videoRepository = get()
    ) }
}

val viewModelModule = module {
    viewModel { MainViewModel(fetchMainUseCase = get()) }
    viewModel { MovieListViewModel(fetchMovieListUseCase = get()) }
    viewModel { MovieDetailViewModel(fetchMovieDetailUseCase = get()) }
}