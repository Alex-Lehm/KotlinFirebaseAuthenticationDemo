package com.example.firebasetesting

object AccessCheckers : IAccessCheckers {
    override fun isUser(user: User): Boolean {
        return (user.role == Roles.USER.toString())
    }

    override fun isAdmin(user: User): Boolean {
        return (user.role == Roles.ADMIN.toString())
    }
}