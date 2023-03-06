package com.gabyperez.divisaserver.others

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.gabyperez.divisaserver.api.API
import com.gabyperez.divisaserver.api.Posts
import com.gabyperez.divisaserver.database.Moneda
import com.gabyperez.divisaserver.database.MonedaDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyWorker (context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    val applicationScope = CoroutineScope(SupervisorJob())

    override fun doWork(): Result {
        MonedaDB.getDatabase(applicationContext, applicationScope).MonedaDAO().deleteAll()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var api: API = retrofit.create(API::class.java)

        var call: Call<Posts> = api.posts

        call.enqueue(object : Callback<Posts> {
            override fun onResponse(call: Call<Posts>, response: Response<Posts>) {
                if (!response.isSuccessful) {
                    return
                }

                var post = response.body()

                val applicationScope = CoroutineScope(SupervisorJob())
                var moneda = Moneda(
                    _id = 0,
                    code = "",
                    value = 0.0
                )
                for (codes in post!!.conversion_ratesonversions) {
                    moneda.code = codes.key
                    moneda.value = codes.value
                    MonedaDB.getDatabase(applicationContext, applicationScope).MonedaDAO()
                        .insert(moneda)
                }
            }

            override fun onFailure(call: Call<Posts>, t: Throwable) {
            }
        })
        Log.d("this_app", "Si que, jala")

        return Result.success()
    }
}