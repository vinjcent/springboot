package com.vinjcent.config.shiro;

import com.vinjcent.utils.RedisUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings("all")
// 重写HashedCredentialsMatcher密码匹配凭证管理器
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    // 设置redis里的key的统一前缀
    private static final String PREFIX  = "USER_LOGIN_FAIL:";
    // 默认最大尝试次数为5次
    private static Integer DEFAULT_MAX_RETRY_COUNT = 5;
    // 定义失败次数
    private Integer MAX_RETRY_COUNT = DEFAULT_MAX_RETRY_COUNT;
    // 为当前类设置日志打印在控制台
    private static final Logger LOGGER = getLogger(RetryLimitHashedCredentialsMatcher.class);

    // 注入redis工具类
    @Autowired
    private RedisUtils redisUtils;

    public RetryLimitHashedCredentialsMatcher() { }

    public void setMaxRetryCount(Integer maxRetryCount) {
        MAX_RETRY_COUNT = maxRetryCount;
    }

    // 重写校验方法
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws ExcessiveAttemptsException {

        // 获取登录用户名
        final String username = (String) token.getPrincipal();

        // 先查看是否redis缓存中是否已有登录次数缓存
        Integer retryCount = (Integer) redisUtils.get(PREFIX + username);

        // 如果之前没有登录缓存,则创建一个登录次数缓存
        if (retryCount == null) {
            retryCount = 1;
        }

        // 将缓存记录的登录次数加1
        retryCount++;

        // 如果有且次数已经超过限制,则驳回本次登录请求,并在控制台打印信息,抛出异常
        if (retryCount > MAX_RETRY_COUNT) {
            LOGGER.error("登录次数超过限制");
            throw new ExcessiveAttemptsException("用户:" + username + "登录次数已经超过限制");
        }

        // 并将其保存到缓存中,设置当前的key有效时间为30s
        redisUtils.set(PREFIX + username, retryCount, 30L);

        // 调用父类验证器,判断是否登录成功
        boolean isMatcher = super.doCredentialsMatch(token, info);

        // 如果成功则清除redis缓存的数据
        if (isMatcher) {
            redisUtils.del(PREFIX + username);
        }
        return isMatcher;

    }

}
