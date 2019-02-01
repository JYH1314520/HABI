package com.habi.boot.generator.service;


import com.habi.boot.generator.dto.GeneratorInfo;
import com.habi.boot.generator.service.impl.ListValue;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IHabiGeneratorService {
    List<ListValue> showTables();

    int generatorFile(GeneratorInfo var1);
}
