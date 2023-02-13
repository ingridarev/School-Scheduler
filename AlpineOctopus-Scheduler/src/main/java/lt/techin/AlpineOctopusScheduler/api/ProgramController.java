package lt.techin.AlpineOctopusScheduler.api;

import io.swagger.annotations.ApiOperation;
import lt.techin.AlpineOctopusScheduler.api.dto.*;
import lt.techin.AlpineOctopusScheduler.api.dto.mapper.ProgramMapper;
import lt.techin.AlpineOctopusScheduler.api.dto.mapper.SubjectMapper;
import lt.techin.AlpineOctopusScheduler.model.Program;
import lt.techin.AlpineOctopusScheduler.model.ProgramSubjectHours;
import lt.techin.AlpineOctopusScheduler.service.ProgramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static lt.techin.AlpineOctopusScheduler.api.dto.mapper.ProgramMapper.toProgram;
import static lt.techin.AlpineOctopusScheduler.api.dto.mapper.ProgramMapper.toProgramDto;
import static lt.techin.AlpineOctopusScheduler.api.dto.mapper.ProgramMapper.toProgramEntityDto;
import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequestMapping("/api/v1/programs")
@Validated
public class ProgramController {

    public static Logger logger = LoggerFactory.getLogger(ProgramController.class);
    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }




    @GetMapping
    @ResponseBody
    public List<ProgramEntityDto> getSubjects() {
        return programService.getAllPrograms();
    }

    @GetMapping(path ="/page", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<ProgramDto> getPagedAllPrograms(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                              @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize)                                              {


        return programService.getPagedAllPrograms(page, pageSize);

    }

    @GetMapping(path = "/starting-with/{nameText}")
    @ApiOperation(value = "Get Programs starting with", notes = "Returns list of Programs starting with passed String")
    @ResponseBody
    public List<ProgramDto> getProgramsByNameContaining(@PathVariable String nameText) {
        return programService.getProgramsByNameContaining(nameText);
    }

    @GetMapping(value = "/{programId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Program> getProgram(@PathVariable Long programId) {
        var programOptional = programService.getById(programId);

        return programOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping(value = "/{programId}/subjects", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<ProgramSubjectHoursDtoForList> getAllSubjectsInProgram(@PathVariable Long programId) {
        return programService.getAllSubjectsInProgram(programId);
    }

    @DeleteMapping("/{programId}")
    public ResponseEntity<Void> deleteProgram(@PathVariable Long programId) {
        logger.info("Attempt to delete Program by id: {}", programId);

        boolean deleted = programService.deleteById(programId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("/{programId}/{subjectId}")
    public ResponseEntity<Void> deleteSubjectByIdFromProgram(@PathVariable Long programId, @RequestParam Long subjectId ) {


        boolean deleted = programService.deleteSubjectInProgramById(programId, subjectId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ProgramDto> createProgram(@RequestBody ProgramDto programDto) {
        var createdProgram = programService.create(toProgram(programDto));

        return ok(toProgramDto(createdProgram));
    }

    @PatchMapping("/{programId}")
    public ResponseEntity<ProgramDto> updateProgram(@PathVariable Long programId, @RequestBody ProgramDto programDto) {
        var updatedProgram = programService.update(programId, toProgram(programDto));

        return ok(toProgramDto(updatedProgram));
    }

    @PostMapping(value = "/{programId}/subjects/newSubjectsWithHours")
    @ResponseBody
    public ProgramSubjectHours addSubjectAndHoursToProgram(@PathVariable Long programId, @RequestParam Integer subjectHours, @RequestParam Long subjectId) {
        return programService.addSubjectAndHoursToProgram(programId, subjectId, subjectHours);
    }


}
