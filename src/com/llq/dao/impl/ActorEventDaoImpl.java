package com.llq.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.llq.model.Person;

public class ActorEventDaoImpl {

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private SimpleJdbcTemplate simpleJdbcTemplate;

	public Person findUser1(String username) {
		String sql = "select * from user where username=?";
		Object[] args = new Object[] { username };
		Object user = this.jdbcTemplate.queryForObject(sql, args,
				new RowMapper() {

					@Override
					public Object mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Person user = new Person();
						user.setId(rs.getInt("id"));
						user.setUsername(rs.getString("username"));
						user.setPassword(rs.getString("password"));
						return user;
					}

				});

		return (Person) user;
	}

	public Person findUser2(String username) {
		String sql = "select id,username,password from user where username=?";
		Object[] args = new Object[] { username };
		Object user = this.jdbcTemplate.queryForObject(sql, args,
				new BeanPropertyRowMapper(Person.class));

		return (Person) user;
	}

	public List findUser3(int id) {
		String sql = "select id,username,password from user where id<?";
		Object[] args = new Object[] { id };
		List list = this.jdbcTemplate.query(sql, args,
				new BeanPropertyRowMapper(Person.class));

		return list;
	}

	public int findUser4(int id) {
		String sql = "select count(*) from user";
		int count = this.jdbcTemplate.queryForInt(sql);

		return count;
	}

	public String findUser5(int id) {
		String sql = "select name from user where id=?";
		Object[] args = new Object[] { id };
		Object name = this.jdbcTemplate.queryForObject(sql, args, String.class);
		return (String) name;
	}

	public Map findUser6(int id) {
		String sql = "select id,username,password from user where id=?";
		Map map = this.jdbcTemplate.queryForMap(sql);

		return map;
	}

	// ���ض��map
	public List findUser7(int id) {
		String sql = "select id,username,password from user where id=?";
		List list = this.jdbcTemplate.queryForList(sql);

		return list;
	}

	
	public List findUser8(int id) {
		String sql = "select id,username,password from user where id=?";
		this.jdbcTemplate.execute(new ConnectionCallback() {

			@Override
			public Object doInConnection(Connection paramConnection)
					throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				return null;
			}
		});

		return null;
	}

	/**
	 * NamedParameterJdbcTemplate
	 */

	public Person findUser8(Person user) {

		String sql = "select id,username,password from user where username=:name and password=:pswd";
		Map map = new HashMap();
		map.put("name", user.getUsername());
		map.put("pswd", user.getPassword());
		Object obj = this.namedParameterJdbcTemplate.queryForObject(sql, map,
				new BeanPropertyRowMapper(Person.class));

		return (Person) obj;

	}

	public Person findUser9(Person user) {

		String sql = "select id,username,password from user where username=:username and password=:password";
		
		SqlParameterSource ps=new BeanPropertySqlParameterSource(user);
		Object obj = this.namedParameterJdbcTemplate.queryForObject(sql, ps,
				new BeanPropertyRowMapper(Person.class));

		return (Person) obj;

	}
	
	
	public Person findUser10(Person user) {

		String sql = "insert into user(id,username,password) values(:id,:username,:password)";
		
		SqlParameterSource ps=new BeanPropertySqlParameterSource(user);
		KeyHolder kh=new GeneratedKeyHolder();
		this.namedParameterJdbcTemplate.update(sql, ps, kh);

		int id=kh.getKey().intValue();
		user.setId(id);
		
		
		Map map=kh.getKeys();
		return (Person) user;

	}
	

	

}
