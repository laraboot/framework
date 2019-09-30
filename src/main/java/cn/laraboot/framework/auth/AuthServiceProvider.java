package cn.laraboot.framework.auth;

import cn.laraboot.framework.contracts.auth.GuardProvider;
import org.springframework.beans.factory.FactoryBean;

public class AuthServiceProvider implements FactoryBean<AuthManager> {

    private GuardProvider guardProvider;

    public AuthServiceProvider(GuardProvider guardProvider) {
        this.guardProvider = guardProvider;
    }

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
