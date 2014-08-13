package com.llq.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import com.llq.model.Person;

public class Scheme {

	JdbcTemplate jdbcTemplate = null;

	@Before
	public void testBeforeClass1() {

		ApplicationContext atc = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		jdbcTemplate = (JdbcTemplate) atc.getBean("jdbcTemplate");

	}

	/**
	 * 使用JdbcTemplate的execute()方法执行SQL语句
	 */
	@Test
	public void test1() {

		jdbcTemplate
				.execute("create table car(id int primary key,wheel int,color varchar(50))");

	}

	/**
	 * 如果是UPDATE或INSERT,可以用update()方法。
	 */
	@Test
	public void test2() {

		jdbcTemplate
				.update("insert into person(username,password,email)values('张三','123','zhangsan@163.com')");
	}

	/**
	 * 带参数的更新
	 */
	@Test
	public void test3() {

		String sql = "update person set username=? where id=?";
		jdbcTemplate.update(sql, new Object[] { "李四", 1 });
	}

	@Test
	public void test4() {

		String sql = "insert into person(username,password,email)values(?,?,?)";
		jdbcTemplate.update(sql, new Object[] { "王五", "123456",
				"wangwu@163.com" });
	}

	/**
	 * 使用JdbcTemplate进行查询时，使用queryForXXX()等方法
	 */
	@Test
	public void test5() {

		String sql = "select count(*) from person";
		int count = jdbcTemplate.queryForInt(sql);
		System.out.println(count);
	}

	@Test
	public void test6() {

		String sql = "select username from person where id=?";
		Object username = jdbcTemplate.queryForObject(sql, new Object[] { 1 },
				String.class);
		System.out.println((String) username);

	}

	@Test
	public void test7() {

		String sql = "select * from person";
		List list = jdbcTemplate.queryForList(sql);

		for (Object obj : list) {
			HashMap hm = (HashMap) obj;
			System.out.println(hm.get("id"));
			System.out.println(hm.get("username"));
			System.out.println(hm.get("password"));
			System.out.println(hm.get("email"));
		}
	}

	@Test
	public void test8() {

		String sql = "insert into person(username,password,email)values(?,?,?)";
		int count = jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {

				ps.setString(1, "赵六");
				ps.setString(2, "654321");
				ps.setString(3, "zhaoliu@163.com");
			}
		});

		System.out.println(count);

	}

	@Test
	public void test9() {

		String sql = "select * from person where id=?";

		jdbcTemplate.query(sql, new Object[] { 1 }, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {

				System.out.println(rs.getInt("id"));
				System.out.println(rs.getString("username"));
				System.out.println(rs.getString("password"));
				System.out.println(rs.getString("email"));

			}
		});
	}

	@Test
	public void test10() {

		String sql = "select * from person where id=?";

		jdbcTemplate.query(sql, new Object[] { 1 }, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int index) throws SQLException {

				System.out.println(rs.getInt("id"));
				System.out.println(rs.getString("username"));
				System.out.println(rs.getString("password"));
				System.out.println(rs.getString("email"));
				System.out.println(index);
				return null;
			}
		});
	}

	@Test
	public void test11() {

		String sql = "select * from person where id=?";

		Object[] args = new Object[] { 1 };
		Object user = this.jdbcTemplate.queryForObject(sql, args,
				new RowMapper() {
					@Override
					public Object mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Person user = new Person();
						user.setId(rs.getInt("id"));
						user.setUsername(rs.getString("username"));
						user.setPassword(rs.getString("password"));
						System.out.println(user.getUsername());
						return user;
					}

				});

	}

	@Test
	public void test12() {

		String sql = "select id,username,password,email from person where id=?";

		List list = jdbcTemplate.query(sql, new Object[] { 1 },
				new BeanPropertyRowMapper(Person.class));
		for (Object obj : list) {
			Person p = (Person) obj;

			System.out.println(p.getUsername());

		}

	}

	@Test
	public void test13() {

		String sql = "select id,username,password,email from person where id=?";

		Object obj = jdbcTemplate.queryForObject(sql, new Object[] { 1 },
				new BeanPropertyRowMapper(Person.class));
		Person p = (Person) obj;

		System.out.println(p.getUsername());

	}

	@Test
	public void test14() {

		String sql = "select id,username,password,email from person where id=?";

		Map map = jdbcTemplate.queryForMap(sql, new Object[] { 1 });

		System.out.println(map.get("username"));

	}

	@Test
	public void test15() {

		jdbcTemplate.update("insert into idcard(address)values('梧州')");
	}

	@Test
	public void test16() {

		String sql = "(select p.id from person p where id=1)union(select i.id  from idcard i where id=1)";

		List list = jdbcTemplate.queryForList(sql);
		
		for(Object obj:list){
			HashMap hm=(HashMap)obj;
			System.out.println(hm.get("id"));
		}


	}

}
