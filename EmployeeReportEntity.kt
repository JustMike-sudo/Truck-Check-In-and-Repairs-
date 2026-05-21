package com.valentinesgarage.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employee_reports")
data class EmployeeReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val employeeName: String,
    val truckRegNo: String,
    val tasksCompleted: String,
    val notesWritten: String,
    val dateTime: String
)
