package org.study.subjectresource.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class OperationNonPermittedException extends RuntimeException{
    private final String errorMessage;
    private final LocalDateTime timestamp;
    private final String operationName;
    private final String source;
}
