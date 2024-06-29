package org.jeecg.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: ExcelUtils<T> excel导出通用方法
 * @Author: nbacheng
 * @Date:   2023-03-01
 * @Version: V1.0
 */
@Slf4j
public class ExcelUtils<T> {
		 
	 public void exportExcel(HttpServletResponse response, String title, String[] headers, String[] columns, Collection<T> dataset, String filename, String datePattern){
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(title);
			// 设置表格默认列宽度为15个字节
			sheet.setDefaultColumnWidth((int) 15);

			// 生成一个样式（用于标题）
			HSSFCellStyle style = workbook.createCellStyle();
			// 设置这些样式
			style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.SKY_BLUE.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setBorderBottom(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN); 
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setAlignment(HorizontalAlignment.CENTER);
			// 生成一个字体
			HSSFFont font = workbook.createFont();
			font.setColor(HSSFColor.HSSFColorPredefined.VIOLET.getIndex());
			font.setFontHeightInPoints((short) 12);
			font.setBold(true);
			// 把字体应用到当前的样式
			style.setFont(font);
			
			// 生成并设置另一个样式（用于内容）
			HSSFCellStyle style2 = workbook.createCellStyle();
			style2.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.getIndex());
			style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style2.setBorderBottom(BorderStyle.THIN);
			style2.setBorderLeft(BorderStyle.THIN);
			style2.setBorderRight(BorderStyle.THIN);
			style2.setBorderTop(BorderStyle.THIN);
			style2.setAlignment(HorizontalAlignment.CENTER);
			style2.setVerticalAlignment(VerticalAlignment.CENTER);
			// 生成另一个字体
			HSSFFont font2 = workbook.createFont();
			font2.setBold(true);
			// 把字体应用到当前的样式
			style2.setFont(font2);

			// 产生表格标题行
			HSSFRow row = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style);
				HSSFRichTextString text = new HSSFRichTextString(headers[i]);
				cell.setCellValue(text);
			}
			
			// 遍历集合数据，产生数据行
			Iterator<T> it = dataset.iterator();
			int index = 0;
			while (it.hasNext()) {
				index++;
				row = sheet.createRow(index);
				T t = (T) it.next();
				// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
				//Field[] fields = t.getClass().getDeclaredFields();
				//for (int i = 0; i < fields.length; i++) {
				for (int i = 0; i < columns.length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(style2);
					//Field field = fields[i];
					//String fieldName = field.getName();
					String fieldName = columns[i];
					String getMethodName = "get"
							+ fieldName.substring(0, 1).toUpperCase()
							+ fieldName.substring(1);
					try {
						Class<? extends Object> tCls = t.getClass();
						Method getMethod = tCls.getMethod(getMethodName,
								new Class[] {});
						Object value = getMethod.invoke(t, new Object[] {});
						// 判断值的类型后进行强制类型转换
						String textValue = null;

						if (value instanceof Boolean) {
							boolean bValue = (Boolean) value;
							textValue = "男";
							if (!bValue) {
								textValue = "女";
							}
						} else if (value instanceof Date) {
							Date date = (Date) value;
							SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
							textValue = sdf.format(date);
						} else {
							// 其它数据类型都当作字符串简单处理
							if(Objects.nonNull(value)) {
								textValue = value.toString();
							}
							else {
								textValue = "";
							}
							
						}
						// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
						if (textValue != null) {
							Pattern p = Pattern.compile("^//d+(//.//d+)?$");
							Matcher matcher = p.matcher(textValue);
							if (matcher.matches()) {
								// 是数字当作double处理
								cell.setCellValue(Double.parseDouble(textValue));
							} else {
								HSSFRichTextString richString = new HSSFRichTextString(
										textValue);
								HSSFFont font3 = workbook.createFont();
								font3.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
								richString.applyFont(font3);
								cell.setCellValue(richString);
							}
						}
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} finally {
						// 清理资源
					}
				}
			}
			try {
				//OutputStream out = new FileOutputStream("/opt/upFiles/"+filename);
				//workbook.write(out);
		        response.setCharacterEncoding("UTF-8");
		        response.setHeader("content-Type", "application/vnd.ms-excel");
		        response.setHeader("Content-Disposition",
		                    "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
		        workbook.write(response.getOutputStream());
				//out.close();
				log.info("导出成功");
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		 }
}
