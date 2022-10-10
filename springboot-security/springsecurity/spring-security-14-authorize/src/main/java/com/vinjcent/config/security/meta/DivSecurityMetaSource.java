package com.vinjcent.config.security.meta;

import com.vinjcent.pojo.Menu;
import com.vinjcent.pojo.Role;
import com.vinjcent.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * 自定义 url 权限元数据的数据源
 */
@Component
public class DivSecurityMetaSource implements FilterInvocationSecurityMetadataSource {


    private final MenuService menuService;

    @Autowired
    public DivSecurityMetaSource(MenuService menuService) {
        this.menuService = menuService;
    }

    // 用于路径对比
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 自定义动态资源权限数据源信息
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 1.获取请求资源路径
        String requestURI = ((FilterInvocation) object).getRequest().getRequestURI();
        // 2.获取数据库中所有菜单
        List<Menu> menus = menuService.getAllMenus();
        // 3.循环对比数据库中的路径与当前uri是否匹配
        for (Menu menu : menus) {
            // 如果匹配成功
            if (antPathMatcher.match(menu.getPattern(), requestURI)) {
                // 转链表为数组
                String[] roles = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
                // 返回角色集合
                return SecurityConfig.createList(roles);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
