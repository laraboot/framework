package cn.laraboot.framework.contracts.kernel.pipeline;

public interface Dockable {
    public Object handle(Object passable, Stack stack) throws Throwable;
}
