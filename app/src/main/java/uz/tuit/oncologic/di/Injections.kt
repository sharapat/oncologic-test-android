package uz.tuit.oncologic.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import uz.tuit.oncologic.data.AppRepository
import uz.tuit.oncologic.data.network.AddCookiesInterceptor
import uz.tuit.oncologic.data.network.ApiService
import uz.tuit.oncologic.data.network.ReceivedCookiesInterceptor
import uz.tuit.oncologic.helper.JsoupHelper
import uz.tuit.oncologic.helper.SharedPreferencesHelper
import uz.tuit.oncologic.ui.auth.AuthViewModel
import uz.tuit.oncologic.ui.main.MainViewModel
import uz.tuit.oncologic.ui.result.ResultViewModel
import java.util.concurrent.TimeUnit

private const val baseUrl = "http://xn----8sb4anfhdi.xn--p1ai"

val networkModule = module {
    single { provideGson() }
    single { provideOkHttpClient(get(), get()) }
    single { providesRetrofit(get(), get()) }
    single { providesApiService(get()) }
    single { FirebaseFirestore.getInstance() }
}

val repositoryModule = module {
    single {AppRepository(get(), get(), get())}
    single {JsoupHelper()}
}

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { ResultViewModel(get(), get()) }
}

val preferencesModule = module {
    single { SharedPreferencesHelper(androidContext()) }
}

val interceptorModule = module {
    single { ReceivedCookiesInterceptor(get()) }
    single { AddCookiesInterceptor(get()) }
}

fun provideGson() : Gson =
    GsonBuilder().setLenient().create()

fun provideOkHttpClient(
    receivedCookiesInterceptor: ReceivedCookiesInterceptor,
    addCookiesInterceptor: AddCookiesInterceptor) : OkHttpClient {

    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY

    return OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .addInterceptor(interceptor)
        .addInterceptor(receivedCookiesInterceptor)
        .addInterceptor(addCookiesInterceptor)
        .build()
}

fun providesRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()

fun providesApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}



