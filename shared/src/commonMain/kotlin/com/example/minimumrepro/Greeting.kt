package com.example.minimumrepro

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}