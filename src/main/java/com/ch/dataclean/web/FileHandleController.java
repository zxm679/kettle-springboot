package com.ch.dataclean.web;

import com.ch.dataclean.common.util.CommonUtils;
import com.ch.dataclean.model.DataDeptModel;
import com.ch.dataclean.model.FileModel;
import com.ch.dataclean.service.DataDeptService;
import com.ch.dataclean.service.FileHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/file")
public class FileHandleController extends BaseController {
    @Autowired
    private FileHandleService fileHandleService;
    @Autowired
    private DataDeptService dataDeptService;

    /**
     * 文件上传页面
     */
    @RequestMapping(value = "/toUpload")
    public String toUpload(Model model){
        try {
            List<DataDeptModel> depts = dataDeptService.getDepts();
            List<FileModel> files = fileHandleService.getFiles(null,"0");
            model.addAttribute("files", files);
            model.addAttribute("depts", depts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "upload";
    }

    /**
     * 实现文件上传
     */
    @RequestMapping(value = "/fileUpload")
    @ResponseBody
    public Object fileUpload(@RequestParam(value = "file", required = false) MultipartFile file, String deptId, String desc){
        if(null != file && !file.isEmpty()){
            try {
                CommonUtils.formCheck(deptId,"请选择部门");
                fileHandleService.fileUpload(file, deptId, desc);
                return data(success(),"文件上传成功");
            } catch (Exception e) {
                e.printStackTrace();
                return data(error(),e.getMessage());
            }
        }else {
            return data(error(),"请不要上传空文件");
        }
    }

    /**
     * 根据pid查询文件目录
     * search可以是文件名、部门名或描述
     */
    @RequestMapping(value = "/getFiles")
    public Object getFiles(String search, @RequestParam(value = "pid",defaultValue = "0") String pid){
        try {
            List<FileModel> files = fileHandleService.getFiles(search, pid);
            Map<String, Object> map = new HashMap<>();
            map.put("files", files);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return error();
        }
    }

    /**
     * 模板下载
     * @param deptId
     * @return
     */
    @RequestMapping(value = "/download")
    public Object downloadTempFile(String deptId){
        try {
            return fileHandleService.downloadTemplateFile(deptId);
        } catch (Exception e) {
            e.printStackTrace();
            return data(error(),"没有该部门的模板文件");
        }
    }

    @RequestMapping("/test")
    @ResponseBody
    public Object test(){
        /*model.addAttribute("message", "The file is empty!");
        return "/uploadStatus";*/
        return data(success(),"这仅仅是一个测试");
    }

    @RequestMapping(value = "/testTrans")
    @ResponseBody
    public Object testTrans(){
        fileHandleService.testTrans();
        return "testTrans success";
    }

    @RequestMapping(value = "/testJob")
    @ResponseBody
    public Object testJob(){
        fileHandleService.testJob();
        return "testJob success";
    }

}
