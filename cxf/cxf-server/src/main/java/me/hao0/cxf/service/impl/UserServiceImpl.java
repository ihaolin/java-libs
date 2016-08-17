package me.hao0.cxf.service.impl;

import me.hao0.cxf.model.User;
import me.hao0.cxf.service.UserService;

import javax.jws.WebService;
import java.util.*;

@WebService(
		serviceName="UserService", 
		portName="UserServicePort", 
		targetNamespace="http://org.haolin.cxf.service.cxf.haolin.org/")
public class UserServiceImpl implements UserService {
	private Map<Long, User> users = new HashMap<Long, User>();
	
	@Override
	public Long create(User user) {
		Long id = users.size() + 1L;
		users.put(id, user);
		return id;
	}

	@Override
	public List<User> list() {
		List<User> list = new ArrayList<User>();
		Iterator<User> it = users.values().iterator();
		while (it.hasNext()){
			User u = it.next();
			list.add(u);
		}
		Collections.sort(list, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				return (int)(o1.getId() - o2.getId());
			}
		});
		return list;
	}

	@Override
	public User load(Long id) {
		return users.get(id);
	}
}
