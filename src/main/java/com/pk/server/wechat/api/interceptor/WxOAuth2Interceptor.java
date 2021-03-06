package com.pk.server.wechat.api.interceptor;

import com.pk.server.wechat.api.process.*;
import com.pk.server.wechat.cms.domain.AccountFans;
import com.pk.server.wechat.cms.service.AccountFansService;
import com.pk.server.wechat.core.util.HttpUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 微信客户端用户请求验证拦截器
 */
public class WxOAuth2Interceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AccountFansService accountFansService;

    /**
     * 开发者自行处理拦截逻辑，
     * 方便起见，此处只处理includes
     */
    private String[] excludes;//不需要拦截的
    private String[] includes;//需要拦截的

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		为方便展示的参数，开发者自行处理
//		boolean oauthFlag = false;
//		for(String s : includes){
//			//如果包含，就拦截
//			if(uri.contains(s)){
//				oauthFlag = true;
//				break;
//			}
//		}
//		如果不需要oauth认证
//		if(!oauthFlag){
//			return true;
//		}
        HttpSession session = request.getSession(true);
        String sessionid = request.getSession().getId();
        String openid = WxMemoryCacheClient.getOpenid(sessionid);//先从缓存中获取openid
        if (StringUtils.isBlank(openid)) {//没有，通过微信页面授权获取
            String code = request.getParameter("code");
            if (!StringUtils.isBlank(code)) {//如果request中包括code，则是微信回调
                try {
                    MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
                    AccountFans fans = WxApiClient.getFansInfoByOauth(openid, mpAccount, code);
                    if (!StringUtils.isBlank(fans.getOpenId())) {
                        WxMemoryCacheClient.setOpenid(sessionid, fans.getOpenId());//缓存openid
                        AccountFans fansTem = accountFansService.getByOpenId(fans.getOpenId());
                        if (fansTem == null) {
                            accountFansService.add(fans);
                        } else {
                            accountFansService.update(fans);
                        }
                        AccountFans loginFans = accountFansService.getByOpenId(fans.getOpenId());
                        session.setAttribute("loginUser", loginFans);
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {//oauth获取code
                MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
                String redirectUrl = HttpUtil.getRequestFullUriNoContextPath(request);//请求code的回调url
                String state = com.pk.server.wechat.api.interceptor.OAuth2RequestParamHelper.prepareState(request);
                String url = WxApi.getOAuthCodeUrl(mpAccount.getAppid(), redirectUrl, OAuthScope.Userinfo.toString(), state);
                HttpUtil.redirectHttpUrl(request, response, url);
                return false;
            }
        } else {
            return true;
        }
        HttpUtil.redirectUrl(request, response, "/error/101.html");
        return false;
    }

    public String[] getExcludes() {
        return excludes;
    }

    public void setExcludes(String[] excludes) {
        this.excludes = excludes;
    }

    public String[] getIncludes() {
        return includes;
    }

    public void setIncludes(String[] includes) {
        this.includes = includes;
    }

}

