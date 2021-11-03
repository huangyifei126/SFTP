import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * @Author: hyf
 * @Create: 2021/11/3 22:17
 */
public class MySFTP {
    public static void main(String[] args) {
        MySFTP sf = new MySFTP();
        String host = "192.168.227.144";
        int port = 22;
        String username = "root";
        String password = "root";
        String directory = "/home/hadoop/tmp/";
        String uploadFile = "C:\\Users\\hyfei\\Desktop\\tmp\\logs\\app.2021-10-30.log";
        ChannelSftp sftp=sf.connect(host, port, username, password);
        sf.upload(directory, uploadFile, sftp);
        try{
            sftp.cd(directory);
            sftp.mkdir("ss");
            System.out.println("finished");
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public ChannelSftp connect(String host, int port, String username,
                               String password) {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            Session sshSession = jsch.getSession(username, host, port);
            System.out.println("Session created.");
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            System.out.println("Session connected.");
            System.out.println("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            System.out.println("Connected to " + host + ".");
        } catch (Exception e) {

        }
        return sftp;
    }

    /**
     * 上传文件
     *
     * @param directory  上传的目录
     * @param uploadFile 要上传的文件
     * @param sftp
     */
    public void upload(String directory, String uploadFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            sftp.put(new FileInputStream(file), file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
