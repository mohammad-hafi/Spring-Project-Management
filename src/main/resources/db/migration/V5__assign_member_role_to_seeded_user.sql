-- Ensure the owner of the seeded projects can read, create, and update them.
INSERT INTO "UserRoles" ("UserID", "RoleID")
SELECT u."ID", r."ID"
FROM "Users" u
CROSS JOIN "Roles" r
WHERE u."ID" = 1
  AND lower(u."Email") = 'user@example.com'
  AND u."Name" = 'user'
  AND r."Name" = 'MEMBER'
ON CONFLICT ("UserID", "RoleID") DO NOTHING;
