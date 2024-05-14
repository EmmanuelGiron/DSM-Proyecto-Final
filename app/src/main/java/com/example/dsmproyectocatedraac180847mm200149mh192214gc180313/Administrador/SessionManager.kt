package com.example.dsmproyectocatedraac180847mm200149mh192214gc180313.Administrador

object SessionManager {
    private var nombreUsuario: String? = null

    fun iniciarSesion(nombre: String) {
        nombreUsuario = nombre
    }

    fun obtenerNombreUsuario(): String? {
        return nombreUsuario
    }

}