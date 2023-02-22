package com.common.utill.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

public class SftpUtil {

    private static Logger logger = LoggerFactory.getLogger(SftpUtil.class);

    /**
     *
     * @param filePath 文件全路径
     * @param ftpPath 上传到目的端目录
     * @param username
     * @param password
     * @param host
     * @param port
     */
    public static void uploadFile(String filePath, String ftpPath, String username, String password, String host, Integer port) {
        FileInputStream input = null;
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            //获取session  账号-ip-端口
            com.jcraft.jsch.Session sshSession = jsch.getSession(username, host, port);
            //添加密码
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            //严格主机密钥检查
            sshConfig.put("StrictHostKeyChecking", "no");
//            sshConfig.put("kex", "diffie-hellman-group1-sha1,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha1,diffie-hellman-group-exchange-sha256");
            sshSession.setConfig(sshConfig);
            //开启session连接
            sshSession.connect();
            //获取sftp通道
            sftp = (ChannelSftp) sshSession.openChannel("sftp");
            //开启
            sftp.connect();
        /*    //文件乱码处理
            Class<ChannelSftp> c = ChannelSftp.class;
            Field f = c.getDeclaredField("server_version");
            f.setAccessible(true);
            f.set(sftp, 2);
            sftp.setFilenameEncoding("GBK");*/
            //判断目录是否存在
            try {
                Vector ls = sftp.ls(ftpPath); //ls()得到指定目录下的文件列表
             /*   if (ls == null) {   //ls不会为null，哪怕它是一个空目录
                    sftp.mkdir(ftpPath);
                }*/
            } catch (SftpException e) {
                sftp.mkdir(ftpPath);
            }
            sftp.cd(ftpPath);
            String filename = filePath.substring(filePath.lastIndexOf(File.separator) + 1); //附件名字
            //filename = new String(filename.getBytes("GBK"), StandardCharsets.ISO_8859_1);
            input = new FileInputStream(new File(filePath));
            sftp.put(input, filename);
            //设定777权限，转为8进制放入chmod中
            //sftp.chmod(Integer.parseInt("777", 8), ftpPath + filename);
            input.close();
            sftp.disconnect();
            sshSession.disconnect();
            logger.info("================SFTP上传成功！==================");
        } catch (Exception e) {
            logger.error("================SFTP上传失败！==================");
            e.printStackTrace();
        }
    }

    /**
     * @param directory    SFTP服务器的文件路径
     * @param downloadFile SFTP服务器上的文件名
     * @param saveFile     保存到本地路径
     * @param username
     * @param password
     * @param host
     * @param port
     */
    public static void downloadFile(String directory, String downloadFile, String saveFile, String username, String password, String host, Integer port) {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            //获取session  账号-ip-端口
            com.jcraft.jsch.Session sshSession = jsch.getSession(username, host, port);
            //添加密码
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            //严格主机密钥检查
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            //开启session连接
            sshSession.connect();
            //获取sftp通道
            sftp = (ChannelSftp) sshSession.openChannel("sftp");
            //开启
            sftp.connect();
            if (directory != null && !"".equals(directory)) {
                sftp.cd(directory);
            }
            FileOutputStream output = new FileOutputStream(new File(saveFile));
            sftp.get(downloadFile, output);
            output.close();
            sftp.disconnect();
            sshSession.disconnect();
            System.out.println("================下载成功！==================");
        } catch (SftpException | FileNotFoundException | JSchException e) {
            logger.error("SFTP文件下载异常！", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
     /* String source = "D:\\test.png";
        String dest = "/home/12121";*/
     //   uploadFile(source ,dest,"root" , "200410Zwx@","101.34.17.206" ,22 );
        String directory = "/home/12121";
        String downloadFile = "test.png";
        String saveFile = "D:\\test\\test.png";
        downloadFile(directory,downloadFile,saveFile,"root" , "200410Zwx@","101.34.17.206" ,22);

    }



}