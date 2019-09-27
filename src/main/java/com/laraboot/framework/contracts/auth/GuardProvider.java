package com.laraboot.framework.contracts.auth;

import java.util.Map;

public interface GuardProvider {
    public Map<String, Guard> guards();

    public String defaultGuard();
}
