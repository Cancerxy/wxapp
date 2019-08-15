package com.zczp.cmsController;

import com.github.pagehelper.PageHelper;
import com.zczp.service_cancer.Impl.TbCompanyServiceImpl;
import com.zczp.service_yycoder.PosterService;
import com.zczp.service_yycoder.impl.FileServiceImpl;
import com.zczp.util.AjaxResult;
import com.zczp.vo_cancer.CompanyVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "公司管理模块")
@RequestMapping("/api/company")
public class CompanyController {
    @Value("${baseUploadUrl}")
    private String url;
    @Value("${qiniu.path}")
    private String Path;
    @Autowired
    private TbCompanyServiceImpl tbCompanyService;
    @Autowired
    private AjaxResult ajaxResult;
    @Autowired
    private FileServiceImpl fileService;
    @Autowired
    private PosterService posterService;

    @ApiOperation("展示所以公司")
    @GetMapping("/showCompany")
    public AjaxResult showComopany(
            @RequestParam @ApiParam("页数")int pageNum){
        PageHelper.startPage(pageNum,10);
        List<CompanyVo> companyVos=tbCompanyService.selectAll();
        if (!companyVos.isEmpty()){
            return ajaxResult.ok(companyVos);
        }
        return ajaxResult.error("操作失败");
    }

    @ApiOperation("搜索公司")
    @GetMapping("/search")
    public AjaxResult searchCompany(
            @RequestParam @ApiParam("页数")int pageNum,
            @RequestParam @ApiParam("公司名称")String companyName){
        PageHelper.startPage(pageNum,10);
        List<CompanyVo> companyVos=tbCompanyService.selectByName(companyName);
        if (!companyVos.isEmpty()){
            return ajaxResult.ok(companyVos);
        }
        return ajaxResult.ok("没有此公司");
    }

    @ApiOperation("新增公司")
    @PostMapping("/addCompany")
    public AjaxResult addCompany(
            @RequestParam(value = "file") @ApiParam("上传图片") MultipartFile upfile,
            @RequestParam @ApiParam("公司名称") String companyName) throws IOException {
        File file = new File(url + upfile.getOriginalFilename());
        Map<String,Object> map = new HashMap<>();
        //将MulitpartFile文件转化为file文件格式
        upfile.transferTo(file);
        CompanyVo companyVo=new CompanyVo();
        companyVo.setCompanyLogo(Path + "/" + fileService.uploadFile(file).get("imgName"));
        companyVo.setCompanyName(companyName);
        Integer result=tbCompanyService.addCompany(companyVo);
        System.out.println(result);
        if (result>0){
            map.put("公司logoUrl", companyVo.getCompanyLogo());
            map.put("公司名称",companyVo.getCompanyName());
            map.put("state","SUCESS,公司新增成功");

            return new AjaxResult().ok(map);
        }
        return new AjaxResult().error("新增失败");
    }

    @ApiOperation("编辑公司")
    @PutMapping("/updateCompany")
    public AjaxResult updateCompany(
            @RequestParam @ApiParam("公司Id")int companyId,
            @RequestParam @ApiParam("公司名称")String companyName,
            @RequestParam(value = "file") @ApiParam("上传图片") MultipartFile upfile) throws IOException {
        Map<String,Object> map = new HashMap<>();
        File file = new File(url + upfile.getOriginalFilename());
        //将MulitpartFile文件转化为file文件格式
        upfile.transferTo(file);
        CompanyVo companyVo=new CompanyVo();
        companyVo.setCompanyLogo(Path + "/" + fileService.uploadFile(file).get("imgName"));
        companyVo.setCompanyName(companyName);
        companyVo.setCompanyId(companyId);
        Integer result=tbCompanyService.updateCompany(companyVo);
        if (result!=0){
            map.put("公司logoUrl", companyVo.getCompanyLogo());
            map.put("公司名称",companyVo.getCompanyName());
            map.put("state","SUCESS,公司编辑成功");
            return new AjaxResult().ok(map);
        }
        return new AjaxResult().error("编辑失败");
    }
}
