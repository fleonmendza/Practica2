package com.eflm.practica2.data.remote.model

import com.google.gson.annotations.SerializedName

data class AlimentoDto(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("imagen")
    val imagen: String? = null,
    @SerializedName("nombre")
    val nombre: String? = null,
    @SerializedName("tiempo_preparacion")
    val tiempo_preparacion: Int? = null,
    @SerializedName("tipo_plato")
    val tipo_plato: String? = null
)