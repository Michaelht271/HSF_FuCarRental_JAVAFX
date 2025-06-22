package com.michael.fu.hsf301assigment1.controller.admin;

import com.michael.fu.hsf301assigment1.controller.BaseController;
import com.michael.fu.hsf301assigment1.service.impl.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/admin/reports")
public class ReportController extends BaseController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping({"", "/"})
    public String showReportPage() {
        return "admin/reports";
    }

    @PostMapping("/generate")
    public String generateReport(@RequestParam("startDate") LocalDate startDate,
                                 @RequestParam("endDate") LocalDate endDate,
                                 @RequestParam(value = "reportType", required = false, defaultValue = "summary") String reportType,
                                 Model model) {
        logger.info("Generating report from {} to {} type {}", startDate, endDate, reportType);
        Map<String, Object> data = reportService.generateReport(startDate, endDate);
        model.addAllAttributes(data);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "admin/reports";
    }

    // placeholder for export
    @GetMapping("/export")
    public String exportReport(@RequestParam("type") String type) {
        // TODO: implement export logic
        return "redirect:/api/v1/admin/reports";
    }
}