package com.pk.server.admin.service;

import com.pk.server.admin.model.Admin;

import java.util.List;
import java.util.Map;

public interface IAdminService {

     boolean login(Admin admin);

     List<Map<String,Integer>> one();

    List<Map<String,String>> two();

    List<Map<String,Integer>> index();

    List<Map<String,String>> three();

    List<Map<String,String>> four();
}
