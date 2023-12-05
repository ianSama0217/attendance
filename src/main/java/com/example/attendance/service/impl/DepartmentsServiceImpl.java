package com.example.attendance.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.Departments;
import com.example.attendance.repository.DepartmentsDao;
import com.example.attendance.service.ifs.DepartmentsService;
import com.example.attendance.vo.DepartmentsCreateReq;
import com.example.attendance.vo.DepartmentsCreateRes;

@Service
public class DepartmentsServiceImpl implements DepartmentsService {

	@Autowired
	private DepartmentsDao dao;

	@Override
	public DepartmentsCreateRes create(DepartmentsCreateReq req) {
		// 檢查一個List或map是否為空 CollectionUtils
		if (CollectionUtils.isEmpty(req.getDepartments())) {
			return new DepartmentsCreateRes(RtnCode.PARAM_ERROR);
		}
		//id和name不為空值 -> 將id加入idList
		List<String> idList = new ArrayList<String>();
		for (Departments item : req.getDepartments()) {
			if (!StringUtils.hasText(item.getId()) || //
					!StringUtils.hasText(item.getName())) {
				return new DepartmentsCreateRes(RtnCode.PARAM_ERROR);
			}
			idList.add(item.getId());
		}

		//檢查id是否已經存在
		if(dao.existsByIdIn(idList)) {
			return new DepartmentsCreateRes(RtnCode.ID_HAS_EXISTED);
		}
		
		dao.saveAll(req.getDepartments());
		return new DepartmentsCreateRes(RtnCode.SUCCESSFUL);
	}

}
