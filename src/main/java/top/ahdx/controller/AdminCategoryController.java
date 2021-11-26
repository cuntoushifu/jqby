package top.ahdx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.ahdx.pojo.Blog;
import top.ahdx.pojo.BlogCategory;
import top.ahdx.pojo.Question;
import top.ahdx.pojo.QuestionCategory;
import top.ahdx.service.BlogCategoryService;
import top.ahdx.service.BlogService;
import top.ahdx.service.QuestionCategoryService;
import top.ahdx.service.QuestionService;
import top.ahdx.utils.AhdxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2021/1/20
 */
@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    @Autowired
    private BlogCategoryService blogCategoryService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionCategoryService questionCategoryService;

    @PostMapping("/blog/add")
    public String addBlogCategory(BlogCategory blogCategory,RedirectAttributes redirectAttributes) {
        int count = blogCategoryService.count(new QueryWrapper<BlogCategory>().eq("category", blogCategory.getCategory()));
        if (count > 0) {
            redirectAttributes.addAttribute("err", "该分类已经存在，请勿重复添加");
            return "redirect:/admin/categories/blog/1/10";
        }
        blogCategory.setGmtCreate(AhdxUtils.getTime());
        blogCategoryService.save(blogCategory);
        return "redirect:/admin/categories/blog/1/10";
    }


    @GetMapping("/question/del/{id}")
    public String delQuestionCategory(@PathVariable int id, RedirectAttributes redirectAttributes) {
        int count = questionService.count(new QueryWrapper<Question>().eq("category_id", id));
        if (count > 0) {
            redirectAttributes.addAttribute("err", "该分类下有问答，不能删除");
        }
        if (count == 0) {
            questionCategoryService.removeById(id);
            redirectAttributes.addAttribute("msg", "删除成功");
        }
        return "redirect:/admin/categories/question/1/10";
    }
    @GetMapping("/blog/del/{id}")
    public String delBlogCategory(@PathVariable int id, RedirectAttributes redirectAttributes) {
        int count = blogService.count(new QueryWrapper<Blog>().eq("category_id", id));
        if (count > 0) {
            redirectAttributes.addAttribute("err", "该分类下有博客，不能删除");
        }
        if (count == 0) {
            blogCategoryService.removeById(id);
            redirectAttributes.addAttribute("msg", "删除成功");
        }
        return "redirect:/admin/categories/blog/1/10";
    }

    @GetMapping("/blog/{page}/{limit}")
    public String blogs(
            @PathVariable int page,
            @PathVariable int limit,
            Model model) {

        if (page < 1) {
            page = 1;
        }
        Page<BlogCategory> pageParam = new Page<>(page, limit);
        blogCategoryService.page(pageParam, new QueryWrapper<BlogCategory>().orderByDesc("gmt_create"));

        // 结果
        List<BlogCategory> blogCategories = pageParam.getRecords();
        model.addAttribute("blogCategories", blogCategories);
        model.addAttribute("pageParam", pageParam);
        return "admin/categories/blog";
    }

    @GetMapping("/question/{page}/{limit}")
    public String questions(
            @PathVariable int page,
            @PathVariable int limit,
            Model model) {

        if (page < 1) {
            page = 1;
        }
        Page<QuestionCategory> pageParam = new Page<>(page, limit);
        questionCategoryService.page(pageParam, new QueryWrapper<QuestionCategory>().orderByDesc("gmt_create"));

        // 结果
        List<QuestionCategory> questionCategory = pageParam.getRecords();
        model.addAttribute("questionCategory", questionCategory);
        model.addAttribute("pageParam", pageParam);
        return "admin/categories/question";
    }
    @PostMapping("/question/add")
    public String addQuestionCategory(QuestionCategory questionCategory,RedirectAttributes redirectAttributes) {
        int count = questionCategoryService.count(new QueryWrapper<QuestionCategory>().eq("category",questionCategory.getCategory()));
        if (count > 0) {
            redirectAttributes.addAttribute("err", "该分类已经存在，请勿重复添加");
            return "redirect:/admin/categories/question/1/10";
        }
        questionCategory.setGmtCreate(AhdxUtils.getTime());
        questionCategoryService.save(questionCategory);
        return "redirect:/admin/categories/question/1/10";
    }
}
