package com.alerting.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alerting.web.rest.TestUtil;

public class UnaryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Unary.class);
        Unary unary1 = new Unary();
        unary1.setId(1L);
        Unary unary2 = new Unary();
        unary2.setId(unary1.getId());
        assertThat(unary1).isEqualTo(unary2);
        unary2.setId(2L);
        assertThat(unary1).isNotEqualTo(unary2);
        unary1.setId(null);
        assertThat(unary1).isNotEqualTo(unary2);
    }
}
