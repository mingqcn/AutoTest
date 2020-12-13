package cn.edu.xmu.testserver.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * @author Ming Qiu
 * @date Created in 2020/12/13 19:32
 **/
@Service
public class TestService {

    public void newtask(String groupName, String manageGate, String mallGate){
        String [] cmd = {"sh","runtest.sh",groupName, manageGate, mallGate};
        try {
            Process proc = Runtime.getRuntime().exec(cmd, null , new File("/home/mybaby/public-test"));
        }catch (IOException e){

        }
    }
}
