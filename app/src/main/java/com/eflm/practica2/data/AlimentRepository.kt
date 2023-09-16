package com.eflm.practica2.data

import com.eflm.practica2.data.remote.AlimentosApi
import com.eflm.practica2.data.remote.model.AlimentoDto
import com.eflm.practica2.data.remote.model.AlimentodetDto
import retrofit2.Call
import retrofit2.Retrofit


class AlimentRepository(private val retrofit: Retrofit) {

    private val alimentApi: AlimentosApi = retrofit.create(AlimentosApi::class.java)

    fun getAllAliments(): Call<List<AlimentoDto>> =
        alimentApi.getAllAliments()

    fun getAlimentDetail(id: String): Call<AlimentodetDto> =
        alimentApi.getAlimentDetail(id)


}