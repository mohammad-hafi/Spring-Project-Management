package com.example.projectmanagement.Infrastructure.Repositories;
import com.example.projectmanagement.Domain.Entities.UserRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import java.util.Set;
public interface AuthorizationRepository extends Repository<UserRole,Long> {
 @Query(value="SELECT DISTINCT p.\"Tag\" FROM \"UserRoles\" ur JOIN \"RolePermissions\" rp ON rp.\"RoleID\"=ur.\"RoleID\" JOIN \"Permissions\" p ON p.\"ID\"=rp.\"PermissionID\" WHERE ur.\"UserID\"=:userId",nativeQuery=true)
 Set<String> findPermissionTags(@Param("userId") long userId);
 @Modifying
 @Query(value="INSERT INTO \"UserRoles\" (\"UserID\",\"RoleID\") SELECT :userId,r.\"ID\" FROM \"Roles\" r WHERE r.\"Name\"='MEMBER'",nativeQuery=true)
 int assignMemberRole(@Param("userId") long userId);
}
