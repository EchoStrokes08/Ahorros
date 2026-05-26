package com.example.ahorrofamiliar.data.model

data class Usuario(
    val id: Int,
    val nombre: String,
    val idDispositivo: String,
    val amigos: List<Int>
)

