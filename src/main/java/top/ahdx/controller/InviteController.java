package top.ahdx.controller;

import top.ahdx.pojo.Invite;
import top.ahdx.service.InviteService;
import top.ahdx.utils.AhdxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2021/1/20
 */
@Controller
@RequestMapping("/invite")
public class InviteController {

    @Autowired
    private InviteService inviteService;


    @PostMapping("/code")
    @ResponseBody
    public String getCode() {
        Invite invite = new Invite();
        invite.setGmtCreate(AhdxUtils.getTime());
        invite.setStatus(0);
        String code = String.valueOf(new Date().getTime());
        invite.setCode(code);
        inviteService.save(invite);
        return code;
    }
}
