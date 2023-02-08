package lt.techin.AlpineOctopusScheduler.api.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class GroupDto {


    private String name;
    private Integer schoolYear;
    private Integer studentAmount;
    private String program;
    private String shift;

    public GroupDto(){}

    public GroupDto(String name, Integer schoolYear, Integer studentAmount, String program, String shift) {
        this.name = name;
        this.schoolYear = schoolYear;
        this.studentAmount = studentAmount;
        this.program = program;
        this.shift = shift;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(Integer schoolYear) {
        this.schoolYear = schoolYear;
    }

    public Integer getStudentAmount() {
        return studentAmount;
    }

    public void setStudentAmount(Integer studentAmount) {
        this.studentAmount = studentAmount;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupDto groupDto = (GroupDto) o;
        return Objects.equals(getName(), groupDto.getName()) && Objects.equals(getSchoolYear(), groupDto.getSchoolYear()) && Objects.equals(getStudentAmount(), groupDto.getStudentAmount()) && Objects.equals(getProgram(), groupDto.getProgram()) && Objects.equals(getShift(), groupDto.getShift());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSchoolYear(), getStudentAmount(), getProgram(), getShift());
    }

    @Override
    public String toString() {
        return "GroupDto{" +
                "name='" + name + '\'' +
                ", schoolYear=" + schoolYear +
                ", studentAmount=" + studentAmount +
                ", program='" + program + '\'' +
                ", shift='" + shift + '\'' +
                '}';
    }
}
