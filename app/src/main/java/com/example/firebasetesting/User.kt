package com.example.firebasetesting

import java.io.Serializable

data class User(var name: String, var email: String, var password: String) : Serializable {
    constructor() : this  ("", "", "")

}