package com.alerting.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alerting.web.rest.TestUtil;

public class OperandTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operand.class);
        Operand operand1 = new Operand();
        operand1.setId(1L);
        Operand operand2 = new Operand();
        operand2.setId(operand1.getId());
        assertThat(operand1).isEqualTo(operand2);
        operand2.setId(2L);
        assertThat(operand1).isNotEqualTo(operand2);
        operand1.setId(null);
        assertThat(operand1).isNotEqualTo(operand2);
    }
}
