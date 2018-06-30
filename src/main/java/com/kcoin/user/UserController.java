package com.kcoin.user;

import com.jfinal.core.Controller;

public class UserController extends Controller {
    public void index() {
        renderJson(UserService.me.findAll());
    }
}


