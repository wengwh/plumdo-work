/*package com.plumdo.plumdoform;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;













import java.util.List;

import org.hibernate.criterion.Example;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.plumdo.form.entity.Banji;
import com.plumdo.form.entity.BanjiRepository;
import com.plumdo.form.entity.User;
import com.plumdo.form.jpa.Criteria;
import com.plumdo.form.jpa.Restrictions;
import com.plumdo.form.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserRepositoryTests {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BanjiRepository banjiRepository ;

	
	
	@Test
	public void test() throws Exception {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);        
		String formattedDate = dateFormat.format(date);
//		Criteria<Banji> c = new Criteria<Banji>();  
		Criteria<User> c = new Criteria<User>();  
		c.add(Restrictions.eq("email", 1, true)); 
		c.add(Restrictions.eq("Banji.id", 2, true)); 
//		System.out.println(userRepository.findAll(c));
		System.out.println(userRepository.findAll(c));
		
		Criteria<User> c = new Criteria<User>();  
		c.add(Restrictions.like("email", "bb", true)); 
		int page=1,size=10;
		List list = new ArrayList<>();
		list.add(new Sort.Order(Direction.DESC, "id"));
		list.add(new Sort.Order(Direction.DESC, "email"));
		Sort sort = new Sort(list);
	    Pageable pageable = new PageRequest(page, size, sort);
		userRepository.findAll(pageable);
		
//		System.out.println(userRepository.findAll(c));
		
		
		
//		userRepository.save(new User("aa1", "aa@126.com", "aa", "aa123456",formattedDate));
//		userRepository.save(new User("bb2", "bb@126.com", "bb", "bb123456",formattedDate));
//		userRepository.save(new User("cc3", "cc@126.com", "cc", "cc123456",formattedDate));
//		System.out.println(userRepository.findByUserNameOrEmail("bb2", "bb@126.com"));
//		Assert.assertEquals(3, userRepository.findAll().size());
//		Assert.assertEquals("bb123456", userRepository.findByUserNameOrEmail("bb2", "bb@126.com").getNickName());
//		userRepository.delete(userRepository.findByUserName("aa1"));
	}

}*/