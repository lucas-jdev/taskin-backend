package br.com.logiquesistemas.task.unit.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import br.com.logiquesistemas.task.domain.entities.StatusTask;
import br.com.logiquesistemas.task.domain.entities.Task;
import br.com.logiquesistemas.task.domain.error.ParamInvalid;

@DisplayName("Testes de unidade da classe Task")
class TaskTest {

    private final String TITLE = "Imersão Code Level";
    private final String DESCRIPTION = "Evento de desenvolvimento que ocorrerá no meet";
    private final StatusTask STATUS_TASK = StatusTask.NOT_STARTED;

    private final String DESCRIPTION_INVALID = null;

    private Task task;

    @Test
    @DisplayName("Deve criar Task com argumentos")
    void testCreateTaskWithArgs() throws ParamInvalid {
        task = new Task(TITLE, DESCRIPTION);
        assertEquals(TITLE, task.title());
        assertEquals(DESCRIPTION, task.description());
        assertEquals(STATUS_TASK, task.status());
    }

    @Nested
    @DisplayName("Testes das regras de negócio")
    class BusinessRules {

        @ParameterizedTest
        @CsvSource({
                "null",
                "''",
                "' '"
        })
        @DisplayName("Deve lançar exceção de argumento inválido para título inválido")
        void titleInvalidShouldToReturnException(String title) {
            String titleInvalid = checkIfNullIsText(title);
            assertThrows(ParamInvalid.class, () -> new Task(titleInvalid, DESCRIPTION));
        }

        @Test
        @DisplayName("Deve lançar exceção de argumento inválido para descrição inválida")
        void descriptionInvalidShouldToReturnException() {
            assertThrows(ParamInvalid.class, () -> new Task(TITLE, DESCRIPTION_INVALID));
        }

        @Test
        @DisplayName("Deve lançar exceção de argumento inválido para status null")
        void changeStatusNullShouldToReturnException() throws ParamInvalid {
            task = new Task(TITLE, DESCRIPTION);
            assertThrows(ParamInvalid.class, () -> {
                task.changeStatus(null);
            });
        }

        @Test
        @DisplayName("Deve lançar exceção de argumento inválido para status inválido quando atributo já estiver COMPLETED")
        void changeStatusInvalidWhenAttributeIsCOMPLETED() throws ParamInvalid {
            task = new Task(TITLE, DESCRIPTION);
            task.changeStatus(StatusTask.COMPLETED.toString());
            assertThrows(ParamInvalid.class, () ->  {
                task.changeStatus(StatusTask.IN_PROGRESS.toString());
            });
        }

        private String checkIfNullIsText(String value) {
            return Objects.equals(value, "null") ? null : value;
        }

    }

}
