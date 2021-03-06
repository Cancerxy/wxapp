package com.zczp.cmsController_yycoder;

import com.zczp.entity.TbAdmin;
import com.zczp.service_yycoder.AdminService;
import com.zczp.util.AjaxResult;
import com.zczp.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 管理员 进行 对用户信息的管理
 */
@RestController
@RequestMapping("admin")
@Api(tags = "登录界面模块")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    @ApiOperation("管理员登录")
    public AjaxResult adminLogin(
            @RequestParam @ApiParam("Admin名字") String adminName,
            @RequestParam @ApiParam("Admin密码") String adminPwd) {
        boolean result = adminService.checkAdmin(adminName, adminPwd);
        if (result) {
//            //把token返回给客户端-->客户端保存至cookie-->客户端每次请求附带cookie参数
            TbAdmin tbAdmin = new TbAdmin(adminName, adminPwd);
            String token = jwtUtil.createTokenByTbAdmin(tbAdmin);
            return new AjaxResult().ok(token);
        }
        return new AjaxResult().error("用户名或密码错误，请重新输入");

    }

}
