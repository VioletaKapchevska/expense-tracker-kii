package com.example.project.web;

import com.example.project.model.Budget;
import com.example.project.model.Transaction;
import com.example.project.model.User;
import com.example.project.service.ChartsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping({"/charts"})
public class ChartsController {
    private final ChartsService service;

    public ChartsController(ChartsService service) {
        this.service = service;
    }
    @GetMapping()
    public String showCharts(@AuthenticationPrincipal User user, Model model)
    {
        if(user==null)
        {
            throw new RuntimeException("User not in session.");

        }


        List<Budget> budgets = service.getAllBudgets(user.getId());

        Map<String, Double> spentMap = service.getSpentAmounts(user.getId());
        List<List<Object>> incomevsexpenses = budgets.stream()
                .map(b -> {
                    Double spent = spentMap.getOrDefault(b.getCategory().getName(), 0.0);
                    return Arrays.asList((Object) b.getCategory().getName(), b.getAmount(), spent);
                })
                .collect(Collectors.toList());
        model.addAttribute("incomeVsExpenses", service.getTotalIncomeVsExpenses(user.getId()));
       model.addAttribute("top5categories",service.getTop3ExpenseCategories(user.getId()));
        model.addAttribute("income", incomevsexpenses);
        model.addAttribute("budgets", service.getAllBudgets(user.getId()));
        model.addAttribute("categoryExpenses", service.getCategoryExpenses(user.getId()));
        model.addAttribute("last5days",service.getTransactionsLast5Days(user.getId()));
        // Balance history (for line chart)
        return "charts";
    }
}
