package com.nttdata.knot.codeapi.Models.Code;

import java.util.List;


import com.nttdata.knot.codeapi.Models.UserPackage.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Code {

    private String projectType;
    private String name;
    private String repoName;
    private String repoDescription;
    private String repoType;
    private String organizationName;
    private Boolean sonarqubeScan;
    private String projectLanguage;
    private String automationTool;
    private String gitBranching;
    private String containerRegistry;
    private String artifactRegistry;
    private List<User> users;
    private List<String> devcontainers;
    private List<Codespace> codespaces;

     public Code(String projectType, String name, String repoName, String repoDescription, String repoType, String organizationName, Boolean sonarqubeScan, String projectLanguage, String automationTool, String gitBranching, String containerRegistry, String artifactRegistry, List<User> users, List<String> devcontainers, List<Codespace> codespaces) {
        this.projectType = projectType;
        this.name = name;
        this.repoName = repoName;
        this.repoDescription = repoDescription;
        this.repoType = repoType;
        this.organizationName = organizationName;
        this.sonarqubeScan = sonarqubeScan;
        this.projectLanguage = projectLanguage;
        this.automationTool = automationTool;
        this.gitBranching = gitBranching;
        this.containerRegistry = containerRegistry;
        this.artifactRegistry = artifactRegistry;
        this.users = users;
        this.devcontainers = devcontainers;
        this.codespaces = codespaces;
    }

    public Code() {

    }
}

