package cn.edu.xmu.testserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author Ming Qiu
 * @date Created in 2020/12/13 19:32
 **/
@Service
public class TestService {
    @Autowired
    LiteSSHCaller ssh;

    private  static  final Logger logger = LoggerFactory.getLogger(TestService.class);

    public String newtask(String groupName, String manageGate, String mallGate){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.systemDefault());
        String dir = LocalDateTime.now().format(format);
        logger.debug("newtask: groupName = "+groupName+",dir = "+ dir +", manageGate="+manageGate+", mallGate="+mallGate);
//        String [] cmd = {"sh","/home/mybaby/privilege/public-test/runtest.sh",groupName, dir, manageGate, mallGate};
//        try {
//            Process proc = Runtime.getRuntime().exec(cmd);
//        }catch (IOException e){
//            logger.error("newtask: msg = "+e.getMessage());
//        }
        ssh.runNew(groupName, dir, manageGate, mallGate);
        return groupName+"/"+dir;
    }
}
