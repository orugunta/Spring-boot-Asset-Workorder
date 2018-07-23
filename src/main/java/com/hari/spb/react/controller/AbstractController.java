package com.hari.spb.react.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hari.spb.react.exception.BadRequestException;
import com.hari.spb.react.exception.AppErrors;
import com.hari.spb.react.exception.NotFoundException;
import com.hari.spb.react.exception.ServiceException;
import com.hari.spb.react.exception.TimeoutException;
import com.hari.spb.react.model.JsonErrorResponseBody;

@ControllerAdvice
public abstract class AbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractController.class);
	
	@ExceptionHandler({
		BadRequestException.class, 
		HttpMessageNotReadableException.class, 
		HttpMessageNotWritableException.class,
		MissingServletRequestParameterException.class, 
		ServletRequestBindingException.class,
		MethodArgumentNotValidException.class})
	@ResponseBody
	protected JsonErrorResponseBody badRequest(Exception ex, HttpServletResponse response) {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		return new JsonErrorResponseBody(
				HttpStatus.BAD_REQUEST,
				ex,
				AppErrors.DEFAULT_BAD_REQUEST);
	}
	
	@ExceptionHandler(NotFoundException.class)
	@ResponseBody
	protected JsonErrorResponseBody notFound(Exception ex, HttpServletResponse response) {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.NOT_FOUND.value());
		return new JsonErrorResponseBody(
				HttpStatus.NOT_FOUND,
				ex, 
				AppErrors.DEFAULT_NOT_FOUND);
	}
	
	@ExceptionHandler({HttpMediaTypeNotSupportedException.class, HttpMediaTypeNotAcceptableException.class})
	@ResponseBody
	protected JsonErrorResponseBody unsupportedMediaType(Exception ex, HttpServletResponse response) {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
		return new JsonErrorResponseBody(
				HttpStatus.UNSUPPORTED_MEDIA_TYPE,
				ex, 
				AppErrors.DEFAULT_UNSUPPORTED_MEDIA_TYPE);
	}
	
	@ExceptionHandler(TimeoutException.class)
	@ResponseBody
	protected JsonErrorResponseBody timeout(Exception ex, HttpServletResponse response) {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.REQUEST_TIMEOUT.value());
		return new JsonErrorResponseBody(
				HttpStatus.REQUEST_TIMEOUT,
				ex, 
				AppErrors.DEFAULT_REQUEST_TIMEOUT);
	}
	
	@ExceptionHandler(ServiceException.class)
	@ResponseBody
	protected JsonErrorResponseBody dppError(Exception ex, HttpServletResponse response) {
		if (response != null) {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new JsonErrorResponseBody(
				HttpStatus.INTERNAL_SERVER_ERROR,
				ex, 
				AppErrors.INTERNAL_ERROR);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	protected JsonErrorResponseBody internalServerError(Exception ex, HttpServletResponse response) {
		try {
			LOG.info("Internal server error: ", ex);

			if (isBadRequestException(ex)) {
				return badRequest(ex, response);
			} else if (ex instanceof NotFoundException) {
				return notFound(ex, response);
			} else if (isUnsupportedMediaTypeException(ex)) {
				return unsupportedMediaType(ex, response);
			} else if (ex instanceof TimeoutException) {
				return timeout(ex, response);
			} else if (ex instanceof ServiceException) {
				return dppError(ex, response);
			} else {
				return dppError(ex, response);
			}
		} catch (Exception e) {
			// to catch internal error handling's internal issues (if any)
			LOG.error("{}", e);
			return dppError(ex, response);
		}
	}
	
	private boolean isBadRequestException(Exception ex) {
		return ex instanceof BadRequestException 
				|| ex instanceof HttpMessageNotReadableException
				|| ex instanceof HttpMessageNotWritableException
				|| ex instanceof MissingServletRequestParameterException 
				|| ex instanceof ServletRequestBindingException 
				|| ex instanceof MethodArgumentNotValidException; 
	}
	
	private boolean isUnsupportedMediaTypeException(Exception ex) {
		return ex instanceof HttpMediaTypeNotSupportedException
				|| ex instanceof HttpMediaTypeNotAcceptableException;
	}
}
