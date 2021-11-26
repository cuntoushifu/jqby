package top.ahdx.service;

import top.ahdx.pojo.Friend;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 杨烨
 * @since 2021-01-18
 */
@Transactional
public interface FriendService extends IService<Friend> {

}
