package com.getir.rig.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@ApiModel(value = "Customer Api model documentation", description = "Model")
public class CustomerDto {

    @ApiModelProperty(value = "Unique id field of customer object")
    private String id;


    @ApiModelProperty(value = "User name of the customer")
    @Size(max = 20,message = "Username can be at most 20 characters long")
    private String userName;

    @ApiModelProperty(value = "Fristname of the customer")
    @Size(max = 20,message = "Name can be at most 20 characters long")
    private String firstName;

    @ApiModelProperty(value = "Lastname of the customer")
    @Size(max=30, message = "surname can be at most 30 characters long")
    private String lastname;

    @ApiModelProperty(value = "Email of the customer")
    @Size(max = 50,message = "E-mail can be at most 50 characters long")
    private String email;

    @ApiModelProperty(value = "Password of the customer")
    @Size(max = 20,min=5,message = "Password can be between 5 to 20 characters long")
    private String password;

}
