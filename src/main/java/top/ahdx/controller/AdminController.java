package top.ahdx.controller;

import top.ahdx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2021/1/19
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @RequestMapping
    public String admin() {

        return "admin/index";
    }

//    @RequestMapping("/user/findList")
//    public String findList(Model model) {
//        List<UserInfo> userInfos = userService.findUserInfoByRolesIsNotAdmin();
//        model.addAttribute("userInfos", userInfos);
//        return "admin/user/users";
//    }





}
