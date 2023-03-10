package com.gabyperez.divisaserver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.work.*
import com.gabyperez.divisaserver.database.MonedaDB
import com.gabyperez.divisaserver.others.MyWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var txt : TextView

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
        val lista = MonedaDB.getDatabase(applicationContext, applicationScope).MonedaDAO().getAllLista()

        for(list in lista){
            txt.append(list.code + " -> " + list.value + "\n")
        }
    }
}