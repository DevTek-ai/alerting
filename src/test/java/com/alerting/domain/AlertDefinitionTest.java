package com.alerting.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alerting.web.rest.TestUtil;

public class AlertDefinitionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlertDefinition.class);
        AlertDefinition alertDefinition1 = new AlertDefinition();
        alertDefinition1.setId(1L);
        AlertDefinition alertDefinition2 = new AlertDefinition();
        alertDefinition2.setId(alertDefinition1.getId());
        assertThat(alertDefinition1).isEqualTo(alertDefinition2);
        alertDefinition2.setId(2L);
        assertThat(alertDefinition1).isNotEqualTo(alertDefinition2);
        alertDefinition1.setId(null);
        assertThat(alertDefinition1).isNotEqualTo(alertDefinition2);
    }
}
