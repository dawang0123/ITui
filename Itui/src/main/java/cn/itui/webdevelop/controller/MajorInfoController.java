package cn.itui.webdevelop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itui.webdevelop.service.FollowService;
import cn.itui.webdevelop.service.MajorInfoService;
import cn.itui.webdevelop.utils.EnDeCode;
import cn.itui.webdevelop.utils.RequestUtil;

/**
 * 管理major info的控制器
 * @author jimmycai
 *
 */
@Controller
public class MajorInfoController {
	private static Log rRLogger = LogFactory.getLog("requestResponse");
	public static final String USERPASSWORD = "userPassword";
	private static final String MAJORID = "majorId";
	private MajorInfoService majorInfoService;
	private FollowService followService;
	
	@RequestMapping(value=URLConstants.GETMAJORINFO)
	public String getMajorInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String majorIdStr = request.getParameter(MAJORID);
		int majorId = EnDeCode.decodePara(majorIdStr);
		String requestStr = RequestUtil.getUserBaseInfo(request) + MAJORID + ":" + majorId;
		rRLogger.info(requestStr);
		String retJson = majorInfoService.getMajorInfo(request, majorId);
		return retJson;
	}
	
	@RequestMapping(value=URLConstants.GETRETESTINFO)
	public String getRetestInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String majorIdStr = request.getParameter(MAJORID);
		int majorId = EnDeCode.decodePara(majorIdStr);
		String requestStr = RequestUtil.getUserBaseInfo(request) + MAJORID + ":" + majorId;
		rRLogger.info(requestStr);
		String retJson = majorInfoService.getRetestInfo(majorId);
		return retJson;
	}
	
	@RequestMapping(URLConstants.FOLLOWMAJOR)
	public String followMajor(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String userPassword = request.getParameter(USERPASSWORD);
		//TODO decode for college id
		int majorId = Integer.parseInt(request.getParameter(MAJORID));
		String requestStr = RequestUtil.getUserBaseInfo(request) + USERPASSWORD + ":" + userPassword + "\t" + MAJORID + ":" + majorId;
		rRLogger.info(requestStr);
		return followService.followMajor(userPassword, majorId);
	}
	
	@RequestMapping(URLConstants.DISFOLLOWMAJOR)
	public String disFollowMajor(HttpServletRequest request, HttpServletResponse response) throws Exception{
		int id = Integer.parseInt(request.getParameter("id"));
		String requestStr = RequestUtil.getUserBaseInfo(request) + "ID:" + id;
		rRLogger.info(requestStr);
		return followService.deleteFollowMajor(id);
	}
	
	@RequestMapping(URLConstants.GETFOLLOWMAJOR)
	public String getFollowMajors(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String userPassword = request.getParameter(USERPASSWORD);
		String requestStr = RequestUtil.getUserBaseInfo(request) + USERPASSWORD + ":" + userPassword;
		rRLogger.info(requestStr);
		return followService.getFollowMajors(userPassword);
	}

	public MajorInfoService getMajorInfoService() {
		return majorInfoService;
	}

	public void setMajorInfoService(MajorInfoService majorInfoService) {
		this.majorInfoService = majorInfoService;
	}

	public FollowService getFollowService() {
		return followService;
	}

	public void setFollowService(FollowService followService) {
		this.followService = followService;
	}

}
