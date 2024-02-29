package com.example.banksystem.account;

import com.example.banksystem.account.entity.BankAccount;
import com.example.banksystem.common.repository.GenericSpecificationRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends GenericSpecificationRepository<BankAccount, Integer>, JpaSpecificationExecutor<BankAccount> {

}
