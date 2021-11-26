package top.ahdx.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QuestionWriteForm {

    @ApiModelProperty(value = "问题标题")
    private String title;
    @ApiModelProperty(value = "问题内容")
    private String content;

    @ApiModelProperty(value = "问题分类id")
    private Integer categoryId;

    @ApiModelProperty(value = "作者id")
    private String authorId;
    @ApiModelProperty(value = "作者名称")
    private String authorName;
    @ApiModelProperty(value = "作者头像")
    private String authorAvatar;

}
