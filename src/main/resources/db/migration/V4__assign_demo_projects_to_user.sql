-- Move the three seeded projects to the intended existing account.
-- Do not silently assign records if ID 1 belongs to a different user.
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM "Users"
        WHERE "ID" = 1
          AND lower("Email") = 'user@example.com'
          AND "Name" = 'user'
    ) THEN
        RAISE EXCEPTION 'Expected user ID 1 with email user@example.com and name user';
    END IF;
END $$;

UPDATE "Projects"
SET "UserID" = 1
WHERE "Name" IN ('Launch planning', 'Research backlog', 'Client rollout')
  AND "UserID" IN (
      SELECT "ID"
      FROM "Users"
      WHERE lower("Email") = 'demo@projectmanager.local'
  );
