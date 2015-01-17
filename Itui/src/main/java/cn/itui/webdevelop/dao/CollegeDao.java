package cn.itui.webdevelop.dao;

import java.util.HashMap;
import java.util.List;

import cn.itui.webdevelop.model.College;


public interface CollegeDao {
	public HashMap<String, Object> findLogoAndRankByMajorId(int id);
	public List<College> findCollegeInRank(int rank, int collegeId);
}
