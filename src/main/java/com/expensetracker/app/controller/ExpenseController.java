package com.expensetracker.app.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expensetracker.app.entity.Expense;
import com.expensetracker.app.service.ExpenseService;

import jakarta.validation.Valid;

@RestController
public class ExpenseController {
	
	@Autowired
	private ExpenseService expenseService;
	
	@GetMapping("/expenses")
	public List<Expense> getAllExpences(Pageable page) {
		return expenseService.getAllExpenses(page).toList();
	}
	
	@GetMapping("/expenses/{id}")
	public Expense getExpenseById(@PathVariable("id") Integer id) {
		return expenseService.getExpenseById(id);
	}
	
	@DeleteMapping("/expenses")
	public void deleteExpenseById(@RequestParam Integer id) {
		expenseService.deleteExpenseById(id);
	}
	
	@PostMapping("/expenses")
	public Expense saveExpenseDetails(@Valid @RequestBody Expense expense) {
		return expenseService.saveExpenseDetails(expense);
	}
	
	@PutMapping("/expenses/{id}")
	public Expense updateExpenseDetails(@RequestBody Expense expense, @PathVariable("id") Integer id) {
		return expenseService.updateExpenseDetails(id, expense);
	}
	
	@GetMapping("/expenses/category")
	public List<Expense> getExpensesByCatergory(@RequestParam("category") String category, Pageable page) {
	
		return expenseService.readByCategory(category, page);
	}
	
	@GetMapping("/expenses/search")
	public List<Expense> getExpensesByName(@RequestParam("keyword") String keyword, Pageable page) {
	
		return expenseService.readByExpenseName(keyword, page);
	}
	
	@GetMapping("/expenses/date")
	public List<Expense> getExpensesByDate(@RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate, Pageable page) {
		return expenseService.readByDate(startDate, endDate, page);
	}

}
