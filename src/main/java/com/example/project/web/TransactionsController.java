package com.example.project.web;

import com.example.project.model.Category;
import com.example.project.model.Transaction;
import com.example.project.model.TransactionType;
import com.example.project.model.User;
import com.example.project.service.CategoryService;
import com.example.project.service.TransactionsService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionsController {
    private final TransactionsService transactionsService;
    private final CategoryService categoryService;

    public TransactionsController(TransactionsService transactionsService, CategoryService categoryService) {
        this.transactionsService = transactionsService;
        this.categoryService = categoryService;

    }

    @GetMapping()
    public String getTransactionsPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) Double from, // amount from
            @RequestParam(required = false) Double to, // amount to
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String categoryName,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        if(user == null) throw new RuntimeException("User not in session.");
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        List<Transaction> transactions = transactionsService.listAllByUser(user.getId());
        Page<Transaction> page = this.transactionsService.findPage(from, to, type, categoryName, pageNum - 1, pageSize);
        model.addAttribute("page", page);
        model.addAttribute("transactions", transactions);
        model.addAttribute("categories", this.categoryService.findAll());
        model.addAttribute("types", List.of("INCOME","EXPENSE"));

        return "transactions";
    }

    @GetMapping("/add-form")
    public String addTransactionPage(Model model){
        List<Category> categories = this.categoryService.findAll();
        List<TransactionType> types = List.of(TransactionType.values());
        model.addAttribute("categories", categories);
        model.addAttribute("types", types);
        return "add-form";
    }
////TO DO -add time to do charts
    @PostMapping("/add")
    public String saveTransaction(@RequestParam(required = false) Long id,
                                  @RequestParam String category,
                                  @RequestParam String type,
                                  @RequestParam Double amount,
                                  @RequestParam String description,
                                  @RequestParam LocalDate date,
                                  @AuthenticationPrincipal User user,
                                  Model model){
        if(user == null){
            throw new RuntimeException("User not in session.");
        }
        try {
            this.transactionsService.save(user.getId(), category, type, amount, description, date);
        }catch (RuntimeException ex){
            model.addAttribute("hasError", true);
            model.addAttribute("error", ex.getMessage());
            return "add-form";
        }
        return "redirect:/transactions";
    }


}
