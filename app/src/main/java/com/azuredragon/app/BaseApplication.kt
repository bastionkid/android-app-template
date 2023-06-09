package com.azuredragon.app

import android.app.Application
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {

	override fun onCreate() {
		super.onCreate()

		FirebaseApp.initializeApp(this)

		startKoin {
			androidContext(this@BaseApplication)

			val isDebug = BuildConfig.DEBUG

			modules(emptyList())
		}
	}
}
