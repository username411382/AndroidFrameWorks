package com.ruihe.demo.common.utils;

import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Function：Excel 工具类
 * Author：rui.he
 * Date：2020/8/17 16:30
 */
public class ExcelUtils {
    /**
     * 读取Excel文件
     * a）Workbook、Sheet、Row、Cell等为接口；
     * b）HSSFWorkbook、HSSFSheet、HSSFRow、HSSFCell为97-2003版本对应的处理实现类；
     * c）XSSFWorkbook、XSSFSheet、XSSFRow、XSSFCell为2007+版本对应的处理实现类；
     *
     * @param file
     * @throws FileNotFoundException
     */
    public static void readExcel(File file) throws FileNotFoundException {
        if (file == null) {
            Log.logLongInfo("NullFile读取Excel出错，文件为空文件");
            return;
        }

        if (!checkIfExcelFile(file)) {
            ToastUtil.show("非法格式");
            return;
        }

        InputStream stream = new FileInputStream(file);
        try {
          /*  XSSFWorkbook workbook = new XSSFWorkbook(stream);
            XSSFSheet sheet = workbook.getSheetAt(0);*/
            Workbook workbook = WorkbookFactory.create(stream);
            Sheet sheet = workbook.getSheetAt(0); //示意访问sheet

            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            StringBuffer sb = new StringBuffer();
            for (int r = 0; r < rowsCount; r++) {
                if (r == 0) {
                    continue;
                }
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                String name = "";
                String tel = "";
                //每次读取一行的内容
                for (int c = 0; c < cellsCount; c++) {
                    //将每一格子的内容转换为字符串形式
                    String value = getCellAsString(row, c, formulaEvaluator);
                    String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                    Log.logLongInfo(cellInfo);
                    if (c == 0) {
                        name = value;
                    }
                    if (c == 1) {
                        tel = value;
                    }
                }
                sb.append(getTemp(VcfUtils.qpEncodeing(name), tel));
                sb.append("\n");
            }

            String vcfName = file.getName().split("\\.")[0];
            Log.logLongInfo("创建vcf名称：" + vcfName);
            File vcfFile = new File("/storage/emulated/0/Download" + File.separator + vcfName + ".vcf");
            if (!vcfFile.exists()) {
                Log.logLongInfo("创建vcf路径：" + vcfFile.getPath());
                vcfFile.getParentFile().mkdirs();
                vcfFile.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(vcfFile, "rwd");
            raf.seek(file.length());
            raf.write(sb.toString().getBytes());
            raf.close();
            ToastUtil.show("生成成功");
        } catch (Exception e) {
            /* proper exception handling to be here */
            Log.logLongInfo(e.toString());
        }

    }

    /**
     * 获取模块
     *
     * @param name
     * @param tel
     * @return
     */
    private static String getTemp(String name, String tel) {
        return "BEGIN:VCARD\n" +
                "VERSION:2.1\n" +
                "N;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:" + name + "\n" +
                "FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:" + name + "\n" +
                "TEL;CELL;VOICE:" + tel + "\n" +
                "END:VCARD";
    }

    /**
     * 读取excel文件中每一行的内容
     *
     * @param row
     * @param c
     * @param formulaEvaluator
     * @return
     */
    private static String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = new BigDecimal(numericValue).toPlainString();
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;
                default:
                    break;
            }
        } catch (NullPointerException e) {
            /* proper error handling should be here */
            Log.logLongInfo(e.toString());
        }
        return value;
    }

    /**
     * 根据类型后缀名简单判断是否Excel文件
     *
     * @param file 文件
     * @return 是否Excel文件
     */
    public static boolean checkIfExcelFile(File file) {
        if (file == null) {
            return false;
        }
        String name = file.getName();
        //”.“ 需要转义字符
        String[] list = name.split("\\.");
        //划分后的小于2个元素说明不可获取类型名
        if (list.length < 2) {
            return false;
        }
        String typeName = list[list.length - 1];
        //满足xls或者xlsx才可以
        return "xls".equals(typeName) || "xlsx".equals(typeName);
    }
}
