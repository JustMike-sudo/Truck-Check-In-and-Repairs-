import { createClient } from 'https://esm.sh/@supabase/supabase-js@2'

const corsHeaders = {
  'Access-Control-Allow-Origin': '*',
  'Access-Control-Allow-Headers': 'authorization, x-client-info, apikey, content-type',
}

Deno.serve(async (req) => {
  if (req.method === 'OPTIONS') {
    return new Response('ok', { headers: corsHeaders })
  }

  try {
    const supabaseClient = createClient(
      Deno.env.get('SUPABASE_URL') ?? '',
      Deno.env.get('SUPABASE_ANON_KEY') ?? '',
      {
        global: {
          headers: { Authorization: req.headers.get('Authorization')! },
        },
      }
    )

    const { check_in_id, service_task_id, notes } = await req.json()

    if (!check_in_id || !service_task_id) {
      throw new Error('Check-in ID and Service Task ID are required')
    }

    const { data, error } = await supabaseClient
      .from('task_entries')
      .insert([{ check_in_id, service_task_id, notes, mechanic_id: supabaseClient.auth.user()?.id }])
      .select()
      .single()

    if (error) throw error

    return new Response(JSON.stringify({ task_entry: data }), {
      headers: { ...corsHeaders, 'Content-Type': 'application/json' },
      status: 201,
    })
  } catch (error) {
    return new Response(JSON.stringify({ error: error.message }), {
      headers: { ...corsHeaders, 'Content-Type': 'application/json' },
      status: 400,
    })
  }
})