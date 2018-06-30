package com.kcoin.common;

import com.jfinal.config.Routes;
import com.kcoin.user.UserController;

/**
 * front routes
 */
public class FrontRoutes extends Routes {

    public void config() {
        setBaseViewPath("/views");

        add("/user", UserController.class);
    }
}
