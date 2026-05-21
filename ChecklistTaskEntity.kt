package com.valentinesgarage.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "checklist_tasks",
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
data class ChecklistTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val truckId: Int,
    val taskName: String,
    val isCompleted: Boolean,
    val updatedAt: String
)
