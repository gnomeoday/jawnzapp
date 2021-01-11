package com.jawnz.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class Thing1MapperTest {

    private Thing1Mapper thing1Mapper;

    @BeforeEach
    public void setUp() {
        thing1Mapper = new Thing1MapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = "id1";
        assertThat(thing1Mapper.fromId(id).getId()).isEqualTo(id);
        assertThat(thing1Mapper.fromId(null)).isNull();
    }
}
