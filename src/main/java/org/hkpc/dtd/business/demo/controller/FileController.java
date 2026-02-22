package org.hkpc.dtd.business.demo.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hkpc.dtd.common.controller.BaseController;
import org.hkpc.dtd.common.core.aspect.enums.ErrorCodeEnum;
import org.hkpc.dtd.common.core.aspect.exception.CodeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * This class is tested for uploading and downloading files using Postman
 */
@RestController
@RequestMapping(value = "/demo/file")
public class FileController extends BaseController {

    @Operation(summary = "test file upload", description = "")
    @PostMapping("/uploadFileTest")
    public Object uploadFileTest(@RequestParam("file") MultipartFile file) throws CodeException {
        if (file.isEmpty()) {
            throw new CodeException(ErrorCodeEnum.PARAM_FORMAT_INVALID);
        }
        /**
         * Test record: the fileSizeInKB is 3 (The decimal point is omitted after "/ 1024", so 3 is the round down value (向下取整); you can + 1 if you want the round up value(向上取整)).
         * The actually size from Windows file property is as below:
         * Size  3.43 KB (3,515 bytes)
         * Size on disk: 4.00 KB (4,096 bytes)
         */
        long fileSizeInKB = file.getSize() / 1024;
        logger.info("file name: {},fileSizeInKB: {}", file.getOriginalFilename(),fileSizeInKB);


        return null;
    }


    @Operation(summary = "test download Excel", description = "")
    @GetMapping("/downloadExcelTest")
    public ResponseEntity<Void> downloadExcelTest(HttpServletResponse response) throws CodeException {
        try (Workbook workbook = new XSSFWorkbook()) {
            // Create Excel Sheet
            Sheet sheet = workbook.createSheet("Data");
            // Header Row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("field 1");
            header.createCell(1).setCellValue("field 2");
            header.createCell(2).setCellValue("field 3");
            header.createCell(3).setCellValue("field 4");
            header.createCell(4).setCellValue("field 5");
            header.createCell(5).setCellValue("field 6");
            // Filter
            sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, 5));
            // Column Width
            sheet.setColumnWidth(0, 2400);
            sheet.setColumnWidth(1, 2400);
            sheet.setColumnWidth(2, 3900);
            sheet.setColumnWidth(3, 6000);
            sheet.setColumnWidth(4, 4000);
            sheet.setColumnWidth(5, 5500);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            // filename
            response.setHeader("Content-Disposition", "attachment; filename=excel_test.xlsx");
            // write to output stream
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            logger.error("downloadExcelTest error", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
