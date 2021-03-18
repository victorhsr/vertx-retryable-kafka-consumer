package io.github.victorhsr.retry.person.application.dto;

import io.github.victorhsr.retry.person.event.Person;
import io.github.victorhsr.retry.person.application.v1.registration.dto.RegisterPersonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Mapper
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    Person registerDTOToPerson(final RegisterPersonDTO registerPersonDTO);

}
