package com.nttdata.knot.codeapi.Interfaces;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.knot.codeapi.Models.Code.Code;
import com.nttdata.knot.codeapi.Models.ComponentPackage.Component;
import com.nttdata.knot.codeapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;

import reactor.core.publisher.Mono;


public interface ICodeService {
    

    Mono<Code> createCodeAsync(String org, String area, String product, Component component) throws JsonProcessingException;

    Mono<DeleteGithubFileRequest> deleteCodeAsync(String org, String area, String product, String deletedComponentName);

    Mono<Code> updateCodeAsync(String org, String area, String product, Component component) throws JsonProcessingException;


    
}
