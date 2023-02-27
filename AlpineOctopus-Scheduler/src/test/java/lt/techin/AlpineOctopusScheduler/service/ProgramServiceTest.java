package lt.techin.AlpineOctopusScheduler.service;

import lt.techin.AlpineOctopusScheduler.dao.ProgramRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static lt.techin.AlpineOctopusScheduler.stubs.ProgramCreator.createProgram;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ProgramServiceTest {

    @Mock
    ProgramRepository programRepository;

    @InjectMocks
    ProgramService programService;

    @Test
    void update_programNotFound_throwsException() {
        var program = createProgram(1l);

        assertThatThrownBy(() -> programService.update(1l, program))
                .isInstanceOf(NullPointerException.class);
    }
}