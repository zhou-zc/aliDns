package com.aliDns.task;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author zhou-zc
 * 定时备份git的目录文件
 */
@Component
public class GitBackupTask {
    @Value("${aliDns.backupPath}")
    private String backupPath;
    @Value("${aliDns.sourcePath}")
    private String sourcePath;
    private static final Logger log = LoggerFactory.getLogger(GitBackupTask.class);
    @Scheduled(cron = "0 30 0 1 * ?")
    public void scheduledTask() {
        String stopResult = execCommand(false);
        log.info("[Git服务]停止服务:{}",stopResult);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
        String dateStr = dateFormat.format(new Date());
        String backupPathTemp = backupPath+"\\"+dateStr;
        long startTime = System.currentTimeMillis();
        log.info("[Git服务]开始备份：{}",sourcePath);
        copyDir(sourcePath,backupPathTemp);
        zipFile(backupPathTemp);
        deleteFile(backupPathTemp);
        long endTime = System.currentTimeMillis();
        log.info("[Git服务]完成备份：{},耗时：{}s",sourcePath,(endTime-startTime)/1000);
        String startResult = execCommand(true);
        log.info("[Git服务]启动服务:{}",startResult);
    }

    /**
     * 文件夹拷贝
     * @param sourcePath
     * @param targetPath
     */
    public void copyDir(String sourcePath,String targetPath){
        File sourceFile = new File(sourcePath);
        File targetFile = new File(targetPath);
        if(!targetFile.exists()){
            targetFile.mkdir();
        }
        for (String file : sourceFile.list()) {
            File tempFile = new File(sourcePath + File.separator + file);
            if(tempFile.isDirectory()){
                copyDir(sourcePath + File.separator + file,targetPath + File.separator + file);
            }else{
                copyFile(sourcePath + File.separator + file,targetPath + File.separator + file);
            }
        }
    }


    /**
     * 文件的拷贝
     * @param sourcePath
     * @param targetPath
     */
    public void copyFile(String sourcePath, String targetPath) {
        File sourceFile = new File(sourcePath);
        File targetFile = new File(targetPath);
        try(BufferedInputStream bis=new BufferedInputStream(new FileInputStream(sourceFile));
            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(targetFile))) {
            int len = 0;
            byte[] flush = new byte[1024 * 2];
            while((len=bis.read(flush)) != -1) {
                bos.write(flush, 0, len);
            }
            bos.flush();
        }catch(FileNotFoundException e) {
            log.error("文件没有找到:{}",sourcePath);
        }catch(IOException e) {
            log.error("IO错误:{}",sourcePath);
        }
    }

    /**
     * 删除某个文件夹
     * @param path
     */
    public void deleteFile(String path) {
        File file = new File(path);
        if(file.exists()){
            if (!file.isFile() && file.list().length != 0) {
                for (String temp : file.list()) {
                    deleteFile(path + File.separator + temp);
                }
            }
            file.delete();
        }
    }


    /**
     * 压缩文件夹
     * @param zipPath
     */
    public void zipFile(String zipPath){
        try {
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setEncryptFiles(true);
            zipParameters.setEncryptionMethod(EncryptionMethod.AES);
            new ZipFile(zipPath+".zip","yingtian".toCharArray()).addFolder(new File(zipPath),zipParameters);
        } catch (ZipException e) {
            log.error("文件压缩错误:{}",e.getMessage());
        }
    }

    public String execCommand(boolean switchFlag){
        try {
            String cmd = switchFlag?"net start gitea":"net stop gitea";
            StringBuffer result = new StringBuffer();
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
            String line = null;
            while ((line=bufferedReader.readLine()) !=null){
                result.append(line).append("\n");
            }
            return result.toString();
        } catch (IOException e) {
            log.error("IO流错误:{}",e.getMessage());
            return "";
        }
    }
}
