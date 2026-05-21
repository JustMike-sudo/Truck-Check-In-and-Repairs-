# Valentine Garage

An Android Jetpack Compose app that manages truck check‑ins, active jobs, checklists, and mechanic notes.

## Features
- **Check‑in**: Create a new truck entry with basic details and automatically generate a checklist of tasks.
- **Active Jobs**: Shows pending and in‑progress trucks with status badges. The UI now scrolls correctly using a `LazyColumn`.
- **Checklist**: Toggle individual tasks; status updates (`Pending → In Progress → Complete`) are calculated on the DB side.
- **Mechanic Notes & Reports**: Add notes per truck and generate employee reports.
- **Admin Dashboard**: View all trucks, stats, leaderboard, and logs.

## Architecture
- **MVVM** with a single `GarageRepository` and multiple ViewModels (`CheckInViewModel`, `ActiveJobsViewModel`, `ChecklistViewModel`, `ReportsViewModel`, `AdminViewModel`).
- **Room** database with DAO `GarageDao` handling trucks, tasks, notes, and reports.
- **Status Logic** lives in `GarageRepository.recalculateTruckStatus` and is triggered on each task toggle.
- UI built with **Jetpack Compose** and custom glass‑morphism components.

## Setup
1. **Clone the repo** (already in `c:/Users/dyral/Music/ValentineGarage`).
2. Open the project in Android Studio.
3. Sync Gradle (`./gradlew build`).
4. Run on an emulator or device.

## Development
- The `selectTruck` function now automatically marks a truck as **In Progress** when a mechanic begins work.
- `ActiveJobsViewModel` uses `getActiveTrucksFlow` to hide completed trucks from the active list.
- UI layout uses a `LazyColumn` to keep the search bar and filter chips visible while scrolling.

## License
MIT.

```

## Suggestions for Improvement

- **Add vehicle owner information**: Table for owners/customers linked to trucks.
- **Check-out records**: Track when service is completed and vehicle leaves.
- **Parts inventory**: Track parts used in service tasks.
- **Service history**: Aggregate completed tasks per vehicle.
- **Notifications**: Alerts for upcoming services based on mileage/time.
- **File uploads**: Direct photo uploads to Supabase Storage instead of just URLs.
