package com.ksy.restaptemplate.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ParamsUser {
    @NotEmpty
    @ApiModelProperty(value = "이메일", required = true)
    private String email;
    @NotEmpty
    @ApiModelProperty(value = "비밀번호", required = true)
    private String password;
    @ApiModelProperty(value = "이름", required = true)
    private String name;


}
