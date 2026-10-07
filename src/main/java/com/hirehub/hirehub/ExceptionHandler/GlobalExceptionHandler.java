package com.hirehub.hirehub.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hirehub.hirehub.responseDto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handlerRuntimeException(RuntimeException runtimeException ){
    	
//    	ErrorResponse error =new Errorresponose
    	
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(runtimeException.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handelException(Exception exception){
    	
    	ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value()
    			, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "Something Went Wrong");
    	
    	return ResponseEntity
    			.internalServerError()
    			.body(exception.getMessage());
    }
    
   
    @ExceptionHandler(EmailAlreradyExist.class)
    public ResponseEntity<ErrorResponse> handlerEmailAlreradyExist(EmailAlreradyExist emailAlreradyExist){
    	
    	ErrorResponse errorResponse=new ErrorResponse(HttpStatus.CONFLICT.value(),
    			HttpStatus.CONFLICT.getReasonPhrase()
    			,emailAlreradyExist.getMessage());
    	
    	return ResponseEntity
    			.status(HttpStatus.CONFLICT)
    			.body(errorResponse);
    	
    }
    /////  Validation error handling 
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception ){
    	
    	Map<String,String> errors=new HashMap<>();
    	
    	exception.getBindingResult()
    	.getFieldErrors()
    	.forEach(error -> errors.put(
    			error.getField(),error.getDefaultMessage()	
    			)
    		);

    	ErrorResponse errorResponse=new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
    			HttpStatus.BAD_REQUEST.getReasonPhrase()
    			,"validation Failed",errors);
    	
    	return ResponseEntity
    			.status(HttpStatus.BAD_REQUEST)
    			.body(errorResponse);
    }
    	
    	@ExceptionHandler(UserNotFoundException.class)
    	public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException exception){
    		
    		ErrorResponse error= new ErrorResponse(HttpStatus.NOT_FOUND.value()
    				, HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getMessage());
    		
    		return ResponseEntity
    				.status(HttpStatus.NOT_FOUND)
    				.body(error);
    			
    	}
}