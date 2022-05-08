package com.itheima.reggie.controller;

import com.itheima.reggie.common.CommonResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.img-path}")
    private String imgBasePath;

    /**
     * 文件上传
     * @param file 文件
     * @return 返回文件名称
     */
    @PostMapping("/upload")
    public CommonResult<String> upload(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        // 获取文件名后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 使用UUID生成随机名称和文件名后缀组成文件名
        String fileName = UUID.randomUUID().toString() + suffix;
        File baseFilePath = new File(imgBasePath);
        // 如果文件夹不存在先创建
        if (!baseFilePath.exists()){
            baseFilePath.mkdirs();
        }
        try {
            file.transferTo(new File(imgBasePath + fileName));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.success(fileName);
    }

    /**
     * 文件下载
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            // 通过文件输入流读取文件
            FileInputStream fileInputStream = new FileInputStream(new File(imgBasePath + name));
            // 通过输出流写给浏览器 在浏览器展示
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            fileInputStream.close();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
