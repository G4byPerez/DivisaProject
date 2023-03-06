package com.gabyperez.divisaserver.others

import com.gabyperez.divisaserver.database.Moneda
import com.gabyperez.divisaserver.database.MonedaDAO
import kotlinx.coroutines.flow.Flow

class MonedaRepository (private val monedaDao: MonedaDAO){
    val allMonedas: Flow<List<Moneda>>
        get() = monedaDao.getAll()
}