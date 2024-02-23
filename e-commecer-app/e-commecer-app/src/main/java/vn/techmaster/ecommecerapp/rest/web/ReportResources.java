package vn.techmaster.ecommecerapp.rest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.techmaster.ecommecerapp.service.ReportService;

@RestController
@RequestMapping("/api/v1/admin/reports")
@RequiredArgsConstructor
public class ReportResources {
    private final ReportService reportService;

    @GetMapping("/export-report")
    public ResponseEntity<?> exportReport(@RequestParam(required = false) String start,
                                          @RequestParam(required = false) String end) {
        byte[] reportDate = reportService.generateReport(start, end);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=report.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(reportDate);
    }

}
