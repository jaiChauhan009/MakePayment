package com.example.makepayment

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform