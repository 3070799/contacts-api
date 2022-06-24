package com.ohol.pavel.contactsapi.config.aws;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pavel Ohol
 */
@RestController
public class AWSCheckHealthController {

    @GetMapping("/")
    public ResponseEntity<?> checkHealth() {
        return ResponseEntity.ok()
                .build();
    }
}
