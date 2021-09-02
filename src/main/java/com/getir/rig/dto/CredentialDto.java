package com.getir.rig.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@ApiModel(value = "Credential Api model documentation", description = "Model")
public class CredentialDto {

    @ApiModelProperty(value = "Username credential")
    String userName;
    @ApiModelProperty(value = "Password credential")
    @Size(max = 20,min=5,message = "Password can be between 5 to 20 characters long")
    String password;

}
