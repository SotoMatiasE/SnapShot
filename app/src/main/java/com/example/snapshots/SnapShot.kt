package com.example.snapshots

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties //anotacion
data class SnapShot(var id: String = "",
                    var title: String = "",
                    var photoURL: String = "",
                    var likeList: Map<String, Boolean> = mapOf())
