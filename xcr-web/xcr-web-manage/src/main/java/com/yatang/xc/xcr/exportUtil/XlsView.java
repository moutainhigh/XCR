package com.yatang.xc.xcr.exportUtil;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * 
 * <class description> 导出XLS文档视图
 * 
 * @author: zhoubaiyun
 * @version: 1.0, 2017年7月18日
 */
public class XlsView extends AbstractXlsView {
	private final Log log = LogFactory.getLog(this.getClass());
	private static final String				EXTENSION			= ".xls";

	private static final String				DEFAULT_FILENAME	= "default";

	private static final SimpleDateFormat	FORMATOR			= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long s = System.currentTimeMillis();
		XlsData xlsData = (XlsData) model.get("xlsData");
		if (null == xlsData) {
			throw new RuntimeException("this no XlsData find in Model!");
		}
		String fileName = null == xlsData.getWorkBookName() ? DEFAULT_FILENAME : xlsData.getWorkBookName();

		Map<Class<?>, List<?>> sheetData = xlsData.getSheetData();
		if (null == sheetData || sheetData.isEmpty()) {
			throw new RuntimeException("this no sheetData find in XlsData!");
		}
		for (Class<?> clazz : sheetData.keySet()) {

			XlsSheet xlsSheet = clazz.getAnnotation(XlsSheet.class);
			if (null == xlsSheet) {
				throw new RuntimeException(
						"Model " + clazz.getClass().getName() + " must be annotationed by XlsSheet !");
			}

			HSSFSheet sheet = (HSSFSheet) workbook.createSheet(xlsSheet.value());
			Field[] fields = clazz.getDeclaredFields();
			List<Field> annotationedField = new ArrayList<>();

			int headerIndex = 0;
			for (Field field : fields) {
				XlsHealder xlsHealder = field.getAnnotation(XlsHealder.class);
				if (null != xlsHealder) {
					if (-1 != xlsHealder.width()) {
						sheet.setColumnWidth(headerIndex, xlsHealder.width() * 256);
					} else {
						sheet.setColumnWidth(headerIndex, xlsHealder.value().getBytes().length * 256);
					}
					annotationedField.add(field);
					HSSFCell cell = getCell(sheet, 0, headerIndex++);
					cell.setCellValue(xlsHealder.value());
				}
			}
			int dataRowIndex = 1;
			int dataClosIndex = 0;
			for (Object obj : sheetData.get(clazz)) {
				for (Field field : annotationedField) {
					Object result = obj.getClass().getMethod("get" + StringUtils.capitalize(field.getName()))
							.invoke(obj);
					HSSFCell cell = getCell(sheet, dataRowIndex, dataClosIndex++);
					setCellValue(cell, result);
				}
				dataRowIndex++;
				dataClosIndex = 0;
			}
		}
		long end = System.currentTimeMillis();
		log.info("Excel视图解析器耗时"+(end-s));
		response.setHeader("Content-disposition",
				"attachment;filename=" + encodeFilename(fileName + EXTENSION, request));
	}



	public void setCellValue(HSSFCell cell, Object result) {

		if (null == result) {
			cell.setCellValue("");
		}
		if (result instanceof String) {

			cell.setCellValue((String) result);

		} else if (result instanceof Integer) {

			cell.setCellValue((Integer) result);

		} else if (result instanceof Long) {

			cell.setCellValue((Long) result);

		} else if (result instanceof Date) {

			cell.setCellValue(FORMATOR.format((Date) result));

		} else if (result instanceof Double) {

			cell.setCellValue((Double) result);

		} else if (result instanceof Boolean) {

			cell.setCellValue((Boolean) result);

		} else if (result instanceof BigDecimal) {

			cell.setCellValue(((BigDecimal) result).doubleValue());

		} else if (result instanceof Float) {

			cell.setCellValue((Float) result);

		}

	}



	/**
	 * 解决火狐中文文件名乱码问题
	 * 
	 * @param filename
	 * @param request
	 * @return
	 */
	public String encodeFilename(String filename, HttpServletRequest request) {
		String agent = request.getHeader("USER-AGENT");
		try {
			if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {
				String newFileName = URLEncoder.encode(filename, "UTF-8");
				return newFileName;
			}
			if (null != agent && agent.indexOf("Firefox") > 0) {

				return new String(filename.getBytes("UTF-8"), "ISO8859-1");
			}
			return URLEncoder.encode(filename, "UTF-8");
		} catch (Exception ex) {
			return filename;
		}
	}



	protected HSSFCell getCell(HSSFSheet sheet, int row, int col) {
		HSSFRow sheetRow = sheet.getRow(row);
		if (sheetRow == null) {
			sheetRow = sheet.createRow(row);
		}
		HSSFCell cell = sheetRow.getCell(col);
		if (cell == null) {
			cell = sheetRow.createCell(col);
		}
		return cell;
	}
}