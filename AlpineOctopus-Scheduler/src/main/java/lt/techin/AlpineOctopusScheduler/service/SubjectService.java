package lt.techin.AlpineOctopusScheduler.service;

import lt.techin.AlpineOctopusScheduler.api.dto.SubjectEntityDto;
import lt.techin.AlpineOctopusScheduler.api.dto.mapper.SubjectMapper;
import lt.techin.AlpineOctopusScheduler.dao.ModuleRepository;
import lt.techin.AlpineOctopusScheduler.dao.SubjectRepository;
import lt.techin.AlpineOctopusScheduler.exception.SchedulerValidationException;
import lt.techin.AlpineOctopusScheduler.model.Module;
import lt.techin.AlpineOctopusScheduler.model.Subject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final ModuleRepository moduleRepository;

    public SubjectService(SubjectRepository subjectRepository, ModuleRepository moduleRepository) {
        this.subjectRepository = subjectRepository;
        this.moduleRepository = moduleRepository;
    }

    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    public List<SubjectEntityDto> getPagedAllSubjects(int page, int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);

        return subjectRepository.findAll(pageable).stream().map(SubjectMapper::toSubjectEntityDto).collect(Collectors.toList());
    }



    public Optional<Subject> getById(Long id) {
        return subjectRepository.findById(id);
    }


    @Transactional(readOnly = true)
    public List<SubjectEntityDto> getPagedSubjectsByNameContaining(String nameText, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return subjectRepository.findByNameContainingIgnoreCase(nameText, pageable).stream()
                .map(SubjectMapper::toSubjectEntityDto).collect(Collectors.toList());
    }

    public Subject create(Subject subject) {
        return subjectRepository.save(subject);
    }

    public Subject update(Long id, Subject subject) {
        var existingSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new SchedulerValidationException("Subject does not exist",
                        "id", "Subject not found", id.toString()));

        existingSubject.setName(subject.getName());
        existingSubject.setDescription(subject.getDescription());
        existingSubject.setCreatedDate(subject.getCreatedDate());
        existingSubject.setModifiedDate(subject.getModifiedDate());

        return subjectRepository.save(existingSubject);
    }

    public boolean deleteById(Long id) {
        if (subjectRepository.existsById(id)) {
            subjectRepository.deleteById(id);
            return true;
        }

        return false;
    }

    public Subject addModuleToSubject(Long subjectId, Long moduleId) {
        var existingSubject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new SchedulerValidationException("Subject does not exist",
                        "id", "Subject not found", subjectId.toString()));

        var existingModule = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new SchedulerValidationException("Module does not exist",
                        "id", "Module not found", moduleId.toString()));

        Set<Module> existingModuleList = existingSubject.getSubjectModules();
        existingModuleList.add(existingModule);
        existingSubject.setSubjectModules(existingModuleList);

        return subjectRepository.save(existingSubject);
    }
}
