# Deploy all Supabase Edge Functions in this project.
# This project ref comes from your Supabase dashboard URL.

$projectRef = "oiduhfpuxxhccotwggrm"

# Optional: set these if not already in your environment
# $env:SUPABASE_URL = "https://xyzcompany.supabase.co"
# $env:SUPABASE_ANON_KEY = "your-anon-key"

$functions = @(
    'create_truck',
    'get_trucks',
    'update_truck',
    'delete_truck',
    'get_check_ins',
    'create_check_in',
    'get_service_tasks',
    'create_service_task',
    'get_task_entries',
    'create_task_entry',
    'update_task_entry',
    'get_condition_photos',
    'add_condition_photo'
)

Write-Host "Deploying Supabase functions to project ref: $projectRef"

Set-Location -Path (Join-Path $PSScriptRoot 'functions')

foreach ($fn in $functions) {
    Write-Host "Deploying function: $fn"
    supabase functions deploy $fn --project-ref $projectRef
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Deployment failed for function: $fn"
        break
    }
}
