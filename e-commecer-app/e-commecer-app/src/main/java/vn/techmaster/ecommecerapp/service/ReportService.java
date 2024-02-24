package vn.techmaster.ecommecerapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import vn.techmaster.ecommecerapp.entity.OrderTable;
import vn.techmaster.ecommecerapp.exception.BadRequestException;
import vn.techmaster.ecommecerapp.model.dto.OrderUserDetailDto;
import vn.techmaster.ecommecerapp.model.dto.PaymentVoucherDto;
import vn.techmaster.ecommecerapp.repository.OrderTableRepository;
import vn.techmaster.ecommecerapp.repository.PaymentVoucherRepository;
import vn.techmaster.ecommecerapp.utils.DateUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final OrderTableRepository orderTableRepository;
    private final PaymentVoucherRepository paymentVoucherRepository;

    public Date getStartDate(String startDateString) {
        log.info("Start date string: {}", startDateString);
        Date startDate = Date.from(
                LocalDate.now()
                        .withDayOfMonth(1)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant());

        if (startDateString != null) {
            try {
                startDate = new SimpleDateFormat("dd-MM-yyyy").parse(startDateString);
            } catch (ParseException e) {
                log.error("Error when parse start date: {}", e.getMessage());
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        startDate = calendar.getTime();
        return startDate;
    }

    public Date getEndDate(String endDateString) {
        log.info("End date string: {}", endDateString);
        Date endDate = Date.from(
                LocalDate.now()
                        .with(TemporalAdjusters.lastDayOfMonth())
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant());

        if (endDateString != null) {
            try {
                endDate = new SimpleDateFormat("dd-MM-yyyy").parse(endDateString);
            } catch (ParseException e) {
                log.error("Error when parse start date: {}", e.getMessage());
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        endDate = calendar.getTime();
        return endDate;
    }

    private Map<String, Date> getDate(String startDateString, String endDateString) {
        Date startDate = getStartDate(startDateString);
        Date endDate = getEndDate(endDateString);

        if (startDate.after(endDate)) {
            throw new BadRequestException("Start date phải nhỏ hơn end date");
        }

        return Map.of("start", startDate, "end", endDate);
    }

    public List<OrderUserDetailDto> getAllOrders(String startDateString, String endDateString) {
        Map<String, Date> dateMap = getDate(startDateString, endDateString);
        Date start = dateMap.get("start");
        Date end = dateMap.get("end");

        return orderTableRepository.getAllOrdersInRangeTimeByStatus(start, end, OrderTable.Status.COMPLETE.toString());
    }

    public List<PaymentVoucherDto> getAllPaymentVouchers(String startDateString, String endDateString) {
        Map<String, Date> dateMap = getDate(startDateString, endDateString);
        Date start = dateMap.get("start");
        Date end = dateMap.get("end");

        return paymentVoucherRepository.getAllPaymentVouchersDtoInRangeTime(start, end);
    }

    public Integer calculateOrderRevenueAmount(String startDateString, String endDateString) {
        Map<String, Date> dateMap = getDate(startDateString, endDateString);
        Date start = dateMap.get("start");
        Date end = dateMap.get("end");

        return orderTableRepository
                .findByOrderDateBetweenAndStatus(start, end, OrderTable.Status.COMPLETE)
                .stream()
                .mapToInt(OrderTable::getTotalAmount)
                .sum();
    }

    public Integer calculateTotalPaymentVoucherAmount(String startDateString, String endDateString) {
        Map<String, Date> dateMap = getDate(startDateString, endDateString);
        Date start = dateMap.get("start");
        Date end = dateMap.get("end");

        return paymentVoucherRepository
                .getAllPaymentVouchersDtoInRangeTime(start, end)
                .stream()
                .mapToInt(PaymentVoucherDto::getAmount)
                .sum();
    }

    public Integer calculateTotalPaymentVoucherAmount(List<PaymentVoucherDto> paymentVouchers) {
        return paymentVouchers.stream()
                .mapToInt(PaymentVoucherDto::getAmount)
                .sum();
    }

    public byte[] generateReport(String startDateString, String endDateString) {
        try (Workbook workbook = new XSSFWorkbook()) {
            // Create CellStyle for title row
            CellStyle titleStyle = createTitleStyle(workbook);
            // Create CellStyle for date row
            CellStyle dateStyle = createDateStyle(workbook);
            // Create CellStyle for header row
            CellStyle headerStyle = createHeaderStyle(workbook);
            // Create CellStyle for data cells
            CellStyle dataStyle = createDataStyle(workbook);

            // Create "Báo cáo doanh thu" sheet
            Sheet revenueSheet = workbook.createSheet("Báo cáo doanh thu");
            createSheetHeaderOfRevenueSheet(revenueSheet, "Báo cáo doanh thu");
            List<OrderUserDetailDto> orderList = getAllOrders(startDateString, endDateString);
            createSheetContentOfRevenueSheet(revenueSheet, orderList);

            // Create "Báo cáo thu chi" sheet
            Sheet incomeExpenseSheet = workbook.createSheet("Báo cáo thu chi");
            createSheetHeaderOfIncomeExpenseSheet(incomeExpenseSheet, "Báo cáo thu chi");
            List<PaymentVoucherDto> paymentVoucherList = getAllPaymentVouchers(startDateString, endDateString);
            createSheetContentOfIncomeExpenseSheet(incomeExpenseSheet, paymentVoucherList);

            // Apply styles to the sheets
            applyStyles(revenueSheet, titleStyle, dateStyle, headerStyle, dataStyle);
            applyStyles(incomeExpenseSheet, titleStyle, dateStyle, headerStyle, dataStyle);

            // Write the workbook content to a ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException("Có lỗi xảy ra khi tạo báo cáo: " + e.getMessage());
        }
    }

    private void createSheetHeaderOfRevenueSheet(Sheet sheet, String reportType) {
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));

        int rowNum = 0;
        Row titleRow = sheet.createRow(rowNum++);
        titleRow.createCell(0).setCellValue(reportType);

        Row dateRow = sheet.createRow(rowNum++);
        dateRow.createCell(0).setCellValue("Thời gian: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        Row headerRow = sheet.createRow(rowNum);
        headerRow.createCell(0).setCellValue("Mã đơn");
        headerRow.createCell(1).setCellValue("Ngày đặt hàng");
        headerRow.createCell(2).setCellValue("Họ tên");
        headerRow.createCell(3).setCellValue("Số điện thoại");
        headerRow.createCell(4).setCellValue("Email");
        headerRow.createCell(5).setCellValue("Số tiền");
        headerRow.createCell(6).setCellValue("Trạng thái đơn hàng");
    }

    private void createSheetHeaderOfIncomeExpenseSheet(Sheet sheet, String reportType) {
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));

        int rowNum = 0;
        Row titleRow = sheet.createRow(rowNum++);
        titleRow.createCell(0).setCellValue(reportType);

        Row dateRow = sheet.createRow(rowNum++);
        dateRow.createCell(0).setCellValue("Thời gian: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        Row headerRow = sheet.createRow(rowNum);
        headerRow.createCell(0).setCellValue("Mã phiếu");
        headerRow.createCell(1).setCellValue("Ngày tạo");
        headerRow.createCell(2).setCellValue("Nội dung chi");
        headerRow.createCell(3).setCellValue("Số tiền");
        headerRow.createCell(4).setCellValue("Người tạo");
        headerRow.createCell(5).setCellValue("Ghi chú");
    }

    private void createSheetContentOfRevenueSheet(Sheet sheet, List<OrderUserDetailDto> orderList) {
        int rowNum = 3; // Starting row after title and header
        for (OrderUserDetailDto order : orderList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(order.getOrderNumber());
            row.createCell(1).setCellValue(DateUtils.formatDate(order.getOrderDate()));
            row.createCell(2).setCellValue(order.getUsername());
            row.createCell(3).setCellValue(order.getPhone());
            row.createCell(4).setCellValue(order.getEmail());
            row.createCell(5).setCellValue(order.getTotalAmount());
            row.createCell(6).setCellValue(order.getStatus().getDisplayValue());
        }
    }

    private void createSheetContentOfIncomeExpenseSheet(Sheet sheet, List<PaymentVoucherDto> paymentVoucherList) {
        int rowNum = 3; // Starting row after title and header
        for (PaymentVoucherDto paymentVoucher : paymentVoucherList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(paymentVoucher.getId());
            row.createCell(1).setCellValue(DateUtils.formatDate(paymentVoucher.getCreatedAt()));
            row.createCell(2).setCellValue(paymentVoucher.getPurpose());
            row.createCell(3).setCellValue(paymentVoucher.getAmount());
            row.createCell(4).setCellValue(paymentVoucher.getUser().getUsername());
            row.createCell(5).setCellValue(paymentVoucher.getNote());
        }
    }

    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(font);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setItalic(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);
        return style;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.MEDIUM);
        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setFont(font);
        return style;
    }

    private void applyStyles(Sheet sheet, CellStyle titleStyle, CellStyle dateStyle, CellStyle headerStyle, CellStyle dataStyle) {
        // Apply title style to the title row
        sheet.getRow(0).getCell(0).setCellStyle(titleStyle);

        // Apply date style to the date row
        sheet.getRow(1).getCell(0).setCellStyle(dateStyle);

        // Apply header style to the header row
        Row headerRow = sheet.getRow(2);
        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
            headerRow.getCell(i).setCellStyle(headerStyle);
        }

        // Apply data style to all data cells
        for (int i = 3; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            for (Cell cell : row) {
                cell.setCellStyle(dataStyle);
            }
        }
    }
}
