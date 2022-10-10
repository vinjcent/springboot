package com.vinjcent.service;

import com.vinjcent.pojo.Menu;

import java.util.List;

public interface MenuService {

    // 获取所有菜单与所需角色信息
    List<Menu> getAllMenus();

}
