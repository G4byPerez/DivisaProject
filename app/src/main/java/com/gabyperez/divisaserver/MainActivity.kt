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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var txt : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txt = findViewById(R.id.jsonText)
        getPosts()

        val workManager = WorkManager.getInstance(applicationContext)

        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<MyWorker>(
            8,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueue(workRequest)
    }

    private fun getPosts(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api : API = retrofit.create(API::class.java)

        val call : Call<Posts> = api.posts

        call.enqueue(object : Callback<Posts> {
            override fun onResponse(call: Call<Posts>, response: Response<Posts>) {
                if(!response.isSuccessful) {
                    txt.text = "Code: " + response.code()
                    return
                }

                val post = response.body()

                val applicationScope = CoroutineScope(SupervisorJob())
                val moneda = Moneda(
                    _id = 0,
                    code = "",
                    value = 0.0
                )

                post!!.conversion_ratesonversions!!.forEach { codes ->
                    moneda.code = codes.key
                    moneda.value = codes.value
                    txt.append(moneda.code + "  " + moneda.value.toString() + "\n")
                    MonedaDB.getDatabase(applicationContext, applicationScope).MonedaDAO().insert(moneda)
                }
            }

            override fun onFailure(call: Call<Posts>, t: Throwable) {
                txt.text = t.message
            }
        })

    }
}