package com.valentinesgarage.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mechanic_notes",
    foreignKeys = [
        ForeignKey(
            entity = TruckEntity::class,
            parentColumns = ["id"],
            childColumns = ["truckId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["truckId"])]
)
data class MechanicNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val truckId: Int,
    val mechanicName: String,
    val noteContent: String,
    val createdAt: String
)
