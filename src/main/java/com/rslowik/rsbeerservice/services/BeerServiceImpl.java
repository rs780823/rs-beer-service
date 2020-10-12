package com.rslowik.rsbeerservice.services;

import com.rslowik.rsbeerservice.domain.Beer;
import com.rslowik.rsbeerservice.ex.NotFoundException;
import com.rslowik.rsbeerservice.repositories.BeerRepository;
import com.rslowik.rsbeerservice.web.dto.BeerDto;
import com.rslowik.rsbeerservice.web.mappers.BeerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper mapper;

    @Override
    public BeerDto getBeerById(UUID beerId) {
        return mapper.beerToBeerDto(
                beerRepository.findById(beerId).orElseThrow(NotFoundException::new)
        );
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        return mapper.beerToBeerDto(
                beerRepository.save(
                        mapper.beerDtoToBeer(beerDto)
                )
        );
    }

    @Override
    public void updateBeer(UUID beerId, BeerDto beerDto) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);
        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());
        beerRepository.save(beer);
    }
}
