package com.example.project.web;

import com.example.project.model.Budget;
import com.example.project.model.Category;
import com.example.project.model.User;
import com.example.project.service.BudgetService;
import com.example.project.service.CategoryService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

// TODO: 08.6.2025 FIX SAVE OR UPDATE
@Controller
@RequestMapping("/budget")
public class BudgetController {
    private final BudgetService budgetService;
    private final CategoryService categoryService;

    public BudgetController(BudgetService budgetService, CategoryService categoryService) {
        this.budgetService = budgetService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getBudgetPage(Model model,
                                @AuthenticationPrincipal User user){
        if(user == null){
            throw new RuntimeException("User not in session.");
        }

        List<Category> categoryList = categoryService.findAll();
        Map<Long, Double> allocatedAmounts = budgetService.getAllocatedAmountsByUser(user.getId());
        List<Budget> budgets = budgetService.listByUser(user.getId()); // Implement this method
        model.addAttribute("budgets", budgets);
        model.addAttribute("categories", categoryList);
        model.addAttribute("allocatedAmounts", allocatedAmounts);
        double totalBudget=budgets.stream().mapToDouble(i->i.getAmount()).sum();
        model.addAttribute("totalBudget",totalBudget);
        return "budget";
    }

   //  edit budget amounts
@PostMapping("/save-all")
public String saveAllBudgets(@RequestParam("budgetPeriod") String budgetPeriod,
                             @RequestParam Map<String, String> allParams,
                             @AuthenticationPrincipal User user,
                             Model model)
{
    if (user == null) {
        System.out.println("No user in session.");
        model.addAttribute("hasError", true);
        model.addAttribute("error", "User not in session");
        return "budgettemp";
    }
    try {
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            if (entry.getKey().startsWith("amounts[")) {
                String key = entry.getKey().substring(8, entry.getKey().length() - 1);
                Long categoryId = Long.parseLong(key);
                Double amount = Double.parseDouble(entry.getValue());

                if (amount >= 0) {
                    Category category = categoryService.find(categoryId);
                    budgetService.save(user.getId(), amount, LocalDateTime.now(), budgetPeriod, category);
                }
            }
        }
        return "redirect:/home";

    } catch (Exception ex) {
        model.addAttribute("hasError", true);
        model.addAttribute("error", ex.getMessage());
        return "budgettemp";
    }
}



    // TODO: 08.6.2025 IMPLEMENT AFTER IMPLEMENTING THE TRANSACTIONS AND EXPENSES 
    @PostMapping("/update/{id}")
    public String updateBudget(@PathVariable Long id,
                               @SessionAttribute("user") User user,
                               @RequestParam Double amount){
        return "";
    }

}
