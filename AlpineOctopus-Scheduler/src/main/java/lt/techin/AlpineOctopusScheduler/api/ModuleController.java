package lt.techin.AlpineOctopusScheduler.api;

import io.swagger.annotations.ApiOperation;
import lt.techin.AlpineOctopusScheduler.api.dto.ModuleDto;
import lt.techin.AlpineOctopusScheduler.api.dto.ModuleEntityDto;
import lt.techin.AlpineOctopusScheduler.api.dto.mapper.ModuleMapper;
import lt.techin.AlpineOctopusScheduler.exception.SchedulerValidationException;
import lt.techin.AlpineOctopusScheduler.model.Module;
import lt.techin.AlpineOctopusScheduler.service.ModuleService;
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
import static lt.techin.AlpineOctopusScheduler.api.dto.mapper.ModuleMapper.toModule;
import static lt.techin.AlpineOctopusScheduler.api.dto.mapper.ModuleMapper.toModuleDto;
import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequestMapping("/api/v1/modules")
@Validated
public class ModuleController {

    public static Logger logger = LoggerFactory.getLogger(ModuleController.class);
    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping
    @ResponseBody
    public List<ModuleEntityDto> getModules() {
        return moduleService.getAll().stream()
                .map(ModuleMapper::toModuleEntityDto)
                .collect(toList());
    }

//    @GetMapping(value ="/{moduleId}/subjects")
//    @ResponseBody
//    public Set<Subject> getAllSubjectsById(@PathVariable Long moduleId) {
//        return moduleService.getAllSubjectsById(moduleId);
//    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ModuleDto> createModule(@Valid @RequestBody ModuleDto moduleDto) {
        if (moduleService.moduleNameIsUnique(toModule(moduleDto))) {
            var createdModule = moduleService.create(toModule(moduleDto));
            return ok(toModuleDto(createdModule));
        } else {
            throw new SchedulerValidationException("Module already exists", "Module name", "Already exists", moduleDto.getName());
        }
    }

    @DeleteMapping("/delete/{moduleId}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long moduleId) {
        var moduleDeleted = moduleService.deleteById(moduleId);

        if (moduleDeleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{moduleId}")
    public ResponseEntity<ModuleDto> updateModule(@PathVariable Long moduleId, @Valid @RequestBody ModuleDto moduleDto) {

        var updatedModule = moduleService.update(moduleId, toModule(moduleDto));
        return ok(toModuleDto(updatedModule));

    }


    @GetMapping(value = "/{moduleId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Module> getModule(@PathVariable Long moduleId) {
        var moduleOptional = moduleService.getById(moduleId);

        var responseEntity = moduleOptional
                .map(module -> ok(module))
                .orElseGet(() -> ResponseEntity.notFound().build());

        return responseEntity;
    }

    @GetMapping(path = "/page", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public List<ModuleEntityDto> getPagedAllModules(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                    @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {


        return moduleService.getPagedAllModules(page, pageSize);

    }

    @GetMapping(path = "page/name-filter/{nameText}")
    @ApiOperation(value = "Get Paged Modules starting with", notes = "Returns list of Modules starting with passed String")
    @ResponseBody
    public List<ModuleEntityDto> getPagedModulesByNameContaining(@PathVariable String nameText,
                                                                 @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                                 @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return moduleService.getPagedModulesByNameContaining(nameText, page, pageSize);
    }

//    @PutMapping("/{moduleId}/subjects/{subjectId}")
//    public ResponseEntity<ModuleDto> addSubjectToModule(@PathVariable Long moduleId, @RequestBody Long subjectId) {
//        var updatedModule = moduleService.addSubjectToModule(moduleId, subjectId);
//
//        return ok(toModuleDto(updatedModule));
//    }
}

