package lhy.nrpc.test.provider.service;

import lhy.nrpc.common.annotation.NRpcService;
import lhy.nrpc.test.api.TestService;

@NRpcService
public class TestServiceImpl implements TestService{

	public String getName(String name) {
		// TODO Auto-generated method stub
		return name+"1234";
	}

}
