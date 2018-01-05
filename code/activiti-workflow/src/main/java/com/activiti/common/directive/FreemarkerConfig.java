package com.activiti.common.directive;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FreemarkerConfig {
    @Autowired
    private Configuration configuration;
    @Autowired
    private IdentityDirective identityDirective;

    @PostConstruct
    public void setSharedVariable() throws TemplateModelException {
        configuration.setSharedVariable("identity_validate", identityDirective);
    }
}
