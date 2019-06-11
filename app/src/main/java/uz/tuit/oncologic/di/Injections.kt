package uz.tuit.oncologic.di

import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import uz.tuit.oncologic.data.AppRepository
import uz.tuit.oncologic.helper.JsoupHelper
import uz.tuit.oncologic.helper.SharedPreferencesHelper
import uz.tuit.oncologic.ui.auth.AuthViewModel
import uz.tuit.oncologic.ui.main.MainViewModel
import uz.tuit.oncologic.ui.result.ResultViewModel

val networkModule = module {
    single { FirebaseFirestore.getInstance() }
}

val repositoryModule = module {
    single {AppRepository(get(), get())}
    single {JsoupHelper()}
}

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { ResultViewModel(get()) }
}

val preferencesModule = module {
    single { SharedPreferencesHelper(androidContext()) }
}



