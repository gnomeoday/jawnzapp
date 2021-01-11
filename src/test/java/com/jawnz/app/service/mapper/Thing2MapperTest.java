package com.jawnz.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class Thing2MapperTest {

    private Thing2Mapper thing2Mapper;

    @BeforeEach
    public void setUp() {
        thing2Mapper = new Thing2MapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = "id1";
        assertThat(thing2Mapper.fromId(id).getId()).isEqualTo(id);
        assertThat(thing2Mapper.fromId(null)).isNull();
    }
}
