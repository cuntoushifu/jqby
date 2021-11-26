package top.ahdx.mapper;

import top.ahdx.pojo.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 遇见狂神说
 * @since 2020-06-29
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    List<UserInfo> findUserInfoByRolesIsNotAdmin();
}
