package com.example.learingrealmandretrofit

import io.realm.RealmConfiguration

object ConfigRealm {
    private const val realmVersion = 3L
    val config: RealmConfiguration = RealmConfiguration
        .Builder()
        .schemaVersion(realmVersion)
        .migration(migration)
        .allowWritesOnUiThread(true)
        .allowQueriesOnUiThread(true)
        .build()
}
