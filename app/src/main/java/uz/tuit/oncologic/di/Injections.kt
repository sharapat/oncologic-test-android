package uz.tuit.oncologic.di

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import uz.pos.botpro.data.OncologicTestRepository

private const val baseUrl = "http://192.168.1.108:3005/api/"

val firebaseModule = module {

}

val repositoryModule = module(override = true) {
    single { OncologicTestRepository(get()) }
}

val viewModelModule = module {

}


