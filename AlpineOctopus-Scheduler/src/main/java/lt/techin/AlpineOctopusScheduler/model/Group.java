package lt.techin.AlpineOctopusScheduler.model;

import org.springframework.data.auditing.CurrentDateTimeProvider;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Group {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    @Min(value = 2023, message = "School year should not be less than year the application was made")
    private Integer schoolYear;
    @NotNull
    private Integer studentAmount;
    @NotBlank
    private String program;
    @NotBlank
    private String shift;

    //private Shift shift;

    public Group(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        Group group = (Group) o;
        return Objects.equals(getId(), group.getId()) && Objects.equals(getName(), group.getName()) && Objects.equals(getSchoolYear(), group.getSchoolYear()) && Objects.equals(getStudentAmount(), group.getStudentAmount()) && Objects.equals(getProgram(), group.getProgram()) && Objects.equals(getShift(), group.getShift());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getSchoolYear(), getStudentAmount(), getProgram(), getShift());
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", schoolYear=" + schoolYear +
                ", studentAmount=" + studentAmount +
                ", program='" + program + '\'' +
                ", shift='" + shift + '\'' +
                '}';
    }
}
