package top.ahdx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import top.ahdx.pojo.Invite;
import top.ahdx.pojo.User;
import top.ahdx.pojo.UserInfo;
import top.ahdx.service.InviteService;
import top.ahdx.service.UserInfoService;
import top.ahdx.service.UserService;
import top.ahdx.utils.AhdxUtils;
import top.ahdx.vo.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class LoginController {
    private String imgBaseUrl = "https://gitee.com/programhero/myblogpic/raw/master/xhimg/";
    @Autowired
    InviteService inviteService;
    @Autowired
    UserService userService;
    @Autowired
    UserInfoService userInfoService;

    @GetMapping({"/","/index"})
    public String index(Model model){
        ArrayList<String> imgUrls = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            imgUrls.add(imgBaseUrl + i + ".jpg");
        }
        model.addAttribute("imgs", imgUrls);
        return "index";

    }

    @GetMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @GetMapping("/register")
    public String toRegister(){
        return "register";
    }

    // 注册业务
    @PostMapping("/register")
    public String register(RegisterForm registerForm, Model model){
        AhdxUtils.print("注册表单信息："+registerForm.toString());
        // 表单密码重复判断
        if (!registerForm.getPassword().equals(registerForm.getRepassword())){
            model.addAttribute("registerMsg","密码输入有误");
            return "register";
        }
        // 用户名已存在
        User hasUser = userService.getOne(new QueryWrapper<User>().eq("username", registerForm.getUsername()));
        if (hasUser!=null){
            model.addAttribute("registerMsg","用户名已存在");
            return "register";
        }

        // 验证邀请码
        Invite invite = inviteService.getOne(new QueryWrapper<Invite>().eq("code", registerForm.getCode()));
        if (invite==null){
            model.addAttribute("registerMsg","邀请码不存在");
            return "register";
        }else {
            // 邀请码存在，判断邀请码是否有效
            if (invite.getStatus()==1){
                model.addAttribute("registerMsg","邀请码已被使用");
                return "register";
            }else {
                // 构建用户对象
                User user = new User();
                user.setUid(AhdxUtils.getUuid()); // 用户唯一id
                user.setRoleId(2);
                user.setDeleted(0);
                user.setUsername(registerForm.getUsername());
                // 密码加密
                String bCryptPassword = new BCryptPasswordEncoder().encode(registerForm.getPassword());
                user.setPassword(bCryptPassword);
                user.setGmtCreate(AhdxUtils.getTime());
                user.setLoginDate(AhdxUtils.getTime());
                // 保存对象！
                userService.save(user);
                AhdxUtils.print("新用户注册成功："+user);

                // 激活邀请码
                invite.setActiveTime(AhdxUtils.getTime());
                invite.setStatus(1);
                invite.setUid(user.getUid());
                inviteService.updateById(invite);

                // todo: 用户信息

                userInfoService.save(new UserInfo().setUid(user.getUid()).setDeleted(0).setGmtCreate(AhdxUtils.getTime()));

                // 注册成功，重定向到登录页面
                return "redirect:/toLogin";
            }
        }
    }

}
