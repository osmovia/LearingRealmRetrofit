package com.example.learingrealmandretrofit

import io.realm.RealmConfiguration
import android.app.Activity

import io.realm.RealmResults
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import androidx.annotation.NonNull

import android.R

import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.String


object ConfigRealm {
     val config: RealmConfiguration = RealmConfiguration
        .Builder()
        .allowWritesOnUiThread(true)
        .allowQueriesOnUiThread(true)
        .build()
}

fun SensorLoggerListAdapter(list: RealmResults<SensorLogged?>?, context: Activity) {
    super(list, true, true)
    this.context = context
    realm = Realm.getDefaultInstance()
}
fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val itemView: View =
        LayoutInflater.from(parent.context).inflate(R.layout.logger_data_item, parent, false)
    return ViewHolder(itemView)
}

fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val temp: SensorLogged = getItem(position)
    holder.sensor.setText(temp.getSensor())
    val date = Date(temp.getDateTimeStart())
    holder.dateTime.setText(String.valueOf(sdf.format(date)))
}