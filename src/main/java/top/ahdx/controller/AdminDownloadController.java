package top.ahdx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.ahdx.pojo.Download;
import top.ahdx.service.DownloadService;
import top.ahdx.utils.AhdxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2021/1/21
 */
@Controller
@RequestMapping("/admin/download")
public class AdminDownloadController {

    @Autowired
    private DownloadService downloadService;

    @GetMapping("/{page}/{limit}")
    public String blogs(
            @PathVariable int page,
            @PathVariable int limit,
            Model model) {

        if (page < 1) {
            page = 1;
        }
        Page<Download> pageParam = new Page<>(page, limit);
        downloadService.page(pageParam, new QueryWrapper<Download>().orderByDesc("gmt_create"));

        // 结果
        List<Download> downloads = pageParam.getRecords();
        model.addAttribute("downloads", downloads);
        model.addAttribute("pageParam", pageParam);
        return "admin/download/downloads";
    }

    @GetMapping("/del/{id}")
    public String delDownload(@PathVariable int id) {
        downloadService.removeById(id);
        return "redirect:/admin/download/1/10";
    }

    @PostMapping("/add")
    public String add(Download download) {
        download.setGmtCreate(AhdxUtils.getTime());
        downloadService.save(download);
        return "redirect:/admin/download/1/10";
    }
}
