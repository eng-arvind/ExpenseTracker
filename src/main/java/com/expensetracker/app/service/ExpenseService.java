package com.expensetracker.app.service;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.expensetracker.app.entity.Expense;


public interface ExpenseService {

	Page<Expense> getAllExpenses(Pageable page);
	
	Expense getExpenseById(Integer id);
	
	void deleteExpenseById(Integer id);
	
	Expense saveExpenseDetails(Expense espense);
	
	Expense updateExpenseDetails(Integer id, Expense expense);
	
	List<Expense> readByCategory(String category, Pageable page);
	
	List<Expense> readByExpenseName(String keyword, Pageable page);
	
	List<Expense> readByDate(Date startDate, Date endDate, Pageable page);
}

