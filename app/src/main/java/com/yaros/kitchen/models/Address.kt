package com.yaros.kitchen.models

class Address {
        var name: String = "Holmes, Sherlock"
        var street: String = "Baker"
        var city: String = "London"
        var state: String? = null
        var zip: String = "123456"
        var badge: Int? = null
        val user: User? = null
        data class User(val name: String = "", val age: Int = 0)

}