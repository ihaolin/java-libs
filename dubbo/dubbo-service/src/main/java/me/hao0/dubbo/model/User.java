package me.hao0.dubbo.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class User implements Serializable{
	
	private static final long serialVersionUID = 8136799445197588519L;
	@NotNull(message="id can't be null")
	private Integer id;
	@Size(min=6, max=20, message="username length must be 6 to 20.")
	private String username;
	@Size(min=6, max=20, message="password length must be 6 to 20.")
	private String password;
	@NotNull(message="name can't be null")
	private String name;

	private Integer sex;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", name='" + name + '\'' +
				", sex=" + sex +
				'}';
	}
}
