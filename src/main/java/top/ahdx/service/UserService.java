package top.ahdx.service;

import top.ahdx.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import top.ahdx.pojo.UserInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 遇见狂神说
 * @since 2020-06-28
 */
public interface UserService extends IService<User> {

    void updateAvatar(String uid, String url);


    List<UserInfo> findUserInfoByRolesIsNotAdmin();

    Integer deleteUser(String uid);
}
