package cn.edu.xmu.testserver.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author Ming Qiu
 * @date Created in 2020/12/13 19:32
 **/
@Service
public class TestService {

    public void newtask(String groupName, String manageGate, String mallGate){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").withZone(ZoneId.systemDefault());
        String dir = LocalDateTime.now().format(format);
        String [] cmd = {"sh","/home/mybaby/privilege/public-test/runtest.sh",groupName, dir, manageGate, mallGate};
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
        }catch (IOException e){

        }
    }
}
