package com.eflm.practica2.data.remote

import com.eflm.practica2.data.remote.model.AlimentoDto
import com.eflm.practica2.data.remote.model.AlimentodetDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AlimentosApi {

    //Para Apiary
    @GET("Alimentos/lista_alimentos")
    fun getAllAliments(): Call<List<AlimentoDto>>

    @GET("Alimentos/alimento_detail/{id}")
    fun getAlimentDetail(
        @Path("id") id: String?
    ): Call<AlimentodetDto>

}