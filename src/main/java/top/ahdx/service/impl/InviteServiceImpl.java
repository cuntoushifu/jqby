package top.ahdx.service.impl;


import top.ahdx.pojo.Invite;
import top.ahdx.mapper.InviteMapper;
import top.ahdx.service.InviteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.ahdx.utils.InviteCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 遇见狂神说
 * @since 2020-06-28
 */
@Service
public class InviteServiceImpl extends ServiceImpl<InviteMapper, Invite> implements InviteService {

    @Autowired
    private InviteCodeUtil inviteCodeUtil;

}
