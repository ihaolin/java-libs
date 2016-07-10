package me.hao0.cxf.service;

import me.hao0.cxf.model.User;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface UserService {
	
	@WebMethod
	Long create(User user);
	
	@WebMethod
	List<User> list();
	
	@WebMethod
	User load(Long id);
}
