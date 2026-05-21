package com.valentinesgarage.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trucks")
data class TruckEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val registrationNumber: String,
    val driverName: String,
    val mechanicName: String,
    val kilometers: Int,
    val damageCoordinates: String,
    val additionalNotes: String,
    val checkInDateTime: String,
    val status: String, // "Pending", "Processed", "Complete"
    val hasScratches: Boolean = false,
    val hasBrokenLights: Boolean = false,
    val hasTireDamage: Boolean = false,
    val hasOilLeak: Boolean = false
)
