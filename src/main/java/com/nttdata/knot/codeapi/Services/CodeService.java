package com.nttdata.knot.codeapi.Services;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import com.nttdata.knot.codeapi.Models.Code.Code;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.nttdata.knot.codeapi.Interfaces.ICodeService;
import com.nttdata.knot.codeapi.Interfaces.IGithubService;
import com.nttdata.knot.codeapi.Models.ComponentPackage.Component;
import com.nttdata.knot.codeapi.Models.GithubPackage.GithubFileRequest.Committer;
import com.nttdata.knot.codeapi.Models.GithubPackage.GithubFileRequest.CreateGithubFileRequest;
import com.nttdata.knot.codeapi.Models.GithubPackage.GithubFileRequest.DeleteGithubFileRequest;

import reactor.core.publisher.Mono;

@Service
public class CodeService implements ICodeService {

        private String repoName = "knot-onboarding-resources";
        private IGithubService githubService;

        public CodeService(IGithubService githubService) {
                this.githubService = githubService;
        }

        @Override
        public Mono<Code> createCodeAsync(String org, String area, String product, Component component) throws JsonProcessingException {

                Code code = new Code(component.getTechnology(), component.getId(), component.getId(),
                                component.getDescription(),
                                component.getTechnology(), component.getOrganizationName(),
                                component.getSonarqubeScan(),
                                component.getTechnology(),
                                component.getAutomationTool(), component.getGitBranching(),
                                component.getContainerRegistry(),
                                component.getArtifactRegistry(), component.getUsers(),
                                component.getDevcontainers(),
                                component.getCodespaces());

                // prepare the verticals values commit
                var values_alm = prepareValueForCommit(component, code);

                // push the values of each vertical in knot-onboarding-resources
                this.githubService.createGithubFileAsync(values_alm, repoName,
                "products/" + org + "/" + area + "/" + product + "/" + component.getId() + "/code/values.yaml").block();

                return Mono.just(code);
        }

        @Override
        public Mono<DeleteGithubFileRequest> deleteCodeAsync(String org, String area, String product, String deletedComponentName) {
                // get the file to delete
                var valuesFile = this.githubService.getGithubFileAsync(repoName,
                                "products/" + org + "/" + area + "/" + product + "/" + deletedComponentName + "/code/values.yaml").block();

                // set the commit
                Committer committer = new Committer();
                committer.setEmail("41898282+github-actions[bot]@users.noreply.github.com");
                committer.setName("github-actions[bot]");

                DeleteGithubFileRequest deleteGithubFileRequest = new DeleteGithubFileRequest();
                deleteGithubFileRequest.setMessage("Removing Component, with name " + deletedComponentName);
                deleteGithubFileRequest.setCommitter(committer);
                deleteGithubFileRequest.setSha(valuesFile.getSha());

                // delete the file
                this.githubService.deleteGithubFileAsync(deleteGithubFileRequest,
                                repoName,
                                "products/" + org + "/" + area + "/" + product + "/" + deletedComponentName + "/code/values.yaml").block();

                return Mono.just(deleteGithubFileRequest);
        }

        @Override
        public Mono<Code> updateCodeAsync(String org, String area, String product, Component component) throws JsonProcessingException {
                 // Populate the code object
                Code code = new Code(component.getTechnology(), component.getId(), component.getId(),
                                component.getDescription(),
                                component.getTechnology(), component.getOrganizationName(),
                                component.getSonarqubeScan(),
                                component.getTechnology(),
                                component.getAutomationTool(), component.getGitBranching(),
                                component.getContainerRegistry(),
                                component.getArtifactRegistry(), component.getUsers(),
                                component.getDevcontainers(),
                                component.getCodespaces());

                // get the values file to update
                var valuesFile = this.githubService.getGithubFileAsync(repoName,
                "products/" + org + "/" + area + "/" + product + "/" + component.getId() + "/code/values.yaml").block();

                // prepare the verticals values commit and set its SHA
                var values_code = prepareValueForCommit(component, code);
                values_code.setSha(valuesFile.getSha());

                // push the values to the repository
                this.githubService.createGithubFileAsync(values_code, repoName,
                "products/" + org + "/" + area + "/" + product + "/" + component.getId() + "/code/values.yaml").block();

                  return Mono.just(code);
        }

        // serialize content of values and prepare a commit
        private CreateGithubFileRequest prepareValueForCommit(Component component, Object vertical)
                        throws JsonProcessingException {
                YAMLFactory yamlFactory = new YAMLFactory();
                yamlFactory.configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false);
                ObjectMapper objectMapper = new ObjectMapper(yamlFactory);

                String verticalInBase64String = Base64.getEncoder()
                                .encodeToString(objectMapper
                                                .writeValueAsString(vertical).getBytes(StandardCharsets.UTF_8));

                Committer committer = new Committer();
                committer.setEmail("41898282+github-actions[bot]@users.noreply.github.com");
                committer.setName("github-actions[bot]");

                CreateGithubFileRequest createGithubFileRequest = new CreateGithubFileRequest();
                createGithubFileRequest.setMessage("Add new Code vertical into a Component, with name " + component.getName());
                createGithubFileRequest.setCommitter(committer);
                createGithubFileRequest.setContent(verticalInBase64String);

                return createGithubFileRequest;

        }
}
