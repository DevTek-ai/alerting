package com.alerting.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alerting.web.rest.TestUtil;

public class BinaryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Binary.class);
        Binary binary1 = new Binary();
        binary1.setId(1L);
        Binary binary2 = new Binary();
        binary2.setId(binary1.getId());
        assertThat(binary1).isEqualTo(binary2);
        binary2.setId(2L);
        assertThat(binary1).isNotEqualTo(binary2);
        binary1.setId(null);
        assertThat(binary1).isNotEqualTo(binary2);
    }
}
