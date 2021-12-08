package com.example.firebasetesting

import java.io.Serializable

/**
 * Data Class to model a User and their fields - password handled by Firebase, no need for storing.
 * Serializable to allow for passing via Intent
 *
 * @param[name] Name of the user
 * @param[email] Age of the user
 * @param[role] Role of the user (usually 'user')
 */
data class User(var name: String, var email: String, var role: String) : Serializable {
    constructor() : this("", "", "")

}