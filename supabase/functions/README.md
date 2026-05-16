# Supabase Edge Functions

This folder contains Supabase Edge Functions that are already structured for deployment.
Each function lives in its own directory under `supabase/functions/` and is invoked by its folder name.

## Functions included
- `create_truck`
- `get_trucks`
- `update_truck`
- `delete_truck`
- `get_check_ins`
- `create_check_in`
- `get_service_tasks`
- `create_service_task`
- `get_task_entries`
- `create_task_entry`
- `update_task_entry`
- `get_condition_photos`
- `add_condition_photo`

## Local development

From the repository root:

```powershell
cd supabase
supabase start
```

To serve functions locally:

```powershell
cd supabase/functions
supabase functions serve
```

## Deploying to Supabase

1. Install the Supabase CLI if you haven't already:
   ```powershell
   npm install -g supabase
   ```
2. Authenticate:
   ```powershell
   supabase login
   ```
3. Link the repo to your Supabase project:
   ```powershell
   supabase link --project-ref <your-project-ref>
   ```
4. Push database migrations:
   ```powershell
   cd supabase
   supabase db push
   ```
5. Deploy the functions:
   ```powershell
   cd supabase
   supabase deploy --project-ref <your-project-ref>
   ```

> If you only want to deploy functions, use:
> `supabase functions deploy <function-name> --project-ref <your-project-ref>`

## Environment variables

The functions expect the following env vars to be available in the Supabase environment:
- `SUPABASE_URL`
- `SUPABASE_ANON_KEY`

For production, you can set secrets via:

```powershell
supabase secrets set SUPABASE_URL=<your-url> SUPABASE_ANON_KEY=<your-anon-key>
```

## Notes

- The code uses Deno-style Edge Functions and imports `@supabase/supabase-js` from esm.sh.
- No additional build step is required for deployment.
