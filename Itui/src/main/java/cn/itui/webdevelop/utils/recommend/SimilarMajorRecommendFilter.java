package cn.itui.webdevelop.utils.recommend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.itui.webdevelop.model.MajorInfo;

public class SimilarMajorRecommendFilter implements MajorRecommendFilter{
	private static final Logger logger = Logger.getLogger(SimilarMajorRecommendFilter.class);
	public static final int SAMECOLLEGE_MAJORCOUNT = 8;
	public static final int SAMEMAJOR_MAJORCOUNT = 4;

	public MajorRecommendResult recommendMajorFilter(List<HashMap<String, Object>> candidates, String code, int majorId) {
		int simiCount = 0;
		int nearCount = 0;
		int corrCount = 0;
		int tranCount = 0;
		HashMap<String, ArrayList<HashMap<String, Object>>> codeDataMaps = new HashMap<String, ArrayList<HashMap<String,Object>>>();
		for(HashMap<String, Object> curData : candidates) {
			if((Integer)curData.get("id") != majorId){
				String curCode = (String)curData.get("code");
				if(codeDataMaps.containsKey(curCode)) {
					codeDataMaps.get(curCode).add(curData);
				}
				else {
					ArrayList<HashMap<String, Object>> tmpDatas = new ArrayList<HashMap<String,Object>>();
					tmpDatas.add(curData);
					codeDataMaps.put(curCode, tmpDatas);
				}
			}
		}
		ArrayList<HashMap<String, Object>> retMajors = new ArrayList<HashMap<String,Object>>();
		if(codeDataMaps.containsKey(code)) {
			addArrays(retMajors, codeDataMaps.get(code),0);
			simiCount = retMajors.size();
			codeDataMaps.remove(code);
		}
		if(retMajors.size() < SAMECOLLEGE_MAJORCOUNT) {
			HashMap<String, ArrayList<HashMap<String, Object>>> tmpCodeDataMaps = (HashMap<String, ArrayList<HashMap<String, Object>>>) codeDataMaps.clone();
			Set<Entry<String, ArrayList<HashMap<String, Object>>>> codeDataSets = tmpCodeDataMaps.entrySet();
			for(Entry<String, ArrayList<HashMap<String, Object>>> curEntry : codeDataSets) {
				if(curEntry.getKey().substring(0, 4).equals(code.substring(0, 4))) {
					addArrays(retMajors, curEntry.getValue(),1);
					codeDataMaps.remove(curEntry.getKey());
				}
				if(retMajors.size() >= SAMECOLLEGE_MAJORCOUNT) {
					nearCount = SAMECOLLEGE_MAJORCOUNT - simiCount;
					return new MajorRecommendResult(retMajors, simiCount, nearCount, corrCount, tranCount);
				}
			}
			if(retMajors.size() < SAMECOLLEGE_MAJORCOUNT) {
				nearCount = retMajors.size() - simiCount;
				tmpCodeDataMaps = (HashMap<String, ArrayList<HashMap<String, Object>>>) codeDataMaps.clone();
				codeDataSets = tmpCodeDataMaps.entrySet();
				for(Entry<String, ArrayList<HashMap<String, Object>>> curEntry : codeDataSets) {
					if(curEntry.getKey().substring(0, 2).equals(code.substring(0, 2))) {
						addArrays(retMajors, curEntry.getValue(),2);
						codeDataMaps.remove(curEntry.getKey());
					}
					if(retMajors.size() >= SAMECOLLEGE_MAJORCOUNT) {
						corrCount = SAMECOLLEGE_MAJORCOUNT - simiCount - nearCount;
						return new MajorRecommendResult(retMajors, simiCount, nearCount, corrCount, tranCount);
					}
				}
			}
		}
		return new MajorRecommendResult(retMajors, simiCount, nearCount, corrCount, tranCount);
	}
	
