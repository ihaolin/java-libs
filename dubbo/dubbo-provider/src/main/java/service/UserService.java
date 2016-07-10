package service;

import model.User;

import javax.validation.constraints.NotNull;

/**
 * 基于注解的dubbo服务
 */
public interface UserService {
	void login(String user, String pass);
	void create(@NotNull User user);
}
