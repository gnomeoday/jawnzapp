package com.jawnz.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.jawnz.app.web.rest.TestUtil;

public class Thing2Test {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Thing2.class);
        Thing2 thing21 = new Thing2();
        thing21.setId("id1");
        Thing2 thing22 = new Thing2();
        thing22.setId(thing21.getId());
        assertThat(thing21).isEqualTo(thing22);
        thing22.setId("id2");
        assertThat(thing21).isNotEqualTo(thing22);
        thing21.setId(null);
        assertThat(thing21).isNotEqualTo(thing22);
    }
}
