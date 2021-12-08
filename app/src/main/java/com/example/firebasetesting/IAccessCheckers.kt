package com.example.firebasetesting

interface IAccessCheckers {
    fun isUser(user: User): Boolean
    fun isAdmin(user: User): Boolean
}