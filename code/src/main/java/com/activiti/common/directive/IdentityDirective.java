package com.activiti.common.directive;

import com.activiti.common.utils.CommonUtil;
import com.activiti.service.UserService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * freemarker 前端和后台数据交互（判断当前用户类型)
 */
@Component("identityDirective")
public class IdentityDirective implements TemplateDirectiveModel {
    @Autowired
    private CommonUtil commonUtil;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels, TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        String email = map.get("userEmail") == null ? "" : map.get("userEmail").toString();
        int id = map.get("id") == null ? 0 : Integer.valueOf(map.get("id").toString());
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_26);
        boolean identity = false;
        if (commonUtil.isManageRole(email, id)) {
            identity = true;
        }
        environment.setVariable("identity", builder.build().wrap(identity));
        templateDirectiveBody.render(environment.getOut());
    }
}
