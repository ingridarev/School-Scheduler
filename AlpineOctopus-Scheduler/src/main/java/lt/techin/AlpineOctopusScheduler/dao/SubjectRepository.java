package lt.techin.AlpineOctopusScheduler.dao;

import lt.techin.AlpineOctopusScheduler.model.Subject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByNameContainingIgnoreCase(String nameText, Pageable pageable);

    List<Subject> findAllBySubjectModules_NameContainingIgnoreCase(String moduleText, Pageable pageable);

    List<Subject> findAllBySubjectTeachers_Id(Long teachersId);

    List<Subject> findAllBySubjectModules_Id(Long modulesId);

    //    @Transactional
//    @Modifying
//    @Query(value = "INSERT INTO modules_subjects (module_id, subject_id) VALUES (:module_id, :subject_id)",
//            nativeQuery = true)
//    void addModuleToSubject(@Param("module_id") Long moduleId, @Param("subject_id") Long subjectId);
}
