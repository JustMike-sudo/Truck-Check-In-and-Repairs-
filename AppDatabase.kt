package com.valentinesgarage.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.valentinesgarage.data.local.dao.GarageDao
import com.valentinesgarage.data.local.entity.ChecklistTaskEntity
import com.valentinesgarage.data.local.entity.EmployeeReportEntity
import com.valentinesgarage.data.local.entity.MechanicNoteEntity
import com.valentinesgarage.data.local.entity.TruckEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Database(
    entities = [
        TruckEntity::class,
        ChecklistTaskEntity::class,
        MechanicNoteEntity::class,
        EmployeeReportEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun garageDao(): GarageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "garage_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        seedDatabase(database.garageDao())
                    }
                }
            }
        }

        private suspend fun seedDatabase(dao: GarageDao) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val nowStr = dateFormat.format(Date())
            val anHourAgoStr = dateFormat.format(Date(System.currentTimeMillis() - 3600000))
            val aDayAgoStr = dateFormat.format(Date(System.currentTimeMillis() - 86400000))

            // Seed Trucks
            val t1Id = dao.insertTruck(
                TruckEntity(
                    registrationNumber = "CA-889-214",
                    driverName = "John Doe",
                    mechanicName = "Mike Miller",
                    kilometers = 185200,
                    damageCoordinates = "0.2,0.1;0.8,0.9",
                    additionalNotes = "Slight squealing noise from front brakes when slowing down.",
                    checkInDateTime = aDayAgoStr,
                    status = "Processed"
                )
            ).toInt()

            val t2Id = dao.insertTruck(
                TruckEntity(
                    registrationNumber = "GP-551-789",
                    driverName = "Sarah Jenkins",
                    mechanicName = "Dave Carter",
                    kilometers = 92450,
                    damageCoordinates = "0.3,0.3;0.5,0.7",
                    additionalNotes = "Engine runs hot after long hills. Left headlight broken.",
                    checkInDateTime = anHourAgoStr,
                    status = "Pending"
                )
            ).toInt()

            val t3Id = dao.insertTruck(
                TruckEntity(
                    registrationNumber = "KZN-903-442",
                    driverName = "Marcus Vance",
                    mechanicName = "Valentine (Admin)",
                    kilometers = 312000,
                    damageCoordinates = "0.1,0.1;0.2,0.8;0.9,0.9;0.5,0.5",
                    additionalNotes = "Routine full inspection. Hard gear shift from 2nd to 3rd.",
                    checkInDateTime = nowStr,
                    status = "Complete"
                )
            ).toInt()

            // Seed Checklist Tasks for T1
            dao.insertChecklistTasks(
                listOf(
                    ChecklistTaskEntity(truckId = t1Id, taskName = "Oil change", isCompleted = true, updatedAt = aDayAgoStr),
                    ChecklistTaskEntity(truckId = t1Id, taskName = "Brake inspection", isCompleted = false, updatedAt = aDayAgoStr),
                    ChecklistTaskEntity(truckId = t1Id, taskName = "Tire replacement", isCompleted = true, updatedAt = aDayAgoStr),
                    ChecklistTaskEntity(truckId = t1Id, taskName = "Battery check", isCompleted = false, updatedAt = aDayAgoStr),
                    ChecklistTaskEntity(truckId = t1Id, taskName = "Engine diagnostics", isCompleted = false, updatedAt = aDayAgoStr)
                )
            )

            // Seed Checklist Tasks for T2
            dao.insertChecklistTasks(
                listOf(
                    ChecklistTaskEntity(truckId = t2Id, taskName = "Oil change", isCompleted = false, updatedAt = anHourAgoStr),
                    ChecklistTaskEntity(truckId = t2Id, taskName = "Brake inspection", isCompleted = false, updatedAt = anHourAgoStr),
                    ChecklistTaskEntity(truckId = t2Id, taskName = "Tire replacement", isCompleted = false, updatedAt = anHourAgoStr),
                    ChecklistTaskEntity(truckId = t2Id, taskName = "Battery check", isCompleted = false, updatedAt = anHourAgoStr),
                    ChecklistTaskEntity(truckId = t2Id, taskName = "Engine diagnostics", isCompleted = false, updatedAt = anHourAgoStr)
                )
            )

            // Seed Checklist Tasks for T3 (all completed)
            dao.insertChecklistTasks(
                listOf(
                    ChecklistTaskEntity(truckId = t3Id, taskName = "Oil change", isCompleted = true, updatedAt = nowStr),
                    ChecklistTaskEntity(truckId = t3Id, taskName = "Brake inspection", isCompleted = true, updatedAt = nowStr),
                    ChecklistTaskEntity(truckId = t3Id, taskName = "Tire replacement", isCompleted = true, updatedAt = nowStr),
                    ChecklistTaskEntity(truckId = t3Id, taskName = "Battery check", isCompleted = true, updatedAt = nowStr),
                    ChecklistTaskEntity(truckId = t3Id, taskName = "Engine diagnostics", isCompleted = true, updatedAt = nowStr)
                )
            )

            // Seed Mechanic Notes
            dao.insertMechanicNote(
                MechanicNoteEntity(
                    truckId = t1Id,
                    mechanicName = "Mike Miller",
                    noteContent = "Replaced rear left tire. Brakes are quite thin, might need new pads.",
                    createdAt = aDayAgoStr
                )
            )

            dao.insertMechanicNote(
                MechanicNoteEntity(
                    truckId = t3Id,
                    mechanicName = "Valentine (Admin)",
                    noteContent = "Flushed oil completely, replaced battery. Verified gearbox synchros, shifting smoothly now.",
                    createdAt = nowStr
                )
            )

            // Seed Employee Reports
            dao.insertReport(
                EmployeeReportEntity(
                    employeeName = "Mike Miller",
                    truckRegNo = "CA-889-214",
                    tasksCompleted = "Oil change, Tire replacement",
                    notesWritten = "Replaced rear left tire. Recommended brake pad swap soon.",
                    dateTime = aDayAgoStr
                )
            )

            dao.insertReport(
                EmployeeReportEntity(
                    employeeName = "Valentine (Admin)",
                    truckRegNo = "KZN-903-442",
                    tasksCompleted = "Oil change, Brake inspection, Tire replacement, Battery check, Engine diagnostics",
                    notesWritten = "Complete rebuild checklist finished. Appears in peak running condition.",
                    dateTime = nowStr
                )
            )
        }
    }
}
