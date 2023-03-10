package com.gabyperez.divisaserver.others

import android.app.Application
import com.gabyperez.divisaserver.database.MonedaDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApp : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { MonedaDB.getDatabase(this, applicationScope) }
    val repositoryMoneda by lazy {  MonedaRepository (database.MonedaDAO()) }

    override fun onCreate() {
        super.onCreate()

    }
}