package com.mho_toys.backend.controller;

import com.mho_toys.backend.dto.ToyDTO;
import com.mho_toys.backend.service.ToyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/toys")
public class ToyController {

    @Autowired
    private ToyService toyService;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping
    public List<ToyDTO> getAllToys() {

        List<ToyDTO> toys = toyService.getAllToys();
        return new ResponseEntity<>(toys, HttpStatus.OK).getBody();
    }



}
