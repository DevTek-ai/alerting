package com.alerting.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alerting.web.rest.TestUtil;

public class AlertHistoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlertHistory.class);
        AlertHistory alertHistory1 = new AlertHistory();
        alertHistory1.setId(1L);
        AlertHistory alertHistory2 = new AlertHistory();
        alertHistory2.setId(alertHistory1.getId());
        assertThat(alertHistory1).isEqualTo(alertHistory2);
        alertHistory2.setId(2L);
        assertThat(alertHistory1).isNotEqualTo(alertHistory2);
        alertHistory1.setId(null);
        assertThat(alertHistory1).isNotEqualTo(alertHistory2);
    }
}
