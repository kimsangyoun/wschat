package com.ksy.wechat.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class CResponseErrorHandler implements ResponseErrorHandler {
	  @Override
	  public void handleError(ClientHttpResponse response) throws IOException {
	    // your error handling here
		  if (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
			  System.out.println("error handler");
			  System.out.println(response.getBody());
		  }
	  }

	  @Override
	  public boolean hasError(ClientHttpResponse response) throws IOException {
		  return response.getStatusCode().isError();
	  }
}