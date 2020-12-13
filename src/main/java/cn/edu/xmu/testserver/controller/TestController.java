package cn.edu.xmu.testserver.controller;

import cn.edu.xmu.testserver.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ming Qiu
 * @date Created in 2020/12/13 19:31
 **/
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/ajax/newtask")
    public void newtask(@Validated @RequestBody TaskVo taskVo) throws Throwable {
        testService.newtask(taskVo.getGroupname(), taskVo.getAdminGateway(), taskVo.getMallGateway());
    }
}
