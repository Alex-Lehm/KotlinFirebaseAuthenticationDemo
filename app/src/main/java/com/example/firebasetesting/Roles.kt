package com.example.firebasetesting

enum class Roles {

    USER {
        override fun toString(): String {
            return "user"
        }
    },

    ADMIN {
        override fun toString(): String {
            return "admin"
        }
    }

}