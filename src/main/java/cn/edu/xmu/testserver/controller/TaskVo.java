package cn.edu.xmu.testserver.controller;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Ming Qiu
 * @date Created in 2020/12/13 19:40
 **/
@Data
public class TaskVo {

    @NotBlank
    private String groupname;
    @NotBlank
    private String adminGateway;
    @NotBlank
    private String mallGateway;
}
