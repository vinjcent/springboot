package com.vinjcent;

import com.vinjcent.pojo.Menu;
import com.vinjcent.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringSecurity14AuthorizeApplicationTests {

    @Autowired
    MenuService menuService;

    @Test
    void contextLoads() {
        List<Menu> allMenus = menuService.getAllMenus();
        allMenus.forEach(m -> {
            System.out.println(m.toString());
        });
    }

}
