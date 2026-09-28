-- Seed a demo owner and projects for a new environment. Each statement is
-- idempotent so redeployments never create duplicate sample data.

INSERT INTO "Users" ("Name", "Description", "Email", "Password", "Department", "JobTitle", "CreatedOn", "StatusID")
SELECT
    'Demo User',
    'Sample project owner',
    'demo@projectmanager.local',
    '$2b$10$HLeG82.u74ZI.DOThbv6A.lUPWDIleJpjgt//0UI/bX4gaiP/S39K',
    'Product',
    'Project Manager',
    CURRENT_TIMESTAMP,
    1
WHERE NOT EXISTS (
    SELECT 1 FROM "Users" WHERE lower("Email") = 'demo@projectmanager.local'
);

INSERT INTO "UserRoles" ("UserID", "RoleID")
SELECT u."ID", r."ID"
FROM "Users" u
CROSS JOIN "Roles" r
WHERE lower(u."Email") = 'demo@projectmanager.local'
  AND r."Name" = 'MEMBER'
ON CONFLICT ("UserID", "RoleID") DO NOTHING;

INSERT INTO "Projects" ("Name", "Description", "CreatedOn", "StartDate", "TargetDate", "PriorityLevel", "StatusID", "UserID")
SELECT seed."Name", seed."Description", CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
       CURRENT_TIMESTAMP + seed."TargetOffset", seed."PriorityLevel", 1, owner."ID"
FROM (
    VALUES
        ('Launch planning', 'Define scope, milestones, and launch owners.', INTERVAL '14 days', 1),
        ('Research backlog', 'Prioritize customer insights for the next release.', INTERVAL '28 days', 2),
        ('Client rollout', 'Prepare the onboarding plan for the pilot team.', INTERVAL '42 days', 3)
) AS seed("Name", "Description", "TargetOffset", "PriorityLevel")
CROSS JOIN (
    SELECT "ID" FROM "Users" WHERE lower("Email") = 'demo@projectmanager.local'
) AS owner
WHERE NOT EXISTS (
    SELECT 1
    FROM "Projects" existing
    WHERE existing."Name" = seed."Name"
      AND existing."UserID" = owner."ID"
);
