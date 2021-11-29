package com.example.learingrealmandretrofit

import io.realm.RealmConfiguration

object ConfigRealm {
    private const val realmVersion = 1L
    val config: RealmConfiguration = RealmConfiguration
        .Builder()
        .schemaVersion(realmVersion)
        .allowWritesOnUiThread(true)
        .allowQueriesOnUiThread(true)
        .build()
}
