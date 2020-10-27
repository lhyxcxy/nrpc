package lhy.nrpc.test.consumer.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lhy.nrpc.common.annotation.NRpcReference;
import lhy.nrpc.common.reqres.http.BaseResponse;
import lhy.nrpc.test.api.TestService;

@RestController
@RequestMapping("/test")
public class TestController {
	@NRpcReference
	private TestService testService;
	@GetMapping("/test")
	public BaseResponse<String> providerRegister(String name,HttpServletRequest request){
		String r=testService.getName(name);
		return BaseResponse.data(r);
	}
}