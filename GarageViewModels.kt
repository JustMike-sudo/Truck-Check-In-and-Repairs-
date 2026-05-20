package com.valentinesgarage.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.valentinesgarage.data.local.AppDatabase
import com.valentinesgarage.data.local.entity.ChecklistTaskEntity
import com.valentinesgarage.data.local.entity.EmployeeReportEntity
import com.valentinesgarage.data.local.entity.MechanicNoteEntity
import com.valentinesgarage.data.local.entity.TruckEntity
import com.valentinesgarage.data.repository.GarageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- USER DATA MODEL ---
data class User(val name: String, val accessCode: String, val isAdmin: Boolean)

// --- USER SESSION (Shared or individual ViewModel) ---
class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPrefs = application.getSharedPreferences("garage_auth_prefs_v2", Context.MODE_PRIVATE)

    // Hardcoded admin account
    private val ADMIN_USER = User("Valentine", "1111", true)

    private val _currentUser = MutableStateFlow<String?>(null)
    val currentUser: StateFlow<String?> = _currentUser.asStateFlow()

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    private val _registeredUsers = MutableStateFlow<List<User>>(emptyList())
    val registeredUsers: StateFlow<List<User>> = _registeredUsers.asStateFlow()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        val userSet = sharedPrefs.getStringSet("registered_users_list_v2", null)
        val mechanicUsers = if (userSet != null) {
            userSet.mapNotNull { serialized ->
                val parts = serialized.split("|")
                if (parts.size == 3) {
                    User(parts[0], parts[1], parts[2].toBoolean())
                } else null
            }
        } else {
            emptyList()
        }
        // Always include admin at the top, then registered mechanics
        _registeredUsers.value = listOf(ADMIN_USER) + mechanicUsers
    }

    private fun saveMechanicsList(mechanics: List<User>) {
        val serialized = mechanics.map { "${it.name}|${it.accessCode}|${it.isAdmin}" }.toSet()
        sharedPrefs.edit().putStringSet("registered_users_list_v2", serialized).apply()
    }

    fun login(name: String, accessCode: String): Boolean {
        if (name.isBlank() || accessCode.isBlank()) return false
        val user = _registeredUsers.value.find { it.name.equals(name, ignoreCase = true) } ?: return false
        if (user.accessCode == accessCode) {
            _currentUser.value = user.name
            _isAdmin.value = user.isAdmin
            return true
        }
        return false
    }

    fun signUp(name: String, accessCode: String, admin: Boolean): Pair<Boolean, String> {
        val trimmedName = name.trim()
        val trimmedCode = accessCode.trim()
        if (trimmedName.isEmpty()) return Pair(false, "Name cannot be empty")
        if (trimmedCode.length < 4) return Pair(false, "Access code must be at least 4 digits")

        val exists = _registeredUsers.value.any { it.name.equals(trimmedName, ignoreCase = true) }
        if (exists) {
            return Pair(false, "User '$trimmedName' is already registered")
        }

        // All signups are mechanics (non-admin)
        val newUser = User(trimmedName, trimmedCode, false)
        // Get current mechanics (exclude hardcoded admin)
        val currentMechanics = _registeredUsers.value.filter { it != ADMIN_USER }
        val updatedMechanics = currentMechanics + newUser
        saveMechanicsList(updatedMechanics)
        _registeredUsers.value = listOf(ADMIN_USER) + updatedMechanics

        return Pair(true, "Successfully registered!")
    }

    fun logout() {
        _currentUser.value = null
        _isAdmin.value = false
    }
}

// --- CHECK IN VIEWMODEL ---
class CheckInViewModel(private val repository: GarageRepository) : ViewModel() {

    var regNumber = MutableStateFlow("")
    var driverName = MutableStateFlow("")
    var kilometers = MutableStateFlow("")
    var damageCoordinates = MutableStateFlow("")
    var additionalNotes = MutableStateFlow("")

    var hasScratches = MutableStateFlow(false)
    var hasBrokenLights = MutableStateFlow(false)
    var hasTireDamage = MutableStateFlow(false)
    var hasOilLeak = MutableStateFlow(false)

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting = _isSubmitting.asStateFlow()

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg = _errorMsg.asStateFlow()

