package cn.bidlink.framework.core.support.rmi;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.remoting.rmi.ContextPropagatingRemoteInvocationFactory;

import cn.bidlink.framework.core.annotation.AutowiredService;
import cn.bidlink.framework.core.exceptions.InnerErrorException;
import cn.bidlink.framework.core.loadbalance.BalanceType;
import cn.bidlink.framework.core.loadbalance.ConsistHashLoadBalancer;
import cn.bidlink.framework.core.loadbalance.RandomLoadBalancer;
import cn.bidlink.framework.core.loadbalance.RoundRobinLoadBalancer;
import cn.bidlink.framework.core.loadbalance.WeightedRandomLoadBalancer;
import cn.bidlink.framework.core.loadbalance.WeightedRoundRobinLoadBalancer;
import cn.bidlink.framework.core.support.Autowire;
import cn.bidlink.framework.core.support.BalanceInfo;
import cn.bidlink.framework.core.support.RemoteBalance;
import cn.bidlink.framework.core.support.RemoteConfig;
 /**
 * @description:	 TODO add description
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-8-30		下午2:15:28
 * 
 */
public class RmiAutowire implements Autowire {
	
	public static  RmiAutowire instance = null; 
	
	private RmiAutowire(){}
 
	private static boolean isInited = false;
 
	public synchronized static RmiAutowire getInstance() {
		if (instance == null) {
			instance = new RmiAutowire();
		}
		return instance;
	}
	
	@Override
	public synchronized Object autoWireInject(AutowiredService ars, 
			Class<?> interfaces,
			RemoteConfig remoteConfig) {
		if(!isInited) {
			initAllBalance(remoteConfig);
			isInited = true;
		}
		
		String [] groupKeys = ars.groupKey();
		String groupKey = "";
		if (!ArrayUtils.isEmpty(groupKeys)) {
			groupKey = groupKeys[0]; //默认取第一个key
		} else {
			groupKey = remoteConfig.getDefaultRmiGroupKey(); 
		}
		if (StringUtils.isEmpty(groupKey)) {
			return null;
		}
		
		Object obj = remoteConfig.getGroupConfigMap().get(groupKey);
		if (obj == null) {
			throw new InnerErrorException(interfaces.getName() +"AutowiredService groupkey ["+groupKey+"] is not existing!");
		}
	 
		 RmiServiceConfig rsc = (RmiServiceConfig)obj;

		 BidRmiProxyFactoryBean bpfb = new BidRmiProxyFactoryBean(groupKey,rsc,interfaces);
		 if(rsc.isEnableAuth()) {
			 bpfb.setSuperRemoteInvocationFactory(new ContextPropagatingRemoteInvocationFactory());
		 }
		 return bpfb.afterPropertiesSetLater().getObject();

 	}
	
