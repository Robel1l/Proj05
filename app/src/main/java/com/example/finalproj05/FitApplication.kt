package com.example.finalproj05


import android.app.Application

class FitApplication : Application() {
    val db by lazy { AppDatabase.getData(this) }
}
