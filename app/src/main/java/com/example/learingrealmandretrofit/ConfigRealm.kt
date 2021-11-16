package com.example.learingrealmandretrofit

import io.realm.RealmConfiguration

object ConfigRealm {
     val config: RealmConfiguration = RealmConfiguration
        .Builder()
        .allowWritesOnUiThread(true)
        .allowQueriesOnUiThread(true)
        .build()
}