package com.expensetracker.app.repository;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.expensetracker.app.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
	
	// select * from tbl_expenses where category = category;
	Page<Expense> findByUserIdAndCategory(int userId, String category, Pageable page);
	
	 @Query("SELECT e FROM Expense e WHERE LOWER(e.expenseName) LIKE LOWER(concat('%', :keyword, '%')) " +
	           "OR LOWER(e.description) LIKE LOWER(concat('%', :keyword, '%')) " +
	           "OR LOWER(e.category) LIKE LOWER(concat('%', :keyword, '%'))")
	Page<Expense> search(@Param("keyword") String keyword, Pageable page);
	 
	Page<Expense> findByUserIdAndDateBetween(int userId, Date startDate,Date endDate, Pageable page);
	
	Page<Expense> findByUserId(int userId, Pageable page);
	
	Optional<Expense> findByUserIdAndId(int userId, int expenseId);
	
}
