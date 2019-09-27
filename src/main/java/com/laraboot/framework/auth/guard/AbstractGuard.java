package com.laraboot.framework.auth.guard;

import com.laraboot.framework.contracts.auth.Guard;
import com.laraboot.framework.contracts.auth.AuthenticateAble;

import java.util.HashMap;
import java.util.Map;

abstract public class AbstractGuard implements Guard {

    protected AuthenticateAble user;

    protected Map<String, String> credentials = new HashMap<>();

    @Override
    public AbstractGuard clone() throws CloneNotSupportedException {
        AbstractGuard guard = (AbstractGuard) super.clone();

        guard.user = null;
        guard.credentials = new HashMap<>();

        return guard;
    }

    @Override
    public AuthenticateAble user(boolean get) {
        if (get) {
            return user;
        }
        return user();
    }
}
