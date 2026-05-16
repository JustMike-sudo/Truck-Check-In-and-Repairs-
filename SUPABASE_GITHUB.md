# GitHub → Supabase automatic deploy

This repository includes a GitHub Actions workflow that will push database migrations
and deploy Edge Functions to your Supabase project whenever you push to the `main` branch.

Setup steps (one-time):

1. Create a GitHub repository and push this project:

   git remote add origin git@github.com:<your-org-or-username>/<repo>.git
   git branch -M main
   git push -u origin main

2. Add two GitHub repository secrets (Settings → Secrets → Actions):
   - `SUPABASE_ACCESS_TOKEN` — a Supabase personal access token (create in your Supabase account settings).
   - `SUPABASE_PROJECT_REF` — your project ref (example: `oiduhfpuxxhccotwggrm`).

3. (Optional) In Supabase Dashboard → Settings → Integrations → GitHub, connect the repository
   to enable the Supabase GitHub integration instead of Actions. Either approach is fine.

4. The workflow triggers automatically on pushes to `main`. It runs these steps:
   - install Supabase CLI
   - login using `SUPABASE_ACCESS_TOKEN`
   - link to the project using `SUPABASE_PROJECT_REF`
   - run `supabase db push --yes`
   - run `supabase deploy --project-ref <PROJECT_REF> --yes`

Security notes:
- Never store the `service_role` key in the repo. Use GitHub secrets only.
- If you ever expose a token, rotate it immediately in the Supabase dashboard.

That’s it — pushing to `main` will now apply migrations and deploy functions automatically.
