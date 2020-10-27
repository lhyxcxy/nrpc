package lhy.nrpc.registry.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegistryConfig {
	/**
	 * 心跳超时时间S
	 */
	@Value("${nrpc.register_heartbat_outtime}")
	private static long register_heartbat_outtime=10;
	/**
	 * 注册端的accesstoken,多个英文逗号隔开
	 */
	@Value("${nrpc.accesstokens}")
	private  Set<String> accesstokens=new HashSet<>();

	
	public static long getRegister_heartbat_outtime() {
		return register_heartbat_outtime;
	}

	public static void setRegister_heartbat_outtime(long register_heartbat_outtime) {
		RegistryConfig.register_heartbat_outtime = register_heartbat_outtime;
	}

	public  Set<String> getAccesstokens() {
		return accesstokens;
	}

	public  void setAccesstokens(Set<String> accesstokens) {
		this.accesstokens = accesstokens;
	}

	
	
}
