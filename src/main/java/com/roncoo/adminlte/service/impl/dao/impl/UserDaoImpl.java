/*
 * Copyright 2015-2016 RonCoo(http://www.roncoo.com) Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.roncoo.adminlte.service.impl.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.roncoo.adminlte.bean.entity.RcUser;
import com.roncoo.adminlte.bean.entity.RcUserExample;
import com.roncoo.adminlte.bean.entity.RcUserExample.Criteria;
import com.roncoo.adminlte.service.impl.dao.UserDao;
import com.roncoo.adminlte.service.impl.dao.impl.mybatis.RcUserMapper;
import com.roncoo.adminlte.util.Base64Util;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private RcUserMapper mapper;

	@Override
	public RcUser selectUser(String userno, String password) {
		RcUserExample example = new RcUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserNoEqualTo(userno);
		criteria.andPasswordEqualTo(Base64Util.encrypt(password));
		List<RcUser> result = mapper.selectByExample(example);
		if (result.size() > 0) {
			RcUser user = result.get(0);
			user.setPassword(password);
			return user;
		}
		return null;
	}

	@Override
	public RcUser selectByUserNo(String userno) {
		RcUserExample example = new RcUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserNoEqualTo(userno);
		List<RcUser> result = mapper.selectByExample(example);
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public int insert(RcUser rcUser) {
		Date date = new Date();
		String password = Base64Util.encrypt(rcUser.getPassword());
		rcUser.setPassword(password);
		rcUser.setCreateTime(date);
		rcUser.setUpdateTime(date);
		return mapper.insertSelective(rcUser);
	}

	@Override
	public int update(RcUser rcUser) {
		rcUser.setUpdateTime(new Date());
		return mapper.updateByPrimaryKeySelective(rcUser);
	}

	@Override
	public int deleteById(Long id) {
		return mapper.deleteByPrimaryKey(id);
	}

}
