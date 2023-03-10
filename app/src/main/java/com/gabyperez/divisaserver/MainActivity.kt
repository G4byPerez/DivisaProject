package com.gabyperez.divisaserver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.work.*
import com.gabyperez.divisaserver.api.API
import com.gabyperez.divisaserver.api.Posts
import com.gabyperez.divisaserver.database.Moneda
import com.gabyperez.divisaserver.database.MonedaDB
import com.gabyperez.divisaserver.others.MyWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.count
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var txt : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txt = findViewById(R.id.jsonText)

        val workManager = WorkManager.getInstance(applicationContext)

        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<MyWorker>(
            24,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueue(workRequest)

        val applicationScope = CoroutineScope(SupervisorJob())
        var lista = MonedaDB.getDatabase(applicationContext, applicationScope).MonedaDAO().getAllLista()

        for(list in lista){
            txt.append(list.code + " -> " + list.value + "\n")
        }
    }
}