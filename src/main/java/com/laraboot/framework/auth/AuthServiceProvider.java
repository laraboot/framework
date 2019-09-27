package com.laraboot.framework.auth;

import com.laraboot.framework.contracts.auth.GuardProvider;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthServiceProvider implements FactoryBean<AuthManager> {

    @Autowired
    private GuardProvider guardProvider;

    @Override
    public Class<?> getObjectType() {
        return AuthManager.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public AuthManager getObject() throws Exception {
        return new AuthManager(guardProvider);
    }
}
