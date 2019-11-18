package com.liuyanzhao.ssm.blog.controller.home;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.weixin4j.Weixin;
import org.weixin4j.WeixinException;
import org.weixin4j.component.SnsComponent;
import org.weixin4j.model.sns.SnsUser;

@Controller
public class WeixinUserInfoController {
	public boolean getSnsUserInfoByCode_1(HttpServletRequest request, HttpServletResponse response,String returnUrl) throws Exception {
		Weixin weixin =  new Weixin();
		 //从session中获取openid
        Object oauth_openid = request.getSession().getAttribute("openid");
        //第一次访问,判断是否存在openid，不存在则说明没有进行授权访问，进行授权访问
        if (oauth_openid == null) {
            //获取Sns组件
            SnsComponent snsComponent = weixin.sns();
            //获取code，换取openid
            String code = request.getParameter("code");
            //如果没有获取到，则说明是直接访问页面链接，进行匿名获取
            if (code == null || code.equals("")) {
                //生成静默授权获取openid跳转链接
                String url = snsComponent.getOAuth2CodeUserInfoUrl(returnUrl);
                //跳转到微信授权页面
                response.sendRedirect(url);
                return false;
            } else {
                //获取授权得到微信用户信息
                SnsUser snsUser = snsComponent.getSnsUserByCode(code);
                System.out.println(snsUser.getNickname());
                System.out.println(snsUser.getHeadimgurl());
                //设置当前用户
                request.getSession().setAttribute("openid", snsUser.getOpenid());
                //重定向到URL
                response.sendRedirect(returnUrl);
                return false;
            }
        }
        return true;
       
    }
	@RequestMapping("/user/getSnsUserInfoByCode")
	public String getSnsUserInfoByCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String returnUrl;
		//returnUrl = request.getScheme() +"://" + request.getServerName()  + ":" +request.getServerPort() +request.getContextPath();
		returnUrl = "http://698df2a7.ngrok.io/user/getSnsUserInfoByCode";
		System.out.print(returnUrl);
		getSnsUserInfoByCode_1(request, response,returnUrl);
		
        return "Home/Page/getSnsPage";
       
    }
}
