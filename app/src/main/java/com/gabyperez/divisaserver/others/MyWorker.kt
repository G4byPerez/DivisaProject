package com.gabyperez.divisaserver.others

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.gabyperez.divisaserver.MainActivity
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
    var info = ""

    override fun doWork(): Result {
        //MonedaDB.getDatabase(applicationContext, applicationScope).MonedaDAO().deleteAll()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api: API = retrofit.create(API::class.java)

        val call: Call<Posts> = api.getPosts("v6/064e537b97fd03303fa8e8ae/latest/USD")

        call.enqueue(object : Callback<Posts> {
            override fun onResponse(call: Call<Posts>, response: Response<Posts>) {

                if (!response.isSuccessful) {
                    return
                }

                val post = response.body()

                val applicationScope = CoroutineScope(SupervisorJob())
                val moneda = Moneda(
                    _id = 0,
                    code = "",
                    update = "",
                    base_code = "",
                    value = 0.0
                )

                for (codes in post!!.conversion_ratesonversions) {
                    moneda.code = codes.key
                    moneda.value = codes.value

                    moneda.base_code = post.base_code
                    moneda.update = post.time_last_update_utc

                    //MainActivity().txt.append(moneda.code + "  " + moneda.value.toString() + "  " + moneda.base_code + "  " + moneda.update + "\n")
                    MonedaDB.getDatabase(applicationContext, applicationScope).MonedaDAO().insert(moneda)
                }
            }

            override fun onFailure(call: Call<Posts>, t: Throwable) {
            }
        })

        return Result.success()
    }
}