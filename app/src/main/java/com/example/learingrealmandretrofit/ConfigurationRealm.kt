package com.example.learingrealmandretrofit

import io.realm.RealmConfiguration

object ConfigurationRealm {
    private const val realmVersion = 4L
    val configuration: RealmConfiguration = RealmConfiguration
        .Builder()
        .schemaVersion(realmVersion)
        .migration(migration)
        .allowWritesOnUiThread(true)
        .allowQueriesOnUiThread(true)
        .build()
}
