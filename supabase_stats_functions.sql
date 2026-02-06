-- =====================================================
-- CLINICVIEWER STATISTICS RPC FUNCTIONS
-- Run this in Supabase SQL Editor
-- =====================================================

-- 1. Get distinct months
CREATE OR REPLACE FUNCTION get_distinct_months()
RETURNS TABLE(month TEXT) AS $$
BEGIN
  RETURN QUERY
  SELECT DISTINCT TO_CHAR(created_at, 'YYYY-MM') as month
  FROM patients
  WHERE created_at IS NOT NULL
  ORDER BY month DESC;
END;
$$ LANGUAGE plpgsql;

-- 2. Stats by day for a specific month
CREATE OR REPLACE FUNCTION get_stats_by_day(year_month TEXT)
RETURNS TABLE(visit_date TEXT, day_of_week INT, count BIGINT) AS $$
BEGIN
  RETURN QUERY
  SELECT 
    TO_CHAR(created_at, 'YYYY-MM-DD') as visit_date,
    EXTRACT(DOW FROM created_at)::INT as day_of_week,
    COUNT(*) as count
  FROM patients
  WHERE TO_CHAR(created_at, 'YYYY-MM') = year_month
  GROUP BY visit_date, day_of_week
  ORDER BY visit_date DESC;
END;
$$ LANGUAGE plpgsql;

-- 3. Stats by week
CREATE OR REPLACE FUNCTION get_stats_by_week(limit_count INT DEFAULT 52)
RETURNS TABLE(week TEXT, count BIGINT) AS $$
BEGIN
  RETURN QUERY
  SELECT 
    TO_CHAR(created_at, 'IYYY-"W"IW') as week,
    COUNT(*) as count
  FROM patients
  WHERE created_at IS NOT NULL
  GROUP BY week
  ORDER BY week DESC
  LIMIT limit_count;
END;
$$ LANGUAGE plpgsql;

-- 4. Stats by month
CREATE OR REPLACE FUNCTION get_stats_by_month(limit_count INT DEFAULT 24)
RETURNS TABLE(month TEXT, count BIGINT) AS $$
BEGIN
  RETURN QUERY
  SELECT 
    TO_CHAR(created_at, 'YYYY-MM') as month,
    COUNT(*) as count
  FROM patients
  WHERE created_at IS NOT NULL
  GROUP BY month
  ORDER BY month DESC
  LIMIT limit_count;
END;
$$ LANGUAGE plpgsql;

-- 5. Stats by year
CREATE OR REPLACE FUNCTION get_stats_by_year()
RETURNS TABLE(year TEXT, count BIGINT) AS $$
BEGIN
  RETURN QUERY
  SELECT 
    TO_CHAR(created_at, 'YYYY') as year,
    COUNT(*) as count
  FROM patients
  WHERE created_at IS NOT NULL
  GROUP BY year
  ORDER BY year DESC;
END;
$$ LANGUAGE plpgsql;

-- Grant execute permissions
GRANT EXECUTE ON FUNCTION get_distinct_months() TO anon, authenticated;
GRANT EXECUTE ON FUNCTION get_stats_by_day(TEXT) TO anon, authenticated;
GRANT EXECUTE ON FUNCTION get_stats_by_week(INT) TO anon, authenticated;
GRANT EXECUTE ON FUNCTION get_stats_by_month(INT) TO anon, authenticated;
GRANT EXECUTE ON FUNCTION get_stats_by_year() TO anon, authenticated;
