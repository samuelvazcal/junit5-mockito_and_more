package org.aguzman.test.springboot.app.controllers;

import org.aguzman.test.springboot.app.models.BankAccount;
import org.aguzman.test.springboot.app.models.TransactionDto;
import org.aguzman.test.springboot.app.services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    @Autowired
    private BankAccountService accountService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BankAccount> toList() {
        return accountService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BankAccount detail(@PathVariable Long id) {
        return accountService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BankAccount save(@RequestBody BankAccount account) {
        return accountService.save(account);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransactionDto dto) {
        accountService.transfer(dto.getOriginAccount(),dto.getDestinyAccount(),dto.getAmount(),dto.getId());
        /* once the transaction was successfully completed, sends a ResponseEntity */

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status","OK");
        response.put("message","Successfully Transfer!");
        response.put("transaction",dto);

        /* it will return a JSON, the previous map */
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        accountService.deleteById(id);
    }


}