    fun checkInTruck(mechanicName: String, onSuccess: () -> Unit) {
        val reg = regNumber.value.trim()
        val driver = driverName.value.trim()
        val kms = kilometers.value.trim().toIntOrNull()

        if (reg.isEmpty()) {
            _errorMsg.value = "Registration number is required"
            return
        }
        if (driver.isEmpty()) {
            _errorMsg.value = "Driver name is required"
            return
        }
        if (kms == null || kms <= 0) {
            _errorMsg.value = "Enter valid kilometers driven"
            return
        }

        _errorMsg.value = null
        _isSubmitting.value = true

        viewModelScope.launch {
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val nowStr = dateFormat.format(Date())

                val truck = TruckEntity(
                    registrationNumber = reg,
                    driverName = driver,
                    mechanicName = mechanicName,
                    kilometers = kms,
                    damageCoordinates = damageCoordinates.value.trim(),
                    additionalNotes = additionalNotes.value.trim(),
                    checkInDateTime = nowStr,
                    status = "Pending",
                    hasScratches = hasScratches.value,
                    hasBrokenLights = hasBrokenLights.value,
                    hasTireDamage = hasTireDamage.value,
                    hasOilLeak = hasOilLeak.value
                )

                val truckId = repository.insertTruck(truck).toInt()

                // Insert the 5 standard tasks for this truck
                val tasks = listOf(
                    ChecklistTaskEntity(truckId = truckId, taskName = "Oil change", isCompleted = false, updatedAt = nowStr),
                    ChecklistTaskEntity(truckId = truckId, taskName = "Brake inspection", isCompleted = false, updatedAt = nowStr),
                    ChecklistTaskEntity(truckId = truckId, taskName = "Tire replacement", isCompleted = false, updatedAt = nowStr),
                    ChecklistTaskEntity(truckId = truckId, taskName = "Battery check", isCompleted = false, updatedAt = nowStr),
                    ChecklistTaskEntity(truckId = truckId, taskName = "Engine diagnostics", isCompleted = false, updatedAt = nowStr)
                )
                repository.insertChecklistTasks(tasks)

                // Reset fields
                regNumber.value = ""
                driverName.value = ""
                kilometers.value = ""
                damageCoordinates.value = ""
                additionalNotes.value = ""
                hasScratches.value = false
                hasBrokenLights.value = false
                hasTireDamage.value = false
                hasOilLeak.value = false

                onSuccess()
            } catch (e: Exception) {
                _errorMsg.value = "Failed to save: ${e.localizedMessage}"
            } finally {
                _isSubmitting.value = false
            }
        }
    }
}

// --- ACTIVE JOBS VIEWMODEL ---
class ActiveJobsViewModel(private val repository: GarageRepository) : ViewModel() {

