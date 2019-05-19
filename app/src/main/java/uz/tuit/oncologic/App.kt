package uz.tuit.oncologic

import android.app.Application
import com.google.firebase.FirebaseApp
import org.koin.android.ext.android.startKoin
import uz.tuit.oncologic.di.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        startKoin(this,
            listOf(
                networkModule,
                repositoryModule,
                viewModelModule,
                preferencesModule,
                interceptorModule)
        )
    }
}