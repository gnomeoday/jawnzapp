package com.jawnz.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.jawnz.app.web.rest.TestUtil;

public class Thing1DTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(Thing1DTO.class);
        Thing1DTO thing1DTO1 = new Thing1DTO();
        thing1DTO1.setId("id1");
        Thing1DTO thing1DTO2 = new Thing1DTO();
        assertThat(thing1DTO1).isNotEqualTo(thing1DTO2);
        thing1DTO2.setId(thing1DTO1.getId());
        assertThat(thing1DTO1).isEqualTo(thing1DTO2);
        thing1DTO2.setId("id2");
        assertThat(thing1DTO1).isNotEqualTo(thing1DTO2);
        thing1DTO1.setId(null);
        assertThat(thing1DTO1).isNotEqualTo(thing1DTO2);
    }
}
