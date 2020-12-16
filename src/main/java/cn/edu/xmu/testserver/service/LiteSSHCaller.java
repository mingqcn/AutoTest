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
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

@Repository
public class LiteSSHCaller {
    Logger logger = LoggerFactory.getLogger(LiteSSHCaller.class);

    @Value("${adminhostusername}")
    String username;

    @Value("${adminhostpassword}")
    String password;

    @Value("${adminhostip}")
    String ip;

    String bashname = "/home/mybaby/privilege/public-test/runtest.sh";

    @PostConstruct
    public void postConstruct() {
        getSession();
    }

    public boolean runNew(String... args) {
        var session = getSession();
        if (session == null) return false;

        /* * WARNING: setsid xx & 命令如果在后台有输出，会导致进程卡死(bug of JSch) * */
        /* * WARNING: 所以需要将输出重定向到xxx.log中 * */
        String cmdTemplate = "%bashname %arg0 %arg1 %arg2 %arg3 2>&1";
        String result = execGetResult(session,
                cmdTemplate
                        .replace("%bashname",bashname)
                        .replace("%arg0",   args[0])
                        .replace("%arg1",   args[1])
                        .replace("%arg2",   args[2])
                        .replace("%arg3",   args[3])
        );
        logger.debug(result);
        return result != null;
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

    String execGetResult(Session session, String cmd) {
        ChannelExec channelExec = null;
        try {
            channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(cmd);
            channelExec.setInputStream(null);
            channelExec.setErrStream(null);
            channelExec.connect();

            var stream = channelExec.getInputStream();
            var s = new String(stream.readAllBytes());
            stream.close();
            return s;
        } catch (IOException | JSchException e) {
            return "IOException | JSchException e";
        } finally {
            try {
                channelExec.disconnect();
            } catch (Exception e) {}
        }
    }
}
