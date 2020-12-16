package cn.edu.xmu.testserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private  static  final Logger logger = LoggerFactory.getLogger(TestService.class);

    public String newtask(String groupName, String manageGate, String mallGate){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.systemDefault());
        String dir = LocalDateTime.now().format(format);
        String [] cmd = {"sh","/home/mybaby/privilege/public-test/runtest.sh",groupName, dir, manageGate, mallGate};
        logger.debug("newtask: cmd = "+cmd.toString());
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            //取得命令结果的输出流
            InputStream fis=proc.getInputStream();
            //用一个读输出流类去读
            InputStreamReader isr=new InputStreamReader(fis);
            //用缓冲器读行
            BufferedReader br=new BufferedReader(isr);
            String line=null;
            //直到读完为止
            while((line=br.readLine())!=null)
            {
              logger.debug(line);
            }
        }catch (IOException e){
            logger.error("newtask: msg = "+e.getMessage());
        }
        return "http://172.16.1.66/test/"+groupName+"/"+dir;
    }
}
