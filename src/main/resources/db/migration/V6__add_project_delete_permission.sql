INSERT INTO "Permissions" ("Name", "Description", "Tag", "Resource")
VALUES ('Delete project', 'Delete owned projects', 'project:delete', 'project')
ON CONFLICT ("Tag") DO NOTHING;

INSERT INTO "RolePermissions" ("RoleID", "PermissionID")
SELECT r."ID", p."ID"
FROM "Roles" r
JOIN "Permissions" p ON p."Tag" = 'project:delete'
WHERE r."Name" = 'MEMBER'
ON CONFLICT ("RoleID", "PermissionID") DO NOTHING;
