package cn.laraboot.framework.encryption;

import cn.laraboot.framework.support.DriverException;
import cn.laraboot.framework.support.Manager;
import cn.laraboot.framework.contracts.encryption.DecryptException;
import cn.laraboot.framework.contracts.encryption.EncryptException;
import cn.laraboot.framework.contracts.encryption.Encrypter;
import cn.laraboot.framework.contracts.kernel.SecretProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class EncrypterManager extends Manager<Encrypter> implements Encrypter {

    @Autowired
    SecretProvider secretProvider;

    /**
     * 设置驱动供应商
     */
    @Override
    public void setDriverProviders() {
        driverProviders = new HashMap<>();
        driverProviders.put("aes", () -> new AesEncrypter(secretProvider));
        driverProviders.put("des", () -> new DesEncrypter(secretProvider));
    }

    /**
     * 获取默认驱动
     *
     * @return 驱动名
     */
    @Override
    protected String defaultDriver() {
        return "aes";
    }

    @Override
    public String encrypt(String value) throws EncryptException {
        try {
            return driver().encrypt(value);
        } catch (DriverException e) {
            throw new EncryptException(e.getMessage());
        }
    }

    @Override
    public String decrypt(String value) throws DecryptException {
        try {
            return driver().decrypt(value);
        } catch (DriverException e) {
            throw new DecryptException(e.getMessage());
        }
    }
}
