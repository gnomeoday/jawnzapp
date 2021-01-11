package com.jawnz.app.service.mapper;


import com.jawnz.app.domain.*;
import com.jawnz.app.service.dto.Thing2DTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Thing2} and its DTO {@link Thing2DTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface Thing2Mapper extends EntityMapper<Thing2DTO, Thing2> {


    @Mapping(target = "child", ignore = true)
    Thing2 toEntity(Thing2DTO thing2DTO);

    default Thing2 fromId(String id) {
        if (id == null) {
            return null;
        }
        Thing2 thing2 = new Thing2();
        thing2.setId(id);
        return thing2;
    }
}
