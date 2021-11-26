package top.ahdx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.ahdx.pojo.Blog;
import top.ahdx.pojo.BlogCategory;
import top.ahdx.service.BlogCategoryService;
import top.ahdx.service.BlogService;
import top.ahdx.utils.AhdxUtils;
import top.ahdx.vo.QuestionWriteForm;

import java.util.List;

/**
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2021/1/22
 */
@Controller
@RequestMapping("/admin/blog")
public class AdminBlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogCategoryService blogCategoryService;

    @GetMapping("/{page}/{limit}")
    public String blogListPage(
            @PathVariable int page,
            @PathVariable int limit,
            Model model) {

        if (page < 1) {
            page = 1;
        }
        Page<Blog> pageParam = new Page<>(page, limit);
        blogService.page(pageParam, new QueryWrapper<Blog>().orderByDesc("sort").orderByDesc("gmt_create"));

        // 结果
        List<Blog> blogList = pageParam.getRecords();
        model.addAttribute("blogList", blogList);
        model.addAttribute("pageParam", pageParam);

        // 分类信息

        return "admin/blog/list";
    }


    @GetMapping("/top/{id}")
    public String top(@PathVariable int id, RedirectAttributes attributes) {
        if (id == 0) {
            attributes.addAttribute("err", "刷新后重试");
        }
        Blog blog = new Blog();
        blog.setSort(1);
        blog.setId(id);
        blogService.updateById(blog);

        attributes.addAttribute("msg", "置顶成功");
        return "redirect:/admin/blog/1/10";
    }

    @GetMapping("/addTop")
    public String addTop(Model model) {
        // 分类信息
        List<BlogCategory> categoryList = blogCategoryService.list(null);
        model.addAttribute("categoryList", categoryList);
        return "admin/blog/write";
    }

    @PostMapping("/writeTop")
    public synchronized String write(QuestionWriteForm questionWriteForm) {
        // 构建问题对象
        Blog blog = new Blog();

        blog.setBid(AhdxUtils.getUuid());
        blog.setTitle(questionWriteForm.getTitle());
        blog.setContent(questionWriteForm.getContent());
        blog.setSort(1);
        blog.setViews(0);

        blog.setAuthorId(questionWriteForm.getAuthorId());
        blog.setAuthorName(questionWriteForm.getAuthorName());
        blog.setAuthorAvatar(questionWriteForm.getAuthorAvatar());

        BlogCategory category = blogCategoryService.getById(questionWriteForm.getCategoryId());
        blog.setCategoryId(questionWriteForm.getCategoryId());
        blog.setCategoryName(category.getCategory());
        blog.setGmtCreate(AhdxUtils.getTime());
        blog.setGmtUpdate(AhdxUtils.getTime());
        // 存储对象
        blogService.save(blog);

        // 重定向到列表页面
        return "redirect:/admin/blog/1/10";
    }

    @GetMapping("/removeTop/{id}")
    public String removeTop(@PathVariable int id, RedirectAttributes attributes) {
        if (id == 0) {
            attributes.addAttribute("err", "刷新后重试");
        }
        Blog blog = new Blog();
        blog.setSort(0);
        blog.setId(id);
        blogService.updateById(blog);

        attributes.addAttribute("msg", "取消置顶成功");
        return "redirect:/admin/blog/1/10";
    }
}
