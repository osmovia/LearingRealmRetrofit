package com.example.learingrealmandretrofit


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId
import java.io.Serializable
import com.google.gson.Gson


open class RealmCard(
    @PrimaryKey
    var id: String = ObjectId().toHexString(),
    @Required
    var word: String? = null,
    var translate: String? = null,
    var example: String? = null
) : RealmObject(), Serializable{
    override fun toString(): String {
        return Gson().toJson(this)
    }
}