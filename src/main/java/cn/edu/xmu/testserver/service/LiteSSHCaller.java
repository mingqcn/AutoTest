package cn.edu.xmu.testserver.service;

import cn.edu.xmu.testserver.service.models.UserInfoClass;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

@Repository
public class LiteSSHCaller {
    Logger logger = LoggerFactory.getLogger(LiteSSHCaller.class);

    @Value("${adminhost.username}")
    String username;

    @Value("${adminhost.password}")
    String password;

    @Value("${adminhost.ip}")
    String ip;

    String bashname = "/home/mybaby/privilege/public-test/runtest.sh";

    @PostConstruct
    public void postConstruct() {
        getSession();
    }

    public void runNew(String... args) {
        var session = getSession();
        if (session != null) {

            /* * WARNING: setsid xx & 命令如果在后台有输出，会导致进程卡死(bug of JSch) * */
            /* * WARNING: 所以需要将输出重定向到xxx.log中 * */
            String cmdTemplate = "%bashname %arg0 %arg1 %arg2 %arg3 2>&1";
            execGetResult(session,
                    cmdTemplate
                            .replace("%bashname", bashname)
                            .replace("%arg0", args[0])
                            .replace("%arg1", args[1])
                            .replace("%arg2", args[2])
                            .replace("%arg3", args[3])
            );
        }
    }

    // session池，提高响应速度
    Session private_get_session;

    Session getSession() {
        if (private_get_session == null
            || !private_get_session.isConnected()) {
            private_get_session = null;

            int port = 22;

            try {
                JSch jsch = new JSch();
                private_get_session = jsch.getSession(username, ip, port);
                private_get_session.setPassword(password);
                private_get_session.setUserInfo(new UserInfoClass());
                var config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                private_get_session.setConfig(config);
                private_get_session.setTimeout(2000);
                private_get_session.connect();
            } catch (JSchException e) {
                return null;
            }
        }
        return private_get_session;
    }

    void execGetResult(Session session, String cmd) {
        ChannelExec channelExec = null;
        try {
            channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(cmd);
            channelExec.setInputStream(null);
            channelExec.setErrStream(null);
            channelExec.connect();

            //取得命令结果的输出流
            var stream = channelExec.getInputStream();

            //用一个读输出流类去读
            InputStreamReader isr=new InputStreamReader(stream);
            //用缓冲器读行
            BufferedReader br=new BufferedReader(isr);
            String line=null;
            //直到读完为止
            while((line=br.readLine())!=null)
            {
                logger.debug(line);
            }
            stream.close();
        } catch (IOException | JSchException e) {
        } finally {
            try {
                channelExec.disconnect();
            } catch (Exception e) {}
        }
    }
}
