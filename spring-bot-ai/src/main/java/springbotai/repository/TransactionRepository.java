package springbotai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import springbotai.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}