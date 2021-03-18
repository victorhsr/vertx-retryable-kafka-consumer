package io.github.victorhsr.retry.recommendations.application.kafka.recommendations.dto;

import io.github.victorhsr.retry.person.event.Person;
import io.github.victorhsr.retry.recommendations.processor.dto.PersonData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
@Mapper
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonData personToPersonData(final Person person);

}
