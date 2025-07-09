package com.mho_toys.backend.service;

import com.mho_toys.backend.dto.ToyDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ToyService {

    List<ToyDTO> getAllToys();
}
