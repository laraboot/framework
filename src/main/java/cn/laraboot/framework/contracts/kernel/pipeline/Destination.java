package cn.laraboot.framework.contracts.kernel.pipeline;

public interface Destination {
    public Object handle(Object traveler) throws Throwable;
}