	/**
	 * @description 初始化负载配置
	 * @param remoteConfig
	 */
	private void initAllBalance(RemoteConfig remoteConfig) {
 
		Map<String, Object> configMap = remoteConfig.getGroupConfigMap();
		for(Iterator<Map.Entry<String, Object>> it = configMap.entrySet().iterator();it.hasNext();) {
			Map.Entry<String, Object> entry = it.next();
			String groupKey = entry.getKey();
			Object obj = entry.getValue();
			if(obj instanceof RmiServiceConfig) {
				RmiServiceConfig rsc = (RmiServiceConfig)obj;
				RemoteBalance rb = rsc.getRemoteBalance();
				
				if (rb != null) {
					BalanceType btype = rb.getBalanceType();
					List<BalanceInfo> listBs = rb.getBalanceInfos();
					for (BalanceInfo bi : listBs) {
						String key =  groupKey;
						String value = bi.getHost()+"_"+bi.getPort();
						
						if (BalanceType.WEIGTHED_ROUNDROBIN == btype) {
							if (BidRmiProxyFactoryBean.getWeightRoundRoubinBalanceMap().get(key) !=null) {
								List<String> listProcesses = BidRmiProxyFactoryBean.getWeightRoundRoubinBalanceMap().get(key).getProcessors();
								List<Integer> listInt = BidRmiProxyFactoryBean.getWeightRoundRoubinBalanceMap().get(key).getWeights();
								listProcesses.add(value);
								listInt.add(bi.getWeight());
								BidRmiProxyFactoryBean.getWeightRoundRoubinBalanceMap().put(key, new WeightedRoundRobinLoadBalancer<String>(listProcesses, listInt));
							} else {
								List<String> newList = new CopyOnWriteArrayList<String>();
								List<Integer> newListInt = new CopyOnWriteArrayList<Integer>();
								newList.add(value);
								newListInt.add(bi.getWeight());
								BidRmiProxyFactoryBean.getWeightRoundRoubinBalanceMap().put(key, new WeightedRoundRobinLoadBalancer<String>(newList, newListInt));
							}
 						} else if (BalanceType.WEIGTHED_RANDOM == btype) {
 							if (BidRmiProxyFactoryBean.getWeightRandomRoubinBalanceMap().get(key) !=null) {
								List<String> listProcesses = BidRmiProxyFactoryBean.getWeightRandomRoubinBalanceMap().get(key).getProcessors();
								List<Integer> listInt = BidRmiProxyFactoryBean.getWeightRandomRoubinBalanceMap().get(key).getWeights();
								listProcesses.add(value);
								listInt.add(bi.getWeight());
								BidRmiProxyFactoryBean.getWeightRandomRoubinBalanceMap().put(key, new WeightedRandomLoadBalancer<String>(listProcesses, listInt));
							} else {
								List<String> newList = new CopyOnWriteArrayList<String>();
								List<Integer> newListInt = new CopyOnWriteArrayList<Integer>();
								newList.add(value);
								newListInt.add(bi.getWeight());
								BidRmiProxyFactoryBean.getWeightRandomRoubinBalanceMap().put(key, new WeightedRandomLoadBalancer<String>(newList, newListInt));
							}
 						} else if (BalanceType.ROUNDROBIN == btype) {
 							if (BidRmiProxyFactoryBean.getRoundrobinBalanceMap().get(key) !=null) {
 								BidRmiProxyFactoryBean.getRoundrobinBalanceMap().get(key).addProcessor(value);
							} else {
 								RoundRobinLoadBalancer<String> rrlb = new RoundRobinLoadBalancer<String>();
 								rrlb.addProcessor(value);
 								BidRmiProxyFactoryBean.getRoundrobinBalanceMap().put(key, rrlb);
							}
 						} else if (BalanceType.HASH == btype) {
 							if (BidRmiProxyFactoryBean.getHashBalanceMap().get(key) !=null) {
								List<String> listProcesses = BidRmiProxyFactoryBean.getHashBalanceMap().get(key).getProcessors();
								List<Integer> listInt = BidRmiProxyFactoryBean.getHashBalanceMap().get(key).getWeights();
								listProcesses.add(value);
								listInt.add(bi.getWeight());
								BidRmiProxyFactoryBean.getHashBalanceMap().put(key, new ConsistHashLoadBalancer<String>(listProcesses, listInt));
							} else {
								List<String> newList = new CopyOnWriteArrayList<String>();
								List<Integer> newListInt = new CopyOnWriteArrayList<Integer>();
								newList.add(value);
								newListInt.add(bi.getWeight());
								BidRmiProxyFactoryBean.getHashBalanceMap().put(key, new ConsistHashLoadBalancer<String>(newList, newListInt));
							}
 						} else if (BalanceType.RANDOM == btype) {
 							if (BidRmiProxyFactoryBean.getRandomBalanceMap().get(key) !=null) {
 								BidRmiProxyFactoryBean.getRandomBalanceMap().get(key).addProcessor(value);
							} else {
								RandomLoadBalancer<String> rrlb = new RandomLoadBalancer<String>();
								rrlb.addProcessor(value);
								BidRmiProxyFactoryBean.getRandomBalanceMap().put(key, rrlb);
							}
 						}						
					}
					
				}
			}	
		}
	}

	 
 
}

