package top.ahdx.utils;

import java.io.Serializable;

/**
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2021/1/19
 */
public class Res implements Serializable {

    private Boolean flag;
    private String message;
    private  Object data;

    public Res() {
    }

    public Res(Boolean flag, String message, Object data) {
        this.flag = flag;
        this.message = message;
        this.data = data;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
