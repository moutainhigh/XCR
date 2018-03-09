package com.yatang.xc.xcr.biz.mission.service;

import java.util.Date;

public interface BuryingPointStatisticsService {

	public long queryByNameAndDate(String name, Date startDate, Date endDate);

}
