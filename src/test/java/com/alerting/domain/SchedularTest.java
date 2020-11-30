package com.alerting.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alerting.web.rest.TestUtil;

public class SchedularTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Schedular.class);
        Schedular schedular1 = new Schedular();
        schedular1.setId(1L);
        Schedular schedular2 = new Schedular();
        schedular2.setId(schedular1.getId());
        assertThat(schedular1).isEqualTo(schedular2);
        schedular2.setId(2L);
        assertThat(schedular1).isNotEqualTo(schedular2);
        schedular1.setId(null);
        assertThat(schedular1).isNotEqualTo(schedular2);
    }
}
