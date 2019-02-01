package com.habi.boot.generator.controllers;


import com.habi.boot.generator.dto.GeneratorInfo;
import com.habi.boot.generator.service.IHabiGeneratorService;
import com.habi.boot.system.base.BaseController;
import com.habi.boot.system.base.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/api/generator"})
public class HabiGeneratorController extends BaseController {
    @Autowired
    IHabiGeneratorService iHabiGeneratorService;

    public HabiGeneratorController() {
    }

    @RequestMapping(
            value = {"/alltables"},
            method = {RequestMethod.GET}
    )
    @ResponseBody
    public ResponseData showTables() {
        return new ResponseData(this.iHabiGeneratorService.showTables());
    }

    @RequestMapping({"/newtables"})
    @ResponseBody
    public int generatorTables(GeneratorInfo generatorInfo) {
        int rs = this.iHabiGeneratorService.generatorFile(generatorInfo);
        return rs;
    }
}
