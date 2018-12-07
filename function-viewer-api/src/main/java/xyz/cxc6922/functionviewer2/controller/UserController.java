package xyz.cxc6922.functionviewer2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.cxc6922.functionviewer2.entity.dto.RestApiResult;
import xyz.cxc6922.functionviewer2.entity.po.User;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private ScalaController scalaController;

    @RequestMapping("getCurrentUserInfo")
    @ResponseBody
    public RestApiResult getCurrentUserInfo() {
        scalaController.newTask();
        return new RestApiResult(new User());
    }
}
