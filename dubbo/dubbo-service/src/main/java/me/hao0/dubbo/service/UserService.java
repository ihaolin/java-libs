package me.hao0.dubbo.service;

import me.hao0.dubbo.model.User;

import javax.validation.constraints.NotNull;

/**
 * 基于注解的dubbo服务
 */
public interface UserService {

	void login(String user, String pass);

	Boolean create(@NotNull User user);
}
