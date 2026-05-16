# Supabase Backend for Vehicle Service Management

This backend provides a comprehensive API for managing vehicle service operations using Supabase.

## Features

- User authentication with roles (mechanic/admin)
- Vehicle (truck) management
- Check-in records with condition and odometer
- Service tasks catalog
- Task entries linking check-ins to tasks with completion tracking
- Condition photos storage
- Row Level Security (RLS) for data protection

## Database Schema

- `profiles`: User roles (mechanic/admin)
- `trucks`: Vehicle details (VIN, make, model, colour, engine number)
- `check_ins`: Service check-in records (date, condition, odometer)
- `service_tasks`: Available service tasks (e.g., "new brakes")
- `task_entries`: Links check-ins to tasks with completion status and notes
- `condition_photos`: Photos associated with check-ins

## Edge Functions

### Trucks
- `get_trucks`: GET /functions/v1/get_trucks - Retrieve all trucks
- `create_truck`: POST /functions/v1/create_truck - Add a new truck
- `update_truck`: PUT /functions/v1/update_truck - Update truck details
- `delete_truck`: DELETE /functions/v1/delete_truck - Remove a truck

### Check-ins
- `get_check_ins`: GET /functions/v1/get_check_ins - Retrieve check-ins with truck details
- `create_check_in`: POST /functions/v1/create_check_in - Create a check-in record

### Service Tasks
- `get_service_tasks`: GET /functions/v1/get_service_tasks - Retrieve all service tasks
- `create_service_task`: POST /functions/v1/create_service_task - Add a service task

### Task Entries
- `get_task_entries`: GET /functions/v1/get_task_entries - Retrieve task entries with related data
- `create_task_entry`: POST /functions/v1/create_task_entry - Create a task entry
- `update_task_entry`: PUT /functions/v1/update_task_entry - Update completion status/notes

### Condition Photos
- `get_condition_photos`: GET /functions/v1/get_condition_photos - Retrieve photos (optional check_in_id param)
- `add_condition_photo`: POST /functions/v1/add_condition_photo - Add a photo URL

## Deployment

1. Install Supabase CLI: `npm install -g supabase`
2. Login: `supabase login`
3. Link to your project: `supabase link --project-ref your-project-ref`
4. Push migrations: `supabase db push`
5. Deploy functions: `supabase functions deploy`

## Usage

After deployment, use the Supabase client in your frontend.

Example:

```javascript
import { createClient } from '@supabase/supabase-js'

const supabase = createClient(SUPABASE_URL, SUPABASE_ANON_KEY)

// Get trucks
const { data: trucks } = await supabase.functions.invoke('get_trucks')

// Create check-in
const { data } = await supabase.functions.invoke('create_check_in', {
  body: { truck_id: 'uuid', condition_description: 'Needs oil change', odometer_km: 50000 }
})
```

## Suggestions for Improvement

- **Add vehicle owner information**: Table for owners/customers linked to trucks.
- **Check-out records**: Track when service is completed and vehicle leaves.
- **Parts inventory**: Track parts used in service tasks.
- **Service history**: Aggregate completed tasks per vehicle.
- **Notifications**: Alerts for upcoming services based on mileage/time.
- **File uploads**: Direct photo uploads to Supabase Storage instead of just URLs.