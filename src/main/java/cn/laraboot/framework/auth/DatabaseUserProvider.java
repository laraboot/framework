package cn.laraboot.framework.auth;

import cn.laraboot.framework.contracts.auth.AuthenticateAble;
import cn.laraboot.framework.contracts.auth.RememberToken;
import cn.laraboot.framework.contracts.auth.RetrieveAble;
import cn.laraboot.framework.contracts.auth.UserProvider;
import org.springframework.data.repository.CrudRepository;

import java.util.Map;

public class DatabaseUserProvider<T extends AuthenticateAble> implements UserProvider {
    private CrudRepository<T, Long> userRepository;

    public DatabaseUserProvider(CrudRepository<T, Long> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AuthenticateAble retrieveById(String identifier) {
        return userRepository.findById(Long.valueOf(identifier)).orElse(null);
    }

    @Override
    public AuthenticateAble retrieveByToken(String identifier, String token) {
        if (userRepository instanceof RememberToken) {
            return ((RememberToken) userRepository).retrieveByToken(identifier, token);
        }
        return null;
    }

    @Override
    public void updateRememberToken(AuthenticateAble user, String token) {
        if (userRepository instanceof RememberToken) {
            ((RememberToken) userRepository).updateRememberToken(user, token);
        }
    }

    @Override
    public AuthenticateAble retrieveByCredentials(Map<String, String> credentials) {
        if (userRepository instanceof RetrieveAble) {
            return ((RetrieveAble) userRepository).retrieveByCredentials(credentials);
        }
        return userRepository.findById(Long.parseLong(credentials.get("id"))).orElse(null);
    }

    @Override
    public boolean validateCredentials(AuthenticateAble user, Map<String, String> credentials) {
        return false;
    }
}
