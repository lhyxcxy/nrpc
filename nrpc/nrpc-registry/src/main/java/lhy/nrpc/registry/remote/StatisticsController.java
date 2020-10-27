package lhy.nrpc.registry.remote;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lhy.nrpc.common.reqres.http.BaseResponse;
import lhy.nrpc.registry.node.ConsumerRegistry;
import lhy.nrpc.registry.node.ProviderRegistry;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {
	
	@GetMapping("/info/providers")
	public BaseResponse<Map> providers(HttpServletRequest request){
		
		return BaseResponse.data(ProviderRegistry.getServiceAndAddressesMap());
	}
	@GetMapping("/info/consumers")
	public BaseResponse<Map> consumers(HttpServletRequest request){
		
		return BaseResponse.data(ConsumerRegistry.getAliveConsumerMap());
	}
	
	
}
