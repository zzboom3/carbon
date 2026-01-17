package com.carbon.system.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "ChangeEmailParam", description = "更新邮箱参数")
public class ChangeEmailParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "账户ID")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;
}

