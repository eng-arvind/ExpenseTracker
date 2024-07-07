package com.expensetracker.app.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.expensetracker.app.entity.Expense;
import com.expensetracker.app.exceptions.ResourceNotFoundException;
import com.expensetracker.app.repository.ExpenseRepository; 

@Service
public class ExpenseServiceImpl implements ExpenseService {
	 
	@Autowired
	private ExpenseRepository expenseRepo;
	
	@Autowired
	private UserService userService;

	@Override
	public Page<Expense> getAllExpenses(Pageable page) { 
		return expenseRepo.findByUserId(userService.getLoggedInUser().getId(), page);
	} 

	@Override
	public Expense getExpenseById(Integer id) {
		Optional<Expense> expense =  expenseRepo.findByUserIdAndId(userService.getLoggedInUser().getId(),id);
		if(expense.isPresent()) {
			return expense.get();
		}
		throw new ResourceNotFoundException("Expense is not found for the id "+ id);
	}

	@Override
	public void deleteExpenseById(Integer id) {
		Expense expense = getExpenseById(id);
		expenseRepo.delete(expense);
		
	}

	@Override
	public Expense saveExpenseDetails(Expense expense) {
		expense.setUser(userService.getLoggedInUser());
		return expenseRepo.save(expense);
	}

	@Override
	public Expense updateExpenseDetails(Integer id, Expense expense) {
		Expense existingExp = getExpenseById(id);
		existingExp.setExpenseName(expense.getExpenseName() != null ? expense.getExpenseName() : existingExp.getExpenseName());
		existingExp.setDescription(expense.getDescription() != null ? expense.getDescription() : existingExp.getDescription());
		existingExp.setCategory(expense.getCategory() != null ? expense.getCategory() : existingExp.getCategory());
		existingExp.setExpenseAmount(expense.getExpenseAmount() != null ? expense.getExpenseAmount() : existingExp.getExpenseAmount());
		existingExp.setDate(expense.getDate() != null ? expense.getDate() : existingExp.getDate());
		return expenseRepo.save(existingExp); 
	}

	@Override
	public List<Expense> readByCategory(String category, Pageable page) {
		return expenseRepo.findByUserIdAndCategory(userService.getLoggedInUser().getId(),category, page).toList();
	}

	@Override
	public List<Expense> readByExpenseName(String keyword, Pageable page) {
		return expenseRepo.search(keyword, page).toList();
	}

	@Override
	public List<Expense> readByDate(Date startDate, Date endDate, Pageable page) {
		if(startDate==null) {
			startDate = new Date(0);
		}
		
		if(endDate==null) {
			endDate = new Date(System.currentTimeMillis());
		}
		return expenseRepo.findByUserIdAndDateBetween(userService.getLoggedInUser().getId(),startDate, endDate, page).toList();
	}

}
