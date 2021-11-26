package top.ahdx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.ahdx.pojo.Friend;
import top.ahdx.service.FriendService;
import top.ahdx.utils.AhdxUtils;
import top.ahdx.utils.Res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2021/1/21
 */
@Controller
@RequestMapping("/admin/friend")
public class AdminFriendController {
    @Autowired
    private FriendService friendService;

    @GetMapping("/{page}/{limit}")
    public String friends(
            @PathVariable int page,
            @PathVariable int limit,
            Model model) {

        if (page < 1) {
            page = 1;
        }
        Page<Friend> pageParam = new Page<>(page, limit);
        friendService.page(pageParam, new QueryWrapper<Friend>().orderByDesc("gmt_create"));

        // 结果
        List<Friend> friends = pageParam.getRecords();
        model.addAttribute("friends", friends);
        model.addAttribute("pageParam", pageParam);
        return "admin/friend/friends";
    }

    @GetMapping("/toadd")
    public String toAdd() {
        return "admin/friend/addFriend";
    }

    @ResponseBody
    @PostMapping("/add")
    public Res add(@RequestBody Friend friend) {
        Res res = new Res();
        if (StringUtils.isEmpty(friend.getName())) {
            res.setFlag(false);
            res.setMessage("名字不能为空");
            return res;
        }
        if (friend.getBirthday() == null) {
            res.setFlag(false);
            res.setMessage("生日不能为空");
            return res;
        }
        if (StringUtils.isEmpty(friend.getMyself())) {
            res.setFlag(false);
            res.setMessage("交友宣言不能为空");
            return res;
        }
        if (StringUtils.isEmpty(friend.getImg())) {
            res.setFlag(false);
            res.setMessage("请上传图片");
            return res;
        }
        if (StringUtils.isEmpty(friend.getAddress())) {
            res.setFlag(false);
            res.setMessage("住址不能为空");
            return res;
        }
        System.out.println(friend);
        friend.setGmtCreate(AhdxUtils.getTime());
        friendService.save(friend);

        res.setFlag(true);
        res.setMessage("成功发布");
        return res;
    }

    @GetMapping("/del/{id}")
    public String delDownload(@PathVariable int id, RedirectAttributes attributes) {
        friendService.removeById(id);
        attributes.addAttribute("msg", "成功删除");
        return "redirect:/admin/friend/1/10";
    }
}
