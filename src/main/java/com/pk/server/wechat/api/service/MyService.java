package com.pk.server.wechat.api.service;

import com.pk.server.wechat.api.process.MpAccount;
import com.pk.server.wechat.api.vo.MsgRequest;
import com.pk.server.wechat.cms.domain.AccountFans;
import net.sf.json.JSONObject;

/**
 * 我的微信服务接口，主要用于结合自己的业务和微信接口
 */
public interface MyService {

    //消息处理
    public String processMsg(MsgRequest msgRequest, MpAccount mpAccount);

    //发布菜单
    public JSONObject publishMenu(String gid, MpAccount mpAccount);

    //删除菜单
    public JSONObject deleteMenu(MpAccount mpAccount);

    //获取用户列表
    public boolean syncAccountFansList(MpAccount mpAccount);

    //获取单个用户信息
    public AccountFans syncAccountFans(String openId, MpAccount mpAccount, boolean merge);

    //根据openid 获取粉丝，如果没有，同步粉丝
    public AccountFans getFansByOpenId(String openid, MpAccount mpAccount);

}



