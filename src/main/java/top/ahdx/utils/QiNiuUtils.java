package top.ahdx.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2021/1/19
 */
@Controller
public class QiNiuUtils {
    //设置好账号的ACCESS_KEY和SECRET_KEY
    @Value("${qiniu.access}")
    private String access;
    @Value("${qiniu.secret}")
    String secret;
    //要上传的空间
    @Value("${qiniu.bucketname}")
    String bucketname;

    @Value("${qiniu.baseUrl}")
    String baseUrl;
    @Value("${qiniu.path}")
    String basePath;


//    public static void main(String args[]) throws IOException {
//        new QiNiuUtils().upload();
//    }

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
//        System.out.println("xxxxxxacces     " + access);
//        System.out.println("wwwwwwwwwwww     " + secret);
        //密钥配置
        Auth auth = Auth.create(access, secret);

        return auth.uploadToken(bucketname);
    }

    public String upload(InputStream fileInputStream, String key) throws IOException {

        ///////////////////////指定上传的Zone的信息//////////////////
        //第一种方式: 指定具体的要上传的zone
        //注：该具体指定的方式和以下自动识别的方式选择其一即可
        //要上传的空间(bucket)的存储区域为华东时
        // Zone z = Zone.zone0();
        //要上传的空间(bucket)的存储区域为华北时
        // Zone z = Zone.zone1();
        //要上传的空间(bucket)的存储区域为华南时
        //Zone z = Zone.zone2();

        //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);

        //创建上传对象
        UploadManager uploadManager = new UploadManager(c);

        key = UUID.randomUUID() + "." + key.split("\\.")[1];
        try {

            //调用put方法上传
            Response res = uploadManager.put(fileInputStream, basePath + "/" + key, getUpToken(), null, null);
            //打印返回的信息
            System.out.println(res.bodyString());
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());

            try {
                //响应的文本信息
                System.out.println(r.bodyString());
                return null;
            } catch (QiniuException e1) {
                return null;
                //ignore
            }
        }

        return baseUrl + basePath + "/" + key;
    }

}
