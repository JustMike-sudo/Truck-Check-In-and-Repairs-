-- Create profiles table for user roles
CREATE TABLE public.profiles (
  id UUID REFERENCES auth.users(id) ON DELETE CASCADE PRIMARY KEY,
  role TEXT CHECK (role IN ('mechanic', 'admin')) NOT NULL DEFAULT 'mechanic'
);

-- Create trucks table
CREATE TABLE public.trucks (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  vin TEXT UNIQUE NOT NULL,
  make TEXT NOT NULL,
  model TEXT NOT NULL,
  colour TEXT,
  engine_number TEXT
);

-- Create check_ins table
CREATE TABLE public.check_ins (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  truck_id UUID REFERENCES public.trucks(id) ON DELETE CASCADE NOT NULL,
  check_in_date TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  condition_description TEXT,
  odometer_km INTEGER
);

-- Create service_tasks table
CREATE TABLE public.service_tasks (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  description TEXT NOT NULL
);

-- Create task_entries table
CREATE TABLE public.task_entries (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  check_in_id UUID REFERENCES public.check_ins(id) ON DELETE CASCADE NOT NULL,
  service_task_id UUID REFERENCES public.service_tasks(id) ON DELETE CASCADE NOT NULL,
  completed BOOLEAN DEFAULT FALSE,
  notes TEXT,
  mechanic_id UUID REFERENCES auth.users(id) ON DELETE SET NULL
);

-- Create condition_photos table
CREATE TABLE public.condition_photos (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  check_in_id UUID REFERENCES public.check_ins(id) ON DELETE CASCADE NOT NULL,
  photo_url TEXT NOT NULL
);

-- Enable RLS on all tables
ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.trucks ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.check_ins ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.service_tasks ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.task_entries ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.condition_photos ENABLE ROW LEVEL SECURITY;

-- Policies for profiles
CREATE POLICY "Users can view their own profile" ON public.profiles
  FOR SELECT USING (auth.uid() = id);

CREATE POLICY "Users can update their own profile" ON public.profiles
  FOR UPDATE USING (auth.uid() = id) WITH CHECK (auth.uid() = id);

CREATE POLICY "Admins can view all profiles" ON public.profiles
  FOR SELECT USING (
    EXISTS (SELECT 1 FROM public.profiles WHERE id = auth.uid() AND role = 'admin')
  );

-- Policies for trucks (authenticated users can CRUD)
CREATE POLICY "Authenticated users can view trucks" ON public.trucks
  FOR SELECT TO authenticated USING (true);

CREATE POLICY "Authenticated users can insert trucks" ON public.trucks
  FOR INSERT TO authenticated WITH CHECK (true);

CREATE POLICY "Authenticated users can update trucks" ON public.trucks
  FOR UPDATE TO authenticated USING (true) WITH CHECK (true);

CREATE POLICY "Authenticated users can delete trucks" ON public.trucks
  FOR DELETE TO authenticated USING (true);

-- Policies for check_ins
CREATE POLICY "Authenticated users can view check_ins" ON public.check_ins
  FOR SELECT TO authenticated USING (true);

CREATE POLICY "Authenticated users can insert check_ins" ON public.check_ins
  FOR INSERT TO authenticated WITH CHECK (true);

CREATE POLICY "Authenticated users can update check_ins" ON public.check_ins
  FOR UPDATE TO authenticated USING (true) WITH CHECK (true);

CREATE POLICY "Authenticated users can delete check_ins" ON public.check_ins
  FOR DELETE TO authenticated USING (true);

-- Policies for service_tasks
CREATE POLICY "Authenticated users can view service_tasks" ON public.service_tasks
  FOR SELECT TO authenticated USING (true);

CREATE POLICY "Authenticated users can insert service_tasks" ON public.service_tasks
  FOR INSERT TO authenticated WITH CHECK (true);

CREATE POLICY "Authenticated users can update service_tasks" ON public.service_tasks
  FOR UPDATE TO authenticated USING (true) WITH CHECK (true);

CREATE POLICY "Authenticated users can delete service_tasks" ON public.service_tasks
  FOR DELETE TO authenticated USING (true);

-- Policies for task_entries
CREATE POLICY "Authenticated users can view task_entries" ON public.task_entries
  FOR SELECT TO authenticated USING (true);

CREATE POLICY "Authenticated users can insert task_entries" ON public.task_entries
  FOR INSERT TO authenticated WITH CHECK (true);

CREATE POLICY "Authenticated users can update task_entries" ON public.task_entries
  FOR UPDATE TO authenticated USING (true) WITH CHECK (true);

CREATE POLICY "Authenticated users can delete task_entries" ON public.task_entries
  FOR DELETE TO authenticated USING (true);

-- Policies for condition_photos
CREATE POLICY "Authenticated users can view condition_photos" ON public.condition_photos
  FOR SELECT TO authenticated USING (true);

CREATE POLICY "Authenticated users can insert condition_photos" ON public.condition_photos
  FOR INSERT TO authenticated WITH CHECK (true);

CREATE POLICY "Authenticated users can update condition_photos" ON public.condition_photos
  FOR UPDATE TO authenticated USING (true) WITH CHECK (true);

CREATE POLICY "Authenticated users can delete condition_photos" ON public.condition_photos
  FOR DELETE TO authenticated USING (true);