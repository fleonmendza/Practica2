package com.eflm.practica2.application

import android.app.Application
import com.eflm.practica2.data.AlimentRepository
import com.eflm.practica2.data.remote.RetrofitHelper

class AlimentsRFApp: Application() {

    private val retrofit by lazy{
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy{
        AlimentRepository(retrofit)
    }

}