    private val _statusFilter = MutableStateFlow("All")
    val statusFilter = _statusFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val trucks: StateFlow<List<TruckEntity>> = combine(
        repository.getAllTrucks(),
        _statusFilter,
        _searchQuery
    ) { allTrucks, filter, query ->
        allTrucks.filter { truck ->
            val matchesFilter = filter == "All" || truck.status.equals(filter, ignoreCase = true)
            val matchesQuery = query.isEmpty() ||
                    truck.registrationNumber.contains(query, ignoreCase = true) ||
                    truck.driverName.contains(query, ignoreCase = true)
            matchesFilter && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setFilter(status: String) {
        _statusFilter.value = status
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}

// --- CHECKLIST / TRUCK DETAILS VIEWMODEL ---
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ChecklistViewModel(private val repository: GarageRepository) : ViewModel() {

    private val _selectedTruckId = MutableStateFlow<Int?>(null)

    val selectedTruck: StateFlow<TruckEntity?> = _selectedTruckId.flatMapLatest { id ->
        if (id != null) repository.getTruckById(id) else flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val tasks: StateFlow<List<ChecklistTaskEntity>> = _selectedTruckId.flatMapLatest { id ->
        if (id != null) repository.getTasksForTruck(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val notes: StateFlow<List<MechanicNoteEntity>> = _selectedTruckId.flatMapLatest { id ->
        if (id != null) repository.getNotesForTruck(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun selectTruck(id: Int) {
        _selectedTruckId.value = id
    }

    fun toggleTask(task: ChecklistTaskEntity) {
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val nowStr = dateFormat.format(Date())

            val updatedTask = task.copy(isCompleted = !task.isCompleted, updatedAt = nowStr)
            repository.updateChecklistTask(updatedTask)

            // Dynamically evaluate status based on tasks
            val truck = selectedTruck.value
            val currentTasks = tasks.value.toMutableList()
            val index = currentTasks.indexOfFirst { it.id == task.id }
            if (index != -1) currentTasks[index] = updatedTask

            if (truck != null) {
                val completedCount = currentTasks.count { it.isCompleted }
                val totalCount = currentTasks.size
                val newStatus = if (totalCount == 0 || completedCount == 0) "Pending"
                                else if (completedCount == totalCount) "Complete"
                                else "Processed"
                if (truck.status != newStatus) {
                    repository.updateTruck(truck.copy(status = newStatus))
                }
            }
        }
    }

    fun addNote(mechanicName: String, content: String) {
        val id = _selectedTruckId.value ?: return
        if (content.isBlank()) return
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val nowStr = dateFormat.format(Date())

            val note = MechanicNoteEntity(
                truckId = id,
                mechanicName = mechanicName,
                noteContent = content.trim(),
                createdAt = nowStr
            )
            repository.insertMechanicNote(note)
        }
    }

    fun finalizeJob(mechanicName: String, onSuccess: () -> Unit) {
        val truck = selectedTruck.value ?: return
        val currentTasks = tasks.value
        val currentNotes = notes.value

        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val nowStr = dateFormat.format(Date())

            // Update status to Complete
            repository.updateTruck(truck.copy(status = "Complete"))

            // Compile information for Employee Report
            val completedNames = currentTasks.filter { it.isCompleted }.joinToString(", ") { it.taskName }
            val formattedTasks = if (completedNames.isEmpty()) "None" else completedNames

            val compiledNotes = currentNotes.joinToString(" | ") { "${it.mechanicName}: ${it.noteContent}" }
            val formattedNotes = if (compiledNotes.isEmpty()) "No mechanic notes written." else compiledNotes

            val report = EmployeeReportEntity(
                employeeName = mechanicName,
                truckRegNo = truck.registrationNumber,
                tasksCompleted = formattedTasks,
                notesWritten = formattedNotes,
                dateTime = nowStr
            )

            repository.insertReport(report)
            onSuccess()
        }
    }

    fun deleteSelectedTruck(onSuccess: () -> Unit) {
        val truck = selectedTruck.value ?: return
        viewModelScope.launch {
            repository.deleteTruck(truck)
            _selectedTruckId.value = null
            onSuccess()
        }
    }
}

// --- REPORTS VIEWMODEL ---
class ReportsViewModel(private val repository: GarageRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val reports: StateFlow<List<EmployeeReportEntity>> = combine(
        repository.getAllReports(),
        _searchQuery
    ) { allReports, query ->
        allReports.filter { report ->
            query.isEmpty() ||
                    report.employeeName.contains(query, ignoreCase = true) ||
                    report.truckRegNo.contains(query, ignoreCase = true) ||
                    report.tasksCompleted.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}

// --- ADMIN LOG ENTRY ---
sealed class AdminLogEntry {
    abstract val timestamp: String

    data class LiveNote(
        override val timestamp: String,
        val mechanicName: String,
        val registrationNumber: String,
        val noteContent: String
    ) : AdminLogEntry()

    data class SignOffReport(
        override val timestamp: String,
        val mechanicName: String,
        val registrationNumber: String,
        val tasksCompleted: String,
        val notesWritten: String
    ) : AdminLogEntry()
}

// --- ADMIN VIEWMODEL ---
class AdminViewModel(private val repository: GarageRepository) : ViewModel() {

    val trucks: StateFlow<List<TruckEntity>> = repository.getAllTrucks()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val jobStats: StateFlow<Map<String, Int>> = trucks.map { list ->
        val pending = list.count { it.status == "Pending" }
        val processed = list.count { it.status == "Processed" }
        val complete = list.count { it.status == "Complete" }
        mapOf("Pending" to pending, "Processed" to processed, "Complete" to complete)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())

    val mechanicLeaderboard: StateFlow<List<Pair<String, Int>>> = trucks.map { list ->
        list.groupBy { it.mechanicName }
            .map { (name, jobs) -> name to jobs.size }
            .sortedByDescending { it.second }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allTasks: StateFlow<List<ChecklistTaskEntity>> = repository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val reports: StateFlow<List<EmployeeReportEntity>> = repository.getAllReports()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val notes: StateFlow<List<MechanicNoteEntity>> = repository.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val logs: StateFlow<List<AdminLogEntry>> = combine(
        repository.getAllNotes(),
        repository.getAllReports(),
        repository.getAllTrucks()
    ) { notesList, reportsList, trucksList ->
        val truckMap = trucksList.associateBy { it.id }

        val liveNotes = notesList.map { note ->
            val regNo = truckMap[note.truckId]?.registrationNumber ?: "Unknown"
            AdminLogEntry.LiveNote(
                timestamp = note.createdAt,
                mechanicName = note.mechanicName,
                registrationNumber = regNo,
                noteContent = note.noteContent
            )
        }

        val signOffs = reportsList.map { report ->
            AdminLogEntry.SignOffReport(
                timestamp = report.dateTime,
                mechanicName = report.employeeName,
                registrationNumber = report.truckRegNo,
                tasksCompleted = report.tasksCompleted,
                notesWritten = report.notesWritten
            )
        }

        (liveNotes + signOffs).sortedByDescending { it.timestamp }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}

// --- VIEWMODEL FACTORY ---
class GarageViewModelFactory(private val repository: GarageRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CheckInViewModel::class.java) -> CheckInViewModel(repository) as T
            modelClass.isAssignableFrom(ActiveJobsViewModel::class.java) -> ActiveJobsViewModel(repository) as T
            modelClass.isAssignableFrom(ChecklistViewModel::class.java) -> ChecklistViewModel(repository) as T
            modelClass.isAssignableFrom(ReportsViewModel::class.java) -> ReportsViewModel(repository) as T
            modelClass.isAssignableFrom(AdminViewModel::class.java) -> AdminViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
