package cn.laraboot.framework.contracts.auth.access;

public interface Authorizable {

    /**
     * Determine if the entity has a given ability.
     *
     * @param $ability ability
     * @return bool
     */
    public boolean can(String $ability);
}
