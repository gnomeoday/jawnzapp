package com.jawnz.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.jawnz.app.web.rest.TestUtil;

public class Thing2DTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(Thing2DTO.class);
        Thing2DTO thing2DTO1 = new Thing2DTO();
        thing2DTO1.setId("id1");
        Thing2DTO thing2DTO2 = new Thing2DTO();
        assertThat(thing2DTO1).isNotEqualTo(thing2DTO2);
        thing2DTO2.setId(thing2DTO1.getId());
        assertThat(thing2DTO1).isEqualTo(thing2DTO2);
        thing2DTO2.setId("id2");
        assertThat(thing2DTO1).isNotEqualTo(thing2DTO2);
        thing2DTO1.setId(null);
        assertThat(thing2DTO1).isNotEqualTo(thing2DTO2);
    }
}
