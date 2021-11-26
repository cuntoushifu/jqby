package top.ahdx.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.ahdx.pojo.Friend;
import top.ahdx.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 杨烨
 * @since 2021-01-18
 */
@Controller
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;

    @GetMapping("/{page}/{limit}")
    public String blogListPage(
            @PathVariable int page,
            @PathVariable int limit,
            Model model) {

        if (page < 1){
            page = 1;
        }
        Page<Friend> pageParam = new Page<>(page, limit);
        friendService.page(pageParam, new QueryWrapper<Friend>().orderByDesc("gmt_create"));

        // 结果
        List<Friend> friends = pageParam.getRecords();
        model.addAttribute("friends", friends);
        model.addAttribute("pageParam", pageParam);


        return "friend/list";
    }
}

