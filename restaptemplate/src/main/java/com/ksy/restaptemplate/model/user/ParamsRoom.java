package com.ksy.restaptemplate.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ParamsRoom {
  
    @NotEmpty
    @ApiModelProperty(value = "방 이름", required = true)
    private String name;


}
