package com.habi.boot.generator.service;


import com.habi.boot.generator.dto.GeneratorInfo;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IHabiGeneratorService {
    List<String> showTables();

    int generatorFile(GeneratorInfo var1);
}
