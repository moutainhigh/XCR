

DROP TABLE IF EXISTS YT_XCR_TRAIN_INFO;

create table YT_XCR_TRAIN_INFO (
	`ID` int (11) NOT NULL AUTO_INCREMENT,
	`NAME` varchar (150) COMMENT '课程名称',
	`CONTENT` text  COMMENT '内容',
	`TRAIN_LENGTH` int (11) COMMENT '建议培训时长',
	`ICON` varchar (765)  COMMENT '课程图标',
	`IS_MISSION` varchar (3) COMMENT '是否任务',
	`STATUS` varchar (3) COMMENT '状态(0:未发布1:已发布)',
	`VIEDIO_URL` varchar (765) COMMENT '视屏链接url',
	`IMAGES_URL` varchar (900) COMMENT '图片url(多图以,拼接)',
	`FILE_URL` varchar (900),
	`REMARK` varchar (600) COMMENT '备注',
	`CREATE_UID` int (10) COMMENT '创建人id',
	`CREATE_TIME` timestamp  COMMENT '创建时间(时间戳)',
	`MODIFY_UID` int (10) COMMENT '修改人id',
	`MODIFY_TIME` timestamp COMMENT '最后修改时间(时间戳)',
	`RELEASES_TIME` timestamp COMMENT '发布时间',
	 PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;