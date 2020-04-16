package ru.javawebinar.topjava.util;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.StringJoiner;

public class ErrorUtil {
    public static ResponseEntity<String> responseForControllers(BindingResult result) {
        StringJoiner joiner = new StringJoiner("<br>");
        result.getFieldErrors().forEach(
                fe -> joiner.add(String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
        );
        return ResponseEntity.unprocessableEntity().body(joiner.toString());
    }
}
