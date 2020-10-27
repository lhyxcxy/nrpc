package lhy.nrpc.core.route.impl;

import java.util.Random;
import java.util.TreeSet;

import lhy.nrpc.core.route.RpcLoadBalance;

/**
 * random
 *
 * @author lhy 2018-12-04
 */
public class XxlRpcLoadBalanceRandomStrategy extends RpcLoadBalance {

    private Random random = new Random();

    @Override
    public String route(String serviceKey, TreeSet<String> addressSet) {
        // arr
        String[] addressArr = addressSet.toArray(new String[addressSet.size()]);

        // random
        String finalAddress = addressArr[random.nextInt(addressSet.size())];
        return finalAddress;
    }

	@Override
	public void resetServiceKey(String serviceKey, String serverAdress) {
		// TODO Auto-generated method stub
		
	}



}
