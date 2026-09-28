package com.example.projectmanagement.Application.UseCase;
import com.example.projectmanagement.Application.Dtos.*;
import com.example.projectmanagement.Domain.NotFoundException;
import com.example.projectmanagement.Domain.Entities.*;
import com.example.projectmanagement.Infrastructure.Repositories.*;
import com.example.projectmanagement.Infrastructure.Repositories.Projects.ProjectAdapter;
import com.example.projectmanagement.Infrastructure.Repositories.Users.UserAdapter;
import org.junit.jupiter.api.*; import org.junit.jupiter.api.extension.ExtendWith; import org.mockito.*; import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant; import java.util.*;
import static org.junit.jupiter.api.Assertions.*; import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class ProjectUseCaseTest {
 @Mock ProjectAdapter projects; @Mock UserAdapter users; @Mock StatusRepository statuses; @InjectMocks ProjectUseCase service;
 @Test void foreignProjectIsHiddenAsNotFound(){Project p=project();p.getUserID().setId(8L);when(projects.findById(2)).thenReturn(Optional.of(p));NotFoundException ex=assertThrows(NotFoundException.class,()->service.getProjectById(2,7));assertEquals("Project not found",ex.getMessage());}
 @Test void missingProjectRemainsNotFound(){when(projects.findById(2)).thenReturn(Optional.empty());NotFoundException ex=assertThrows(NotFoundException.class,()->service.getProjectById(2,7));assertEquals("Project not found",ex.getMessage());}
 @Test void updatePreservesOwnerAndCreationTimestamps(){Project p=project();Instant created=p.getCreatedOn(),started=p.getStartDate();when(projects.findById(2)).thenReturn(Optional.of(p));when(statuses.findById(1)).thenReturn(Optional.of(p.getStatusID()));when(projects.save(p)).thenReturn(p);
  service.updateProject(2,new ProjectUpdateDto("Updated","desc",Instant.now().plusSeconds(3600),2,1),7);
  assertEquals(created,p.getCreatedOn());assertEquals(started,p.getStartDate());assertEquals(7,p.getUserID().getId());assertNotNull(p.getModifiedOn());}
 @Test void deletesOwnedProject(){Project p=project();when(projects.findById(2)).thenReturn(Optional.of(p));service.deleteProject(2,7);verify(projects).delete(p);}
 @Test void doesNotDeleteForeignProject(){Project p=project();p.getUserID().setId(8L);when(projects.findById(2)).thenReturn(Optional.of(p));assertThrows(NotFoundException.class,()->service.deleteProject(2,7));verify(projects,never()).delete(any());}
 private static Project project(){User u=new User();u.setId(7L);Status s=new Status();s.setId(1);Project p=new Project();p.setId(2L);p.setUserID(u);p.setStatusID(s);p.setName("Old");p.setCreatedOn(Instant.parse("2025-01-01T00:00:00Z"));p.setStartDate(Instant.parse("2025-01-02T00:00:00Z"));p.setTargetDate(Instant.now().plusSeconds(7200));p.setPriorityLevel(1);return p;}
}
