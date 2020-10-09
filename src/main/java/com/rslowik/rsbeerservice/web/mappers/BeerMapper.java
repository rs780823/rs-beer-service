package com.rslowik.rsbeerservice.web.mappers;


import com.rslowik.rsbeerservice.domain.Beer;
import com.rslowik.rsbeerservice.web.dto.BeerDto;
import org.mapstruct.Mapper;

@Mapper(uses = DateMapper.class)
public interface BeerMapper {
    BeerDto beerToBeerDto(Beer beer);

    Beer beerDtoToBeer(BeerDto beerDto);
}
