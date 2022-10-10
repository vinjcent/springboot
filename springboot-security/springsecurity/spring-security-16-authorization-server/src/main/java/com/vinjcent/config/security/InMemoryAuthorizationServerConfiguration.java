//package com.vinjcent.config.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//
///**
// * 自定义授权服务配置
// */
//@Configuration
//@EnableAuthorizationServer  //  指定当前应用为授权服务器
//public class InMemoryAuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
//
//    // 认证数据源
//    private final UserDetailsService userDetailsService;
//
//    // 认证管理者
//    private final AuthenticationManager authenticationManager;
//
//    // 不支持明文的方式进行认证
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public InMemoryAuthorizationServerConfiguration(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
//        this.passwordEncoder = passwordEncoder;
//        this.userDetailsService = userDetailsService;
//        this.authenticationManager = authenticationManager;
//    }
//
//    // 用来配置授权服务器可以为哪些客户端授权  ===> clientId、secret、redirectURI 使用那种授权模式
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                .withClient("clientId")
//                .secret(passwordEncoder.encode("secret"))   // 注册客户端的密钥
//                .redirectUris("http://www.baidu.com")
//                .authorizedGrantTypes("authorization_code", "refresh_token", "implicit", "password", "client_credentials") // 授权服务器支持的模式,仅支持授权码模式
//                .scopes("read:user");        // 令牌允许获取的资源权限
//    }
//
//    // 配置授权服务器使用哪个 UserDetailsService(用于刷新令牌,如果不配置则会报错)
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.userDetailsService(userDetailsService);   // 注入 userDetailsService
//        endpoints.authenticationManager(authenticationManager); // 注入 authenticationManager
//    }
//
//    /**
//     * 授权码模式
//     *  1.请求用户是否授权  /oauth/authorize
//     *  完整路径: http://localhost:3035/oauth/authorize?client_id=clientId&response_type=code&redirect_uri=http://www.baidu.com
//     *
//     *  2.授权之后根据获取的授权码获取令牌  /oauth/token    携带参数===>clientId secret grant_type redirectUri code
//     *  完整路径: curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d 'grant_type=authorization_code&code=3FZI5F&redirect_uri=http://www.baidu.com' "http://clientId:secret@localhost:3035/oauth/token"
//     *
//     *  3.支持刷新令牌    /oauth/token    携带参数===> clientId secret grant_type refresh_token
//     *  完整路径: curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d 'grant_type=refresh_token&refresh_token=&redirect_uri=http://www.baidu.com' "http://clientId:secret@localhost:3035/oauth/token"
//     */
//}
