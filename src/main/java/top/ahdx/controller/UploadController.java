package top.ahdx.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.ahdx.service.UserService;
import top.ahdx.utils.QiNiuUtils;
import top.ahdx.utils.Res;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private UserService userService;

    @Autowired
    private QiNiuUtils qiNiuUtils;
    @Autowired
    private HttpServletRequest request;

    @PostMapping("/native")
    public String nativeUpload(@RequestParam("file") MultipartFile file) {
        System.out.println("native......");
        String path = request.getSession().getServletContext().getRealPath("//static//img");
        String filePath = path + "/" + file.getOriginalFilename();
        File desFile = new File(filePath);
        if (!desFile.getParentFile().exists()) {
            desFile.mkdirs();
        }
        try {
            file.transferTo(desFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/img/" + file.getOriginalFilename();
    }
    @PostMapping("/native/{uid}")
    public Res nativeUpload(@RequestParam("file") MultipartFile file, @PathVariable String uid) {
        if (file == null || file.getSize() <= 0) {
            return new Res(false, "上传失败", null);
        }
        String filename = file.getOriginalFilename();

        if (!filename.endsWith(".png") && !filename.endsWith(".jpg")) {

            return new Res(false, "只能上传jpg或者png格式的图片", null);
        }
        if (file.getSize() > 1024 * 2 * 1024) {
            return new Res(false, "图片大小不能大于2M", null);
        }

        System.out.println("native......uid........");
        String path = request.getSession().getServletContext().getRealPath("/img/");

        String filePath = path   + file.getOriginalFilename();
        File desFile = new File(filePath);
        if (!desFile.getParentFile().exists()) {
            desFile.mkdirs();
        }
        try {
            file.transferTo(desFile);
        } catch (IOException e) {
            return new Res(false, "上传失败", null);
        }

        return new Res(true, "上传失败", "/img/" + file.getOriginalFilename());
    }

    @PostMapping("/qiniu")
    @ResponseBody
    public Res qiniuUpload(@RequestParam("file") MultipartFile file) {
        System.out.println("size" + file.getSize() / 1024);
        System.out.println("name" + file.getOriginalFilename());
        System.out.println("isJPG" + file.getOriginalFilename().endsWith(".png"));
        if (file == null || file.getSize() <= 0) {
            return new Res(false, "上传失败", null);
        }
        String filename = file.getOriginalFilename();

        if (!filename.endsWith(".png") && !filename.endsWith(".jpg")) {
            System.out.println("ssssssssss");
            return new Res(false, "只能上传jpg或者png格式的图片", null);
        }
        if (file.getSize() > 1024 * 2 * 1024) {
            return new Res(false, "图片大小不能大于2M", null);
        }

        System.out.println("qiniu");

        try {
            String url = qiNiuUtils.upload(file.getInputStream(), file.getOriginalFilename());
            if (url == null) {
                return new Res(false, "上传失败", null);
            }
            System.out.println(url);
            return new Res(true, "上传成功", url);
        } catch (IOException e) {
            e.printStackTrace();
            return new Res(false, "上传失败", null);
        }


    }

    @PostMapping("/qiniu/{uid}")
    @ResponseBody
    public Res qiniuUpload(@RequestParam("file") MultipartFile file, @PathVariable String uid) {
//        System.out.println("size" + file.getSize() / 1024);
//        System.out.println("name" + file.getOriginalFilename());
//        System.out.println("isJPG" + file.getOriginalFilename().endsWith(".png"));
        if (file == null || file.getSize() <= 0) {
            return new Res(false, "上传失败", null);
        }
        String filename = file.getOriginalFilename();

        if (!filename.endsWith(".png") && !filename.endsWith(".jpg")) {

            return new Res(false, "只能上传jpg或者png格式的图片", null);
        }
        if (file.getSize() > 1024 * 2 * 1024) {
            return new Res(false, "图片大小不能大于2M", null);
        }



        try {
            String url = qiNiuUtils.upload(file.getInputStream(), file.getOriginalFilename());
            if (url == null) {
                return new Res(false, "上传失败", null);
            }
            System.out.println(url);

            userService.updateAvatar(uid, url);
            return new Res(true, "上传成功", url);
        } catch (IOException e) {
            e.printStackTrace();
            return new Res(false, "上传失败", null);
        }


    }


}
