package lhy.nrpc.registry.remote;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lhy.nrpc.common.exception.NRpcException;
import lhy.nrpc.common.reqres.http.BaseResponse;
import lhy.nrpc.common.reqres.http.node.BaseProviderNode;
import lhy.nrpc.common.reqres.http.registry.AcquireAliveProviderRequest;
import lhy.nrpc.common.reqres.http.registry.RegisterConsumerRequest;
import lhy.nrpc.common.reqres.http.registry.RegisterProviderRequest;
import lhy.nrpc.registry.config.RegistryConfig;
import lhy.nrpc.registry.node.ConsumerRegistry;
import lhy.nrpc.registry.node.ProviderRegistry;

@RestController
@RequestMapping("/remote")
public class RemoteController {
	@Autowired
	private RegistryConfig registryConfig;
	
	public void checkAccesstoken(String accesstoken){
		if(!registryConfig.getAccesstokens().contains(accesstoken))
			throw new NRpcException("访问不合法！！！");
	}
	@PostMapping("/provider/register")
	public BaseResponse<String> providerRegister(@RequestBody  @Valid RegisterProviderRequest rsRequest,HttpServletRequest request){
		checkAccesstoken(rsRequest.getAccesstoken());
		ProviderRegistry.register(rsRequest.getProviderNode());
		return BaseResponse.SUCCESS;
	}
	
	@PostMapping("/provider/unRegister")
	public BaseResponse<String> providerUnregister(@RequestBody @Valid RegisterProviderRequest rsRequest,HttpServletRequest request){
		checkAccesstoken(rsRequest.getAccesstoken());
		ProviderRegistry.unRegister(rsRequest.getProviderNode().getAppid());
		return BaseResponse.SUCCESS;
	}
	
	@PostMapping("/provider/aliveProviders")
	public BaseResponse<Set<BaseProviderNode>> providerAliveservice(@RequestBody @Valid AcquireAliveProviderRequest aaRequest,HttpServletRequest request){
		checkAccesstoken(aaRequest.getAccesstoken());
		 BaseResponse<Set<BaseProviderNode>> d=BaseResponse.data(ProviderRegistry.getAliveProviderNodes(aaRequest.getServiceIds()));
		 return d;
	}
	
	
	@PostMapping("/consumer/register")
	public BaseResponse<String> consumerRegister(@RequestBody @Valid RegisterConsumerRequest rsRequest,HttpServletRequest request){
		checkAccesstoken(rsRequest.getAccesstoken());
		ConsumerRegistry.register(rsRequest.getConsumerrNode());
		return BaseResponse.SUCCESS;
	}
	
	@PostMapping("/consumer/unRegister")
	public BaseResponse<String> consumerUnregister(@RequestBody @Valid RegisterConsumerRequest rsRequest,HttpServletRequest request){
		checkAccesstoken(rsRequest.getAccesstoken());
		ConsumerRegistry.unRegister(rsRequest.getConsumerrNode().getAppid());
		return BaseResponse.SUCCESS;
	}
	
}
