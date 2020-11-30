package com.alerting.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alerting.web.rest.TestUtil;

public class TriggeredAlertTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TriggeredAlert.class);
        TriggeredAlert triggeredAlert1 = new TriggeredAlert();
        triggeredAlert1.setId(1L);
        TriggeredAlert triggeredAlert2 = new TriggeredAlert();
        triggeredAlert2.setId(triggeredAlert1.getId());
        assertThat(triggeredAlert1).isEqualTo(triggeredAlert2);
        triggeredAlert2.setId(2L);
        assertThat(triggeredAlert1).isNotEqualTo(triggeredAlert2);
        triggeredAlert1.setId(null);
        assertThat(triggeredAlert1).isNotEqualTo(triggeredAlert2);
    }
}
