package com.pk.server.wechat.cms.service.impl;

import com.pk.server.wechat.cms.domain.Account;
import com.pk.server.wechat.cms.mapper.AccountDao;
import com.pk.server.wechat.cms.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao entityDao;

    public Account getById(String id) {
        return entityDao.getById(id);
    }

    public Account getByAccount(String account) {
        return entityDao.getByAccount(account);
    }

    public List<Account> listForPage(Account searchEntity) {
        return entityDao.listForPage(searchEntity);
    }

    public void add(Account entity) {
        entityDao.add(entity);
    }

    public void update(Account entity) {
        entityDao.update(entity);
    }

    public void delete(Account entity) {
        entityDao.delete(entity);
    }

}