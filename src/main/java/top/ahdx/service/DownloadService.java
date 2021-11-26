package top.ahdx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ahdx.pojo.Download;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 遇见狂神说
 * @since 2020-07-08
 */
@Transactional
public interface DownloadService extends IService<Download> {

}
