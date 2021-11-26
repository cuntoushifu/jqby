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
import top.ahdx.pojo.Question;
import top.ahdx.pojo.QuestionCategory;
import top.ahdx.service.QuestionCategoryService;
import top.ahdx.service.QuestionService;
import top.ahdx.utils.AhdxUtils;
import top.ahdx.vo.QuestionWriteForm;

import java.util.List;

/**
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2021/1/22
 */
@Controller
@RequestMapping("/admin/question")
public class AdminQuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionCategoryService questionCategoryService;

    @GetMapping("/{page}/{limit}")
    public String questionListPage(
            @PathVariable int page,
            @PathVariable int limit,
            Model model) {

        if (page < 1) {
            page = 1;
        }
        Page<Question> pageParam = new Page<>(page, limit);
        questionService.page(pageParam, new QueryWrapper<Question>().orderByDesc("sort").orderByDesc("gmt_create"));

        // 结果
        List<Question> questionList = pageParam.getRecords();
        model.addAttribute("questionList", questionList);
        model.addAttribute("pageParam", pageParam);

        // 分类信息
        return "admin/question/list";
    }


    @GetMapping("/top/{id}")
    public String top(@PathVariable int id, RedirectAttributes attributes) {
        if (id == 0) {
            attributes.addAttribute("err", "刷新后重试");
        }
        Question question = new Question();
        question.setSort(1);
        question.setId(id);
        questionService.updateById(question);

        attributes.addAttribute("msg", "置顶成功");
        return "redirect:/admin/question/1/10";
    }

    @GetMapping("/addTop")
    public String addTop(Model model) {
        // 分类信息
        List<QuestionCategory> categoryList = questionCategoryService.list(null);
        model.addAttribute("categoryList", categoryList);
        return "admin/question/write";
    }

    @PostMapping("/writeTop")
    public synchronized String write(QuestionWriteForm questionWriteForm) {
        // 构建问题对象
        Question question = new Question();

        question.setQid(AhdxUtils.getUuid());
        question.setTitle(questionWriteForm.getTitle());
        question.setContent(questionWriteForm.getContent());
        question.setSort(1);
        question.setViews(0);

        question.setAuthorId(questionWriteForm.getAuthorId());
        question.setAuthorName(questionWriteForm.getAuthorName());
        question.setAuthorAvatar(questionWriteForm.getAuthorAvatar());

        QuestionCategory category = questionCategoryService.getById(questionWriteForm.getCategoryId());
        question.setCategoryId(questionWriteForm.getCategoryId());
        question.setCategoryName(category.getCategory());
        question.setGmtCreate(AhdxUtils.getTime());
        question.setGmtUpdate(AhdxUtils.getTime());
        // 存储对象
        questionService.save(question);

        // 重定向到列表页面
        return "redirect:/admin/question/1/10";
    }

    @GetMapping("/removeTop/{id}")
    public String removeTop(@PathVariable int id, RedirectAttributes attributes) {
        if (id == 0) {
            attributes.addAttribute("err", "刷新后重试");
        }
        Question question = new Question();
        question.setSort(0);
        question.setId(id);
        questionService.updateById(question);

        attributes.addAttribute("msg", "取消置顶成功");
        return "redirect:/admin/question/1/10";
    }
}
