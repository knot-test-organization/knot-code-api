package com.nttdata.knot.codeapi.Controllers;


import com.nttdata.knot.codeapi.Models.Code.Code;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.knot.codeapi.Interfaces.ICodeService;
import com.nttdata.knot.codeapi.Models.ComponentPackage.Component;
import com.nttdata.knot.codeapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;

@RestController
@RequestMapping("/code")
public class CodeController {

    private ICodeService codeService;
    private static final Logger logger = LoggerFactory.getLogger(Code.class);


    @Autowired
    public CodeController(ICodeService codeService) {
        this.codeService = codeService;
       
    }

    @PostMapping("/{org}/{area}/{product}")
    public  ResponseEntity<Mono<Code>> create(@PathVariable String org, @PathVariable String area, @PathVariable String product, @RequestBody Component component) throws JsonProcessingException {
         var deploy = codeService.createCodeAsync(org, area, product, component);
        return ResponseEntity.ok(deploy);
    }
    
     @DeleteMapping("/{org}/{area}/{product}/{name}")
    public  ResponseEntity<Mono<DeleteGithubFileRequest>> delete(@PathVariable String org, @PathVariable String area, @PathVariable String product, @PathVariable String name) throws JsonProcessingException {
          
        var code = codeService.deleteCodeAsync(org, area, product, name);
        logger.info("The component {} is being deleted", name);

        return ResponseEntity.ok(code);
    }

    @PutMapping("/{org}/{area}/{product}")
    public  ResponseEntity<Mono<Code>> update (@PathVariable String org, @PathVariable String area, @PathVariable String product, @RequestBody Component component) throws JsonProcessingException {
          
        var code = codeService.updateCodeAsync(org, area, product, component);
        logger.info("The component {} is being Updated", component.getName());

        return ResponseEntity.ok(code);
    }


}

