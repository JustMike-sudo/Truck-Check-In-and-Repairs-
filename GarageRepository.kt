package com.valentinesgarage.data.repository

import com.valentinesgarage.data.local.dao.GarageDao
import com.valentinesgarage.data.local.entity.ChecklistTaskEntity
import com.valentinesgarage.data.local.entity.EmployeeReportEntity
import com.valentinesgarage.data.local.entity.MechanicNoteEntity
import com.valentinesgarage.data.local.entity.TruckEntity
import kotlinx.coroutines.flow.Flow

class GarageRepository(private val garageDao: GarageDao) {

    // --- TRUCKS ---
    fun getAllTrucks(): Flow<List<TruckEntity>> = garageDao.getAllTrucksFlow()

    // FIX: Active trucks excludes completed jobs so the Active Jobs screen self-cleans
    fun getActiveTrucks(): Flow<List<TruckEntity>> = garageDao.getActiveTrucksFlow()

    fun getTruckById(truckId: Int): Flow<TruckEntity?> = garageDao.getTruckById(truckId)

    suspend fun insertTruck(truck: TruckEntity): Long = garageDao.insertTruck(truck)

    suspend fun updateTruck(truck: TruckEntity) = garageDao.updateTruck(truck)

    suspend fun deleteTruck(truck: TruckEntity) = garageDao.deleteTruck(truck)

    // FIX: Recalculates and saves truck status based on real task completion counts.
    // Called after every task toggle so status is always accurate.
    // Pending   = no tasks done yet
    // In Progress = some tasks done
    // Complete  = all tasks done
    suspend fun recalculateTruckStatus(truckId: Int) {
        val total = garageDao.getTotalTaskCount(truckId)
        val completed = garageDao.getCompletedTaskCount(truckId)
        val newStatus = when {
            total == 0 || completed == 0 -> "Pending"
            completed < total -> "In Progress"
            else -> "Complete"
        }
        garageDao.updateTruckStatus(truckId, newStatus)
    }

    // --- CHECKLIST TASKS ---
    fun getTasksForTruck(truckId: Int): Flow<List<ChecklistTaskEntity>> = garageDao.getTasksForTruck(truckId)

    fun getAllTasks(): Flow<List<ChecklistTaskEntity>> = garageDao.getAllTasksFlow()

    suspend fun insertChecklistTasks(tasks: List<ChecklistTaskEntity>) = garageDao.insertChecklistTasks(tasks)

    suspend fun updateChecklistTask(task: ChecklistTaskEntity) = garageDao.updateChecklistTask(task)

    suspend fun completeAllTasksForTruck(truckId: Int) = garageDao.completeAllTasksForTruck(truckId)

    // --- MECHANIC NOTES ---
    fun getNotesForTruck(truckId: Int): Flow<List<MechanicNoteEntity>> = garageDao.getNotesForTruck(truckId)

    fun getAllNotes(): Flow<List<MechanicNoteEntity>> = garageDao.getAllNotesFlow()

    suspend fun insertMechanicNote(note: MechanicNoteEntity) = garageDao.insertMechanicNote(note)

    // --- REPORTS ---
    fun getAllReports(): Flow<List<EmployeeReportEntity>> = garageDao.getAllReportsFlow()

    suspend fun insertReport(report: EmployeeReportEntity) = garageDao.insertReport(report)
}
