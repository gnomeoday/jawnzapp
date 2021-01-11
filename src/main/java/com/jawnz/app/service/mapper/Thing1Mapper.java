package com.jawnz.app.service.mapper;


import com.jawnz.app.domain.*;
import com.jawnz.app.service.dto.Thing1DTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Thing1} and its DTO {@link Thing1DTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface Thing1Mapper extends EntityMapper<Thing1DTO, Thing1> {


    @Mapping(target = "parent", ignore = true)
    Thing1 toEntity(Thing1DTO thing1DTO);

    default Thing1 fromId(String id) {
        if (id == null) {
            return null;
        }
        Thing1 thing1 = new Thing1();
        thing1.setId(id);
        return thing1;
    }
}
