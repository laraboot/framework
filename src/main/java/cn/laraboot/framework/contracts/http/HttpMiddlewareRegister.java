package cn.laraboot.framework.contracts.http;

import cn.laraboot.framework.contracts.kernel.pipeline.Dockable;

import java.util.List;
import java.util.Map;

public interface HttpMiddlewareRegister {
    public Map<String, List<Dockable>> registerMiddlewareGroups();

    public List<Dockable> registerGlobalMiddlewares();
}
