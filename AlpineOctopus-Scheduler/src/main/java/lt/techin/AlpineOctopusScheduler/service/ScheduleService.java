package lt.techin.AlpineOctopusScheduler.service;

import lt.techin.AlpineOctopusScheduler.api.dto.ScheduleEntityDto;
import lt.techin.AlpineOctopusScheduler.api.dto.mapper.LessonMapper;
import lt.techin.AlpineOctopusScheduler.api.dto.mapper.ScheduleMapper;
import lt.techin.AlpineOctopusScheduler.dao.*;
import lt.techin.AlpineOctopusScheduler.exception.SchedulerValidationException;
import lt.techin.AlpineOctopusScheduler.model.Lesson;
import lt.techin.AlpineOctopusScheduler.model.ProgramSubjectHours;
import lt.techin.AlpineOctopusScheduler.model.Schedule;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final ScheduleLessonsRepository scheduleLessonsRepository;

    private final GroupsRepository groupsRepository;
    private final ShiftRepository shiftRepository;
    private final ProgramRepository programRepository;
    private final ProgramSubjectHoursRepository programSubjectHoursRepository;
    private final RoomRepository roomRepository;
    private final TeacherRepository teacherRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, ScheduleLessonsRepository scheduleLessonsRepository,
                           GroupsRepository groupsRepository,
                           ShiftRepository shiftRepository,
                           ProgramRepository programRepository,
                           ProgramSubjectHoursRepository programSubjectHoursRepository,
                           RoomRepository roomRepository,
                           TeacherRepository teacherRepository) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleLessonsRepository = scheduleLessonsRepository;
        this.groupsRepository = groupsRepository;
        this.shiftRepository = shiftRepository;
        this.programRepository = programRepository;
        this.programSubjectHoursRepository = programSubjectHoursRepository;
        this.roomRepository = roomRepository;
        this.teacherRepository = teacherRepository;
    }

    @Transactional
    public List<ScheduleEntityDto> getSchedulesByNameContaining(String nameText, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return scheduleRepository.findByNameContainingIgnoreCaseOrderByModifiedDateDesc(nameText, pageable)
                .stream()
                .map(ScheduleMapper::toScheduleEntityDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ScheduleEntityDto> getSchedulesByStartingDate(String startingDate, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);

        //Provided String must be in a YYYY-MM-DD format. Subtracting one in order for the searched day itself to appear in results
        var starting = LocalDate.parse(startingDate).minusDays(1);

        return scheduleRepository.findAllByStartingDateAfterOrderByModifiedDateDesc(starting, pageable)
                .stream()
                .map(ScheduleMapper::toScheduleEntityDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ScheduleEntityDto> getSchedulesByPlannedTill(String plannedTill, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        
        //Provided String must be in a YYYY-MM-DD format. Adding one more in order for the searched day itself to appear in results
        var planned = LocalDate.parse(plannedTill).plusDays(1);

        return scheduleRepository.findByPlannedTillDateBeforeOrderByModifiedDateDesc(planned, pageable)
                .stream()
                .map(ScheduleMapper::toScheduleEntityDto)
                .collect(Collectors.toList());
    }


    public List<ScheduleEntityDto> getAllPagedSchedules(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);

        return scheduleRepository.findAll(pageable)
                .stream()
                .map(ScheduleMapper::toScheduleEntityDto)
                .collect(Collectors.toList());
    }

    public Optional<Schedule> getById(Long id) {
        return scheduleRepository.findById(id);
    }

    public Schedule create(Schedule schedule, Long groupId) {

        //Getting group, from it - shift and program
        var createdGroup = groupsRepository.findById(groupId)
                .orElseThrow(() -> new SchedulerValidationException("Group doesn't exist", "id", "Group doesn't exist", groupId.toString()));
        var createdProgram = createdGroup.getProgram().getId();

        //Getting subjects in the program and adding them to the lesson Set
        List<ProgramSubjectHours> subjectHoursList = programSubjectHoursRepository.findAllByProgramId(createdProgram);
        Set<Lesson> lessonList = subjectHoursList
                .stream()
                .map(LessonMapper::toLessonFromSubject)
                .collect(Collectors.toSet());

        //Creating the Schedule
        schedule.setName(createdGroup.getName() + " " + createdGroup.getShift().getName() + " " + createdGroup.getSchoolYear().toString());
        schedule.setStartingDate(LocalDate.now());

        schedule.setGroup(createdGroup);
        schedule.setGroupName(createdGroup.getId().toString());
        schedule.setShift(createdGroup.getShift());
        schedule.setShiftName(createdGroup.getShift().getName());
        schedule.setSubjects(lessonList);


        return scheduleRepository.save(schedule);
    }

    public Schedule setTeacherAndRoomInASchedule(Long id, Long lessonId, Long teacherId, Long roomId) {

        //finding the schedule in repository
        var existingSchedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new SchedulerValidationException("Schedule does not exist", "id", "Schedule not found", id.toString()));
        //finding the teacher
        var existingTeacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new SchedulerValidationException("Teacher does not exist", "id", "Teacher not found", teacherId.toString()));
        //setting the teacher
        existingSchedule.getSubjects()
                .stream()
                .filter(lesson -> lesson.getId().equals(lessonId))
                .forEach(lesson -> lesson.setTeacher(existingTeacher));
        //finding the room
        var existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new SchedulerValidationException("Room does not exist", "id", "Room not found", roomId.toString()));
        //setting the room
        existingSchedule.getSubjects()
                .stream()
                .filter(lesson -> lesson.getId().equals(lessonId))
                .forEach(lesson -> lesson.setRoom(existingRoom));

        //replacing the subjectList
        existingSchedule.setSubjects(existingSchedule.getSubjects());
        //save to repository
        return scheduleRepository.save(existingSchedule);
    }

    public Schedule setLessonsForScheduling(Long id) {
        //finding the schedule in repository
        var existingSchedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new SchedulerValidationException("Schedule does not exist", "id", "Schedule not found", id.toString()));

        Set<Lesson> createdLessons = existingSchedule.getSubjects().stream().map(LessonMapper::toIndividualLessonSets).collect(Collectors.toSet());

        existingSchedule.setLessons(createdLessons);
        return scheduleRepository.save(existingSchedule);
    }


//    public Schedule scheduleLessons(Long id, Long subjectId, Integer amount, LocalDateTime timeToSchedule) {
//
//    }

    public boolean deleteById(Long id) {
        try {
            scheduleRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException exception) {
            return false;
        }
    }
}
