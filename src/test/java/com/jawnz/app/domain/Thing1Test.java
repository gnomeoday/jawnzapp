package com.jawnz.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.jawnz.app.web.rest.TestUtil;

public class Thing1Test {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Thing1.class);
        Thing1 thing11 = new Thing1();
        thing11.setId("id1");
        Thing1 thing12 = new Thing1();
        thing12.setId(thing11.getId());
        assertThat(thing11).isEqualTo(thing12);
        thing12.setId("id2");
        assertThat(thing11).isNotEqualTo(thing12);
        thing11.setId(null);
        assertThat(thing11).isNotEqualTo(thing12);
    }
}
