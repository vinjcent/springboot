package com.vinjcent.config.shiro;


import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
public class ShiroConfiguration {

    // 1.创建shiroFilter  负责拦截请求
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            @Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager){


        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 让管理者所管理的用户过滤生效
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        // 配置shiro的内置过滤器
        /*
         *   anon:  无需认证即可访问
         *   authc: 必须认证才能用
         *   user:  必须拥有 “记住我” 功能才能用
         *   perms: 拥有对某个资源的权限才能用
         *   role:  拥有某个角色权限才能访问
         */

        // 由于过滤器链是一个链结构,使用linkedHashMap比较合适
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/login","anon");
        filterMap.put("/","anon");

        // 登陆后授权,正常情况下没有授权会跳转到未授权页面
        filterMap.put("/toAdd","perms[addPerm]");
        filterMap.put("/toDelete","perms[deletePerm]");
        filterMap.put("/toQuery","perms[queryPerm]");
        filterMap.put("/toUpdate","perms[updatePerm]");


        // 设置注销过滤器
        filterMap.put("/logout","logout");
        // 如果没有认证,拦截所有请求
        filterMap.put("/**", "authc");

        /*
         *   /** 匹配所有的路径
         *   通过Map集合组成了一个拦截器链,自顶向下过滤,一旦匹配,则不再执行下面的过滤
         *   如果下面的定义与上面冲突,那按照了谁先定义谁说了算
         *   所以/** 一定要配置在最后
         *   这里是否要对所有路径进行认证视情况而定,因为一些路由跳转可能在没登陆出现导致出错
         */

        // 设置登录的请求
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        // 未授权跳转的页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/noauth");

        // 设置shiro过滤器链
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return shiroFilterFactoryBean;
    }

    // 2.web安全管理者(这里的使用DefaultWebSecurityManager,该类继承了DefaultSecurityManager)
    @Bean(name = "defaultWebSecurityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(
            @Qualifier("userRealm") UserRealm userRealm,
            @Qualifier("webSessionManager") DefaultWebSessionManager webSessionManager,
            @Qualifier("cookieRememberMeManager") CookieRememberMeManager rememberMeManager,
            @Qualifier("redisCacheManager") RedisCacheManager redisCacheManager) {
            // @Qualifier("ehCacheManager") EhCacheManager ehCacheManager){

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 让管理者管理userRealm
        securityManager.setRealm(userRealm);
        // 配置webSessionManager
        securityManager.setSessionManager(webSessionManager);
        // 关联rememberMe
        securityManager.setRememberMeManager(rememberMeManager);
        // shiro自带的缓存(不推荐使用)
        // securityManager.setCacheManager(cacheManager);
        // ehcache缓存(推荐)
        // securityManager.setCacheManager(ehCacheManager);
        // redis缓存
        securityManager.setCacheManager(redisCacheManager);

        return securityManager;
    }

    // 3.配置Realm对象
    @Bean(name = "userRealm")
    public UserRealm userRealm(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher credentialsMatcher){

        UserRealm userRealm = new UserRealm();
        // 给realm配置凭证校验匹配器
        userRealm.setCredentialsMatcher(credentialsMatcher);
        // 开启缓存
        userRealm.setCachingEnabled(true);
        // 启用身份认证缓存,即缓存AuthenticationInfo信息,默认false
        userRealm.setAuthenticationCachingEnabled(true);
        // 设置AuthenticationInfo信息的缓存名称,在ehcache-shiro.xml文件中有对应缓存的配置
        userRealm.setAuthenticationCacheName("authenticationCache");
        // 启用身份授权缓存,即缓存AuthorizationInfo信息,默认false
        userRealm.setAuthorizationCachingEnabled(true);
        // 设置AuthorizationInfo信息的缓存名称,在ehcache-shiro.xml文件中有对应缓存的配置
        userRealm.setAuthorizationCacheName("authorizationCache");

        return userRealm;
    }

    // ShiroDialect: 用来整合前端标签 shiro-thymeleaf
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }

    // 密码匹配凭证管理器
    @Bean(name = "hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {

        // HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();  // 默认匹配方法
        RetryLimitHashedCredentialsMatcher hashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher();
        // 设置加密算法为md5
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        // 设置散列次数
        hashedCredentialsMatcher.setHashIterations(1024);
        // storedCredentialsHexEncoded默认是true,此时用的是密码加密用的是Hex编码: false时用Base64编码
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        // 最大重试次数3次
        hashedCredentialsMatcher.setMaxRetryCount(3);

        return hashedCredentialsMatcher;
    }

    // 配置webSessionManager
    @Bean(name="webSessionManager")
    public DefaultWebSessionManager webSessionManager(@Qualifier("redisSessionDAO") RedisSessionDAO redisSessionDAO){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 将sessionIdUrlRewritingEnabled属性设置成false
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        // 配置redisSessionDao(添加地方)
        sessionManager.setSessionDAO(redisSessionDAO);

        return sessionManager;
    }

    // 记住我配置
    @Bean(name = "cookieRememberMeManager")
    public CookieRememberMeManager rememberMeManager(){
        // cookie管理器
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        // cookie的名字
        SimpleCookie simpleCookie = new SimpleCookie("shiro-rememberMe");
        // 设置有效期时间30天(单位秒)
        simpleCookie.setMaxAge(2592000);
        cookieRememberMeManager.setCookie(simpleCookie);
        // rememberMe cookie加密的密钥,建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        // cookieRememberMeManager.setCipherKey用来设置加密的Key,参数类型byte[],字节数组长度要求16
        cookieRememberMeManager.setCipherKey("vinjcent30_ovo35".getBytes());
        // 也可以使用cookieRememberMeManager.setCipherKey(Base64.decode("16位"));

        return cookieRememberMeManager;
    }

    /**
    // shiro自带的MemoryConstrainedCacheManager作缓存
    // 但是只能用于本机,在集群时就无法使用,如果集群需要使用ehcache
    @Bean(name = "cacheManager")
    public CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager(); // 使用内存缓存
    }

    // 配置ehcache,推荐使用
    @Bean(name = "ehCacheManager")
    public EhCacheManager ehCacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        // 绑定缓存配置文件
        ehCacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
        return ehCacheManager;
    }
     */

    // 配置Redis缓存管理者
    @Bean(name = "redisCacheManager")
    public RedisCacheManager redisCacheManager(@Qualifier("redisManager") RedisManager redisManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        // 设置redisManager属性
        redisCacheManager.setRedisManager(redisManager);
        // 用户权限信息缓存时间(单位:秒)
        redisCacheManager.setExpire(200000);
        return redisCacheManager;
    }


    // 配置redisSessionDAO
    @Bean(name = "redisSessionDAO")
    public RedisSessionDAO redisSessionDAO(@Qualifier("redisManager") RedisManager redisManager) {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        // 设置redisManager属性
        redisSessionDAO.setRedisManager(redisManager);
        return redisSessionDAO;
    }

    // 配置redis管理者
    @Bean(name = "redisManager")
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        return redisManager;
    }


}
