package com.mho_toys.backend.service.ServiceImpl;

import com.mho_toys.backend.dto.ToyDTO;
import com.mho_toys.backend.model.Toy;
import com.mho_toys.backend.repository.ToyRepository;
import com.mho_toys.backend.service.ToyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToyServiceImpl implements ToyService {

    @Autowired
    private ToyRepository toyRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ToyDTO> getAllToys() {

        List<Toy> toys = toyRepository.findAll();

        return toys.stream().map(toy-> modelMapper.map(toy, ToyDTO.class))
                .collect(Collectors.toList());

    }
}
