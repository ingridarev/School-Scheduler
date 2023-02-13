package lt.techin.AlpineOctopusScheduler.api;

import lt.techin.AlpineOctopusScheduler.api.dto.SubjectDto;
import lt.techin.AlpineOctopusScheduler.api.dto.SubjectEntityDto;
import lt.techin.AlpineOctopusScheduler.api.dto.mapper.SubjectMapper;
import lt.techin.AlpineOctopusScheduler.model.Subject;
import lt.techin.AlpineOctopusScheduler.service.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static lt.techin.AlpineOctopusScheduler.api.dto.mapper.SubjectMapper.toSubject;
import static lt.techin.AlpineOctopusScheduler.api.dto.mapper.SubjectMapper.toSubjectDto;
import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequestMapping("/api/v1/subjects")
@Validated
public class SubjectController {
    public static Logger logger = LoggerFactory.getLogger(SubjectController.class);
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    @ResponseBody
    public List<SubjectEntityDto> getSubjects() {
       return subjectService.getAll().stream()
                .map(SubjectMapper::toSubjectEntityDto)
                .collect(toList());
    }

    @PostMapping
    public ResponseEntity<SubjectDto> createSubject(@Valid @RequestBody SubjectDto subjectDto) {
        var createdSubject = subjectService.create(toSubject(subjectDto));

        return ok(toSubjectDto(createdSubject));
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long subjectId) {
        var subjectDeleted = subjectService.deleteById(subjectId);

        if (subjectDeleted) {
            return ResponseEntity.noContent().build();
//            new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{subjectId}")
    public ResponseEntity<SubjectDto> updateSubject(@PathVariable Long subjectId, @RequestBody SubjectDto subjectDto) {
        var updatedSubject = subjectService.update(subjectId, toSubject(subjectDto));

        return ok(toSubjectDto(updatedSubject));
    }
    @GetMapping(value = "/{subjectId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Subject> getSubject(@PathVariable Long subjectId) {
        var subjectOptional = subjectService.getById(subjectId);

        var responseEntity = subjectOptional
                .map(subject -> ok(subject))
                .orElseGet(() -> ResponseEntity.notFound().build());

        return responseEntity;
    }
}