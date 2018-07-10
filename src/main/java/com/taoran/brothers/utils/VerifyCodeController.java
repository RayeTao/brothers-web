package com.taoran.brothers.utils;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.taoran.brothers.common.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@RestController
public class VerifyCodeController {

@Autowired
    private DefaultKaptcha defaultKaptcha;

@RequestMapping(value = "/getCode",method = RequestMethod.GET)
    public void  getCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
    byte[] captchaChallengeAsJpeg = null;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
        //生成验证码，并将验证码放在session中
        String code = defaultKaptcha.createText();
        httpServletRequest.getSession().setAttribute("verifyCode",code);
        //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
        BufferedImage image = defaultKaptcha.createImage(code);
        ImageIO.write(image,"jpg",outputStream);
    }catch (IllegalArgumentException e){
       httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
       return;
    }
    //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
    captchaChallengeAsJpeg = outputStream.toByteArray();
    httpServletResponse.setHeader("Cache-Control", "no-store");
    httpServletResponse.setHeader("Pragma", "no-cache");
    httpServletResponse.setDateHeader("Expires", 0);
    httpServletResponse.setContentType("image/jpeg");
    ServletOutputStream responseOutputStream =
            httpServletResponse.getOutputStream();
    responseOutputStream.write(captchaChallengeAsJpeg);
    responseOutputStream.flush();
    responseOutputStream.close();
}

    @RequestMapping(value = "/checkCode",method = RequestMethod.GET)
    public ResultInfo checkCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
       // ModelAndView andView = new ModelAndView();
        ResultInfo result = new ResultInfo();
        String captchaId = (String) httpServletRequest.getSession().getAttribute("verifyCode");
        String parameter = httpServletRequest.getParameter("code");
        System.out.println("Session  vrifyCode "+captchaId+" form verifyCode "+parameter);

        if (!captchaId.equals(parameter)) {
            result.setCode(10);
            result.setMessage("验证码错误");
        } else {
            result.setCode(11);
            result.setMessage("验证成功");

        }
        return result;
    }
}
