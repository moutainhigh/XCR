package com.yatang.xc.xcr.web.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.busi.common.resp.Response;
import com.yatang.xc.mbd.biz.region.dto.RegionDTO;
import com.yatang.xc.mbd.biz.region.dubboservice.RegionDubboService;

@Controller
@RequestMapping(value = "xcr")
public class PageSkipAction {
	private static Logger log = LoggerFactory.getLogger(PageSkipAction.class);

	@Autowired
	private RegionDubboService regionDubboService;

	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public String mainPageSkip(HttpServletRequest request) {
		return "screen/main";
	}

	/**
	 * 左侧导航跳转到任务页
	 *
	 * @return
	 */
	@RequestMapping(value = "/mission.htm", method = { RequestMethod.POST, RequestMethod.GET })
	public String missionSkip() {
		return "screen/mission/missionManager";
	}

	/**
	 * 跳转到关联任务列表
	 *
	 * @return
	 */
	@RequestMapping(value = "relationMissionBackList.htm", method = { RequestMethod.POST, RequestMethod.GET })
	public String relationMissionBackList() {
		return "screen/mission/relationMissionBackList";
	}

	/**
	 * 普通任务列表页
	 *
	 * @return
	 */
	@RequestMapping(value = "normalMissionList.htm", method = { RequestMethod.POST, RequestMethod.GET })
	public String missionListSkip() {
		return "screen/mission/normalMissionList";
	}

	/**
	 * 跳转到关联任务列表
	 *
	 * @return
	 */
	@RequestMapping(value = "relationMissionList.htm", method = { RequestMethod.POST, RequestMethod.GET })
	public String missionRelListSkip() {
		return "screen/mission/relationMissionList";
	}

	/**
	 * 普通任务添加页
	 *
	 * @return
	 */
	@RequestMapping(value = "missionAdd.htm", method = { RequestMethod.POST, RequestMethod.GET })
	public String missionAddSkip() {
		return "screen/mission/normalMissionAdd";
	}

	/**
	 * 关联任务添加页
	 *
	 * @return
	 */
	@RequestMapping(value = "missionRelAdd.htm", method = { RequestMethod.POST, RequestMethod.GET })
	public String missionRelAddSkip() {
		return "screen/mission/relationMissionAdd";
	}

	/**
	 * 课程列表页
	 *
	 * @return
	 */
	@RequestMapping(value = "/train.htm", method = { RequestMethod.POST, RequestMethod.GET })
	public String trainSkip() {
		return "screen/train/classList";
	}

	/**
	 * 课程编辑页
	 *
	 * @return
	 */
	@RequestMapping(value = "trainAdd.htm", method = { RequestMethod.POST, RequestMethod.GET })
	public String trainAddSkip() {
		return "screen/train/classEdit";
	}

	/**
	 * 消息编辑页
	 *
	 * @return
	 */
	@RequestMapping(value = "msgEdit.htm", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView msgEditSkip(ModelMap map) {
		Response<List<RegionDTO>> response = regionDubboService.selectRegionByParentCode("");
		if (response == null) {
			return new ModelAndView("screen/msg/pushEdit");
		}
		List<RegionDTO> regionDTOList = response.getResultObject();
		if (CollectionUtils.isEmpty(regionDTOList)) {
			return new ModelAndView("screen/msg/pushEdit");
		}
		for (RegionDTO dto : regionDTOList) {
			System.out.println(dto);
		}
		map.put("regionDTOList", regionDTOList);
		return new ModelAndView("screen/msg/pushEdit", map);
	}

	/**
	 * 消息列表页
	 *
	 * @return
	 */
	@RequestMapping(value = "/msg.htm", method = { RequestMethod.POST, RequestMethod.GET })
	public String msgSkip() {
		return "screen/msg/pushList";
	}

	/**
	 * 广告列表页
	 *
	 * @return
	 */
	@RequestMapping(value = "/advertise.htm", method = { RequestMethod.POST, RequestMethod.GET })
	public String advertiseSkip() {
		return "screen/advertisement/advertisementList";
	}

	/**
	 * 广告添加页
	 * <method description>
	 *
	 * @return
	 */
	@RequestMapping(value = "turnAdAdd.htm", method = { RequestMethod.POST, RequestMethod.GET })
	public String turnAdAdd() {
		return "screen/advertisement/advertisementAdd";
	}

	/**
	 * 广告添加页
	 * <method description>
	 *
	 * @return
	 */
	@RequestMapping(value = "/advertiseAdd.htm", method = { RequestMethod.POST, RequestMethod.GET })
	public String advertiseAddSkip() {
		return "screen/advertisement/advertisementAdd";
	}

	/**
	 * 签到奖励页
	 *
	 * @return
	 */
	@RequestMapping(value = "/aaaaa.htm", method = { RequestMethod.POST, RequestMethod.GET })
	public String signAward() {
		return "screen/sign/award";
	}

	/**
	 * 操作统计
	 *
	 * @return
	 */
	@RequestMapping(value = "count/statistics", method = { RequestMethod.POST, RequestMethod.GET })
	public String statistics() {
		return "screen/dataCount/OperateMain";
	}

	/**
	 * 操作内容页面
	 *
	 * @return
	 */
	@RequestMapping(value = "count/statisticsMain", method = { RequestMethod.POST, RequestMethod.GET })
	public String statisticsMain() {
		return "screen/dataCount/StatisticsMain";
	}

	/**
	 * 跳转到满送红包页面
	 */
	@RequestMapping(value = "/fullS/fullSList", method = { RequestMethod.POST, RequestMethod.GET })
	public String redPList() {
		log.info("跳转到页面fullS/fullSList");
		return "screen/toc/fullSend/fullSendActivityList";
	}

	/**
	 * 跳转满送活动增加页面
	 *
	 * @return
	 */
	@RequestMapping(value = "activityAddPage.htm", method = { RequestMethod.POST, RequestMethod.GET })
	public String activityAddPage() {
		return "screen/toc/fullSend/activityAddPage";
	}

	/**
	 * 跳转到满送红包页面
	 */
	@RequestMapping(value = "/launchP/launchP", method = { RequestMethod.POST, RequestMethod.GET })
	public String launchP() {
		log.info("跳转到页面launchP/launchP");
		return "screen/toc/launchPage/launchPList";
	}
}
