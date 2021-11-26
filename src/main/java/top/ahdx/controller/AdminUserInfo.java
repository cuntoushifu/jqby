package top.ahdx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.ahdx.pojo.UserInfo;
import top.ahdx.service.UserInfoService;
import top.ahdx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2021/1/20
 */
@Controller
@RequestMapping("/admin/user")
public class AdminUserInfo {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserService userService;

    @GetMapping("/findList/{page}/{limit}")
    public String questionListPage(
            @PathVariable int page,
            @PathVariable int limit,
            Model model){

        if (page < 1){
            page = 1;
        }
        Page<UserInfo> pageParam = new Page<>(page, limit);
        userInfoService.page(pageParam,new QueryWrapper<UserInfo>().orderByDesc("gmt_create"));

        // 结果
        List<UserInfo> userInfos = pageParam.getRecords();
        model.addAttribute("userInfos",userInfos);
        model.addAttribute("pageParam",pageParam);


        return "admin/user/users";
    }
    @GetMapping("/del/{uid}")
    public String delUser(@PathVariable String uid, RedirectAttributes attributes) {


        Integer i = userService.deleteUser(uid);

        if (i == -1) {
            attributes.addAttribute("msg", "成功删除！恢复账号请联系站长");
        }
        if (i == 0) {
            attributes.addAttribute("err", "删除失败！可能是重复删除");
        }

        if (i == 1) {
            attributes.addAttribute("err", "删除失败！该账号为管理员账号，请联系站长删除！");
        }

        return "redirect:/admin/user/findList/1/10";
    }
}