	private void addArrays(ArrayList<HashMap<String, Object>> retMajors, ArrayList<HashMap<String, Object>> toAdd, int color) {
		for(HashMap<String, Object> curMap : toAdd) {
			if(retMajors.size() >= SAMECOLLEGE_MAJORCOUNT)
				return;
			addOneElement(retMajors, curMap, color);
		}
	}
	
	private void addOneElement(List<HashMap<String, Object>> retMajors, HashMap<String, Object> toAdd, int color) {
		HashMap<String, Object> majorCurInfo = new HashMap<String, Object>();
		majorCurInfo.put("majorName", toAdd.get("name"));
		majorCurInfo.put("schoolName", toAdd.get("school"));
		majorCurInfo.put("color", color);
		double rate = (Double) toAdd.get("rate");
		majorCurInfo.put("value", MajorInfo.translateRate(rate));
		System.out.println("code:" + toAdd.get("code"));
		retMajors.add(majorCurInfo);
	}

	public MajorRecommendResult recommendMajorFilter(
			MajorRecommendResult recommendMajors,
			List<HashMap<String, Object>> candidateMajors, int collegeId, int majorId,
			String code) {
		try{
			int preCode = Integer.parseInt(code.substring(0, 2));
			int I = 2;
			while(recommendMajors.getMajors().size() <= SAMECOLLEGE_MAJORCOUNT) {
				for(HashMap<String, Object> curMap : candidateMajors) {
					if((Integer)curMap.get("id") != majorId) {
						try{
							String curCode = (String)curMap.get("code");
							int curPreCode = Integer.parseInt(curCode.substring(0, 2));
							if(Math.abs(curPreCode - preCode) < I) {
								addOneElement(recommendMajors.getMajors(), curMap, 3);
							}
							if(recommendMajors.getMajors().size() >= SAMECOLLEGE_MAJORCOUNT)
								return recommendMajors;
						}catch(Exception e) {
							continue;
						}
					}
				}
			I++;
			}
		}catch(Exception e) {
			logger.error("pre-code not interger.");
		}
		return recommendMajors;
	}

	public List<HashMap<String, Object>> recommendMajorFilter(
			List<HashMap<String, Object>> candidates, double rate) throws Exception{
		List<HashMap<String, Object>> resultMaps = new ArrayList<HashMap<String,Object>>();
		double curRate = 0;
		double lastRate = 0;
		if(candidates.size() == 0)
			return new ArrayList<HashMap<String,Object>>();
		curRate = (Double)candidates.get(0).get("applyAdmitRate");
		int index = 0;
		for(int i = 1; i < candidates.size(); i++) {
			lastRate = curRate;
			curRate = (Double)candidates.get(i).get("applyAdmitRate");
			if(rate < curRate && rate > lastRate) {
				index = i;
			}
		}
		HashMap<String, Object> tmpMap = candidates.get(index);
		double curAARate = (Double)tmpMap.get("applyAdmitRate");				
		tmpMap.put("applyAdmitRate", MajorInfo.translateRate(curAARate));
		resultMaps.add(tmpMap);
		for(int length = 1; resultMaps.size() < candidates.size(); length++) {
			if(index - length >= 0) {
				tmpMap = candidates.get(index - length);
				curAARate = (Double)tmpMap.get("applyAdmitRate");				
				tmpMap.put("applyAdmitRate", MajorInfo.translateRate(curAARate));
				resultMaps.add(tmpMap);
				if(resultMaps.size() >= SAMEMAJOR_MAJORCOUNT)
					return resultMaps;
			}
			if(index + length < candidates.size()) {
				tmpMap = candidates.get(index + length);
				curAARate = (Double)tmpMap.get("applyAdmitRate");				
				tmpMap.put("applyAdmitRate", MajorInfo.translateRate(curAARate));
				resultMaps.add(tmpMap);
				if(resultMaps.size() >= SAMEMAJOR_MAJORCOUNT)
					return resultMaps;
			}
		}
		
		return resultMaps;
	}

}
