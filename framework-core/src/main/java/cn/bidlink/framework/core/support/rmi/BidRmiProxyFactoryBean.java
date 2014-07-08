package cn.bidlink.framework.core.support.rmi;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.remoting.support.RemoteInvocationFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ReflectionUtils;

import cn.bidlink.framework.core.exceptions.InnerErrorException;
import cn.bidlink.framework.core.loadbalance.BalanceType;
import cn.bidlink.framework.core.loadbalance.ConsistHashLoadBalancer;
import cn.bidlink.framework.core.loadbalance.RandomLoadBalancer;
import cn.bidlink.framework.core.loadbalance.RoundRobinLoadBalancer;
import cn.bidlink.framework.core.loadbalance.WeightedRandomLoadBalancer;
import cn.bidlink.framework.core.loadbalance.WeightedRoundRobinLoadBalancer;
import cn.bidlink.framework.core.support.BalanceInfo;
import cn.bidlink.framework.core.support.RemoteBalance;
import cn.bidlink.framework.core.utils.NetUtils;

/**
 * @description: RMIProxyFactoryBean 扩展
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-12-1 下午10:18:27
 * 
 */
public class BidRmiProxyFactoryBean extends RmiProxyFactoryBean {

	private static Logger logger = Logger
			.getLogger(BidRmiProxyFactoryBean.class);

	private RmiServiceConfig rmiServiceConfig;

	private Class<?> interfaces;

	/**
	 * 缓存配置 key = groupKey
	 * value = Class
	 */
	private static ConcurrentHashMap<String, List<Class<?>>> cacheAllInterfaceMap= new ConcurrentHashMap<String, List<Class<?>>>();

	/**
	 * key = groupKey+"_"+Class<?> value = BalanceType
	 */
	private static ConcurrentHashMap<String, BalanceType> classBalanceTypeMap = new ConcurrentHashMap<String, BalanceType>();

 
	/**
	 * groupKey+":"+host+"_"+port
	 */
	private static ConcurrentHashMap<String,StubQueue> cacheErrorStub = new ConcurrentHashMap<String,StubQueue>();
	
	/**
	 * 缓存所有主机stub
	 * key = groupKey+":"+host+"_"+port 
	 * value = Map<String, RmiStub> = {key = ClassType,value=RmiStub}
	 */
	private static ConcurrentHashMap<String,Map<Class<?>, RmiStub>> cacheAllStubMap = new ConcurrentHashMap<String, Map<Class<?>,RmiStub>>(); 
	

	/**
	 * key = groupKey
	 * value = host+"_"+port
	 */
	static ConcurrentHashMap<String, WeightedRoundRobinLoadBalancer<String>> weightRoundRoubinBalanceMap = new ConcurrentHashMap<String, WeightedRoundRobinLoadBalancer<String>>();
	
	/**
	 * key = groupKey
	 * value = host+"_"+port
	 */
	static ConcurrentHashMap<String, WeightedRandomLoadBalancer<String>> weightRandomRoubinBalanceMap = new ConcurrentHashMap<String, WeightedRandomLoadBalancer<String>>();
	
	/**
	 * key = groupKey
	 * value = host+"_"+port
	 */
	static ConcurrentHashMap<String, RoundRobinLoadBalancer<String>> roundrobinBalanceMap = new ConcurrentHashMap<String,RoundRobinLoadBalancer<String>>();
	
	/**
	 * key = groupKey
	 * value = host+"_"+port
	 */
	static ConcurrentHashMap<String, ConsistHashLoadBalancer<String>> hashBalanceMap = new ConcurrentHashMap<String, ConsistHashLoadBalancer<String>>();
	
	/**
	 * key = groupKey
	 * value = host+"_"+port
	 */
	static ConcurrentHashMap<String, RandomLoadBalancer<String>> randomBalanceMap = new ConcurrentHashMap<String, RandomLoadBalancer<String>>();
	
 
	/**
	 * 是否已经启动了检查
	 */
	private static boolean isStartedCheck = false;

	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	private final Lock rLock = lock.readLock();

	private final Lock wLock = lock.writeLock();

	private ReentrantLock recoverLock = new ReentrantLock(true);

	public BidRmiProxyFactoryBean(String groupKey,
			RmiServiceConfig rmiServiceConfig, Class<?> interfaces) {
		this.rmiServiceConfig = rmiServiceConfig;
		this.interfaces = interfaces;
		rmiServiceConfig.setGroupKey(groupKey);
		
		if (cacheAllInterfaceMap.get(groupKey) != null) {
			cacheAllInterfaceMap.get(groupKey).add(interfaces);
		} else {
			List<Class<?>> lists = new ArrayList<Class<?>>();
			lists.add(interfaces);
			cacheAllInterfaceMap.put(groupKey, lists);
		}
 	}
	
	 
	public BidRmiProxyFactoryBean setSuperRemoteInvocationFactory(RemoteInvocationFactory remoteInvocationFactory) {
		 setRemoteInvocationFactory(remoteInvocationFactory);
		 return this;
	}
	

	
	public static int count = 0;

	/**
	 * 针对每个对象只初始化一次
	 */
	public boolean isInited = false;

	public BidRmiProxyFactoryBean afterPropertiesSetLater() {
		this.setServiceInterface(interfaces);
		this.setServiceUrl(getRemoteUrl(rmiServiceConfig, interfaces));
		this.setCacheStub(rmiServiceConfig.isCacheStub());
		this.setLookupStubOnStartup(rmiServiceConfig.isLookupStubOnStartup());
		this.setRefreshStubOnConnectFailure(true);
		if (rmiServiceConfig.isEnableSsl()) {
	    	System.setProperty("javax.rmi.ssl.client.enabledCipherSuites","SSL_DH_anon_WITH_RC4_128_MD5");
	 		System.setProperty("javax.rmi.ssl.client.enabledProtocols", "TLSv1");
		}
		super.afterPropertiesSet();
		// RMI 失效查检
		checkRemote();
		return this;
	}
	
	/**
	 * 
	 * @description  根据主机端口清出缓存
	 * @param hostPort host+"_"+port
	 * @param cacheAllStubMap
	 */
	private void clearStubByHost(String hostPort,ConcurrentHashMap<String,Map<Class<?>, RmiStub>> cacheAllStubMap) {
		 if (!cacheAllStubMap.isEmpty()) {
			 try {
				 wLock.lock();
				 Set<String> setKeys = cacheAllStubMap.keySet();
				 for (String key : setKeys) {
					 String hostPortInfo = StringUtils.substringAfter(key,":");
                     if (hostPort.equals(hostPortInfo)) {
                    	 cacheAllStubMap.remove(key);
                     }
				}  
			 }finally {
				 wLock.unlock();
			 }
		 }
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Class<?> classType = invocation.getMethod().getDeclaringClass();
		String groupKey = rmiServiceConfig.getGroupKey();
		String key = groupKey + "_" + classType.getName();
		RemoteBalance remoteBalance = rmiServiceConfig.getRemoteBalance();
		Remote stubCur = null;
		RmiStub curRmiStub = null;
		try {
			if (remoteBalance != null) {
				if (remoteBalance.isEnableSsl()) {
			    	System.setProperty("javax.rmi.ssl.client.enabledCipherSuites","SSL_DH_anon_WITH_RC4_128_MD5");
			 		System.setProperty("javax.rmi.ssl.client.enabledProtocols", "TLSv1");
				}
				if (!isInited) {
					initStub(groupKey, remoteBalance, classType);
					classBalanceTypeMap.put(key, remoteBalance.getBalanceType());
					isInited = true;
					logger.info("初始化远程接口:" + classType.getName() + " 成功!");
				}

				 
				BalanceType bt = classBalanceTypeMap.get(key);
				String host_port = null;
				if (BalanceType.WEIGTHED_ROUNDROBIN == bt) {
					 host_port = weightRoundRoubinBalanceMap.get(groupKey).select();
				} else if (BalanceType.WEIGTHED_RANDOM == bt) {
					 host_port = weightRandomRoubinBalanceMap.get(groupKey).select();
				} else if (BalanceType.ROUNDROBIN == bt) {
					 host_port = roundrobinBalanceMap.get(groupKey).select();
				} else if (BalanceType.HASH == bt) {
					 host_port = hashBalanceMap.get(groupKey).select();
				} else if (BalanceType.RANDOM == bt) {
					 host_port = randomBalanceMap.get(groupKey).select();
				}
		 
				
				Map<Class<?>, RmiStub> rmiStubMap = cacheAllStubMap.get(groupKey+":"+host_port);
				if (rmiStubMap == null || rmiStubMap.isEmpty()) {
					throw new InnerErrorException("RMI系统内部异常！请等待30秒，如果还未恢复，请与管理员联系!");
				}
				curRmiStub = rmiStubMap.get(classType);
				if (curRmiStub == null) {
					throw new InnerErrorException("RMI系统内部异常！请等待30秒，如果还未恢复，请与管理员联系!");
				}
				
				if (StubStatus.ACTIVITE != curRmiStub.getStubStatus()) {
					throw new RemoteConnectFailureException("RMI系统内部异常！请等待30秒，如果还未恢复，请与管理员联系!",null);
				}
				if(rmiServiceConfig.isEnableAuth()) {
					SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL); 
					SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(curRmiStub.getUsername(),curRmiStub.getPassword()));
				}
 
				
//				logger.info(curRmiStub.getHost()+"_"+curRmiStub.getPort()+"_"+classType.getName());
				stubCur = curRmiStub.getStub();
			} else {
				stubCur = getStub();
			}
 			
			return doInvoke(invocation, stubCur);
		} catch (RemoteConnectFailureException ex) {
 			logger.error(ex.getMessage());
			return handleRemoteConnectFailure(groupKey, classType, curRmiStub,invocation, ex);
		} catch (RemoteException ex) {
 			logger.error(ex.getMessage());
			if (isConnectFailure(ex)) {
				return handleRemoteConnectFailure(groupKey, classType,curRmiStub, invocation, ex);
			} else {
				throw ex;
			}
		} catch (InnerErrorException ex) {
			logger.error(ex.getMessage(),ex);
			initAllStubByGroupKey(groupKey, remoteBalance);
 			throw ex;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}

	/**
	 * 
	 * @description  初始化该groupKey下 所有stub，
	 * @param groupKey
	 * @param remoteBalance
	 */
	public void initAllStubByGroupKey(String groupKey,RemoteBalance remoteBalance) {
		if(remoteBalance != null) {
			List<BalanceInfo> lists = remoteBalance.getBalanceInfos();
			for (BalanceInfo balanceInfo : lists) {
				String key = groupKey+":"+balanceInfo.getHost()+"_"+balanceInfo.getPort();
				StubQueue sq = new StubQueue(System.currentTimeMillis(),groupKey,balanceInfo.getHost(),balanceInfo.getPort(),balanceInfo.getBasePath());
				sq.setWeight(balanceInfo.getWeight());
				sq.setBalanceType(remoteBalance.getBalanceType());
				cacheErrorStub.put(key, sq);
			}
		}
 
	}
	
	/**
	 * @description 失败处理
	 * @param invocation
	 * @param ex
	 * @return
	 */
	protected Object handleRemoteConnectFailure(String groupKey,
			Class<?> classType, RmiStub curRmiStub,
			MethodInvocation invocation, Throwable ex) {
		Object objRes = null;
		if (curRmiStub != null) {
			try {
				// 重新初始化新的
				List<String> processors = new ArrayList<String>();
				List<Integer> radios = new ArrayList<Integer>();
                String hostPort = curRmiStub.getHost()+"_"+curRmiStub.getPort();
				clearStubByHost(hostPort, cacheAllStubMap);

				if (BalanceType.WEIGTHED_ROUNDROBIN == curRmiStub.getBalanceType()) {
					processors = weightRoundRoubinBalanceMap.get(groupKey).removeProcessor(hostPort).getProcessors();
					weightRoundRoubinBalanceMap.get(groupKey).getWeights().remove(Integer.valueOf(curRmiStub.getWeight()));
					radios = weightRoundRoubinBalanceMap.get(groupKey).getWeights();
					// 将原值 覆盖
					weightRoundRoubinBalanceMap.put(groupKey,new WeightedRoundRobinLoadBalancer<String>(processors, radios));
					
				} else if (BalanceType.WEIGTHED_RANDOM == curRmiStub.getBalanceType()) {
					processors = weightRandomRoubinBalanceMap.get(groupKey).removeProcessor(hostPort).getProcessors();
					weightRandomRoubinBalanceMap.get(groupKey).getWeights().remove(Integer.valueOf(curRmiStub.getWeight()));
					radios = weightRandomRoubinBalanceMap.get(groupKey).getWeights();
					// 将原值 覆盖
					weightRandomRoubinBalanceMap.put(groupKey,new WeightedRandomLoadBalancer<String>(processors, radios));
				} else if (BalanceType.ROUNDROBIN == curRmiStub.getBalanceType()) {
					processors = roundrobinBalanceMap.get(groupKey).removeProcessor(hostPort).getProcessors();
				} else if (BalanceType.HASH == curRmiStub.getBalanceType()) {
					processors = hashBalanceMap.get(groupKey).removeProcessor(hostPort).getProcessors();
				} else if (BalanceType.RANDOM == curRmiStub.getBalanceType()) {
					processors = randomBalanceMap.get(groupKey).removeProcessor(hostPort).getProcessors();
				}

				if (processors != null && processors.size() > 0) {
					String failToNewHost = processors.get(0); 
					RmiStub rmiStubnew = cacheAllStubMap.get(groupKey+":"+failToNewHost).get(classType);
					if (rmiStubnew != null) {
						objRes = super.doInvoke(invocation, rmiStubnew.getStub());
					} else {
						throw new InnerErrorException("缓存不存在【"+groupKey+":"+failToNewHost+"】stub信息!");
					}	
				}
			} catch (Throwable e) {
				// 如果所有服务都停止时会出现该异常
				logger.error(e.getMessage(), e);
				throw new InnerErrorException("RMI系统内部异常！请等待30秒，如果还未恢复，请与管理员联系!");
			} finally {
				String key = groupKey+":"+curRmiStub.getHost()+"_"+curRmiStub.getPort();
				StubQueue sq = new StubQueue(System.currentTimeMillis(),groupKey ,curRmiStub.getHost(),curRmiStub.getPort(),curRmiStub.getBasePath());
				sq.setBalanceType(curRmiStub.getBalanceType());
				sq.setWeight(curRmiStub.getWeight());
				cacheErrorStub.put(key,sq);
			}
			
		} else {
			//不存在负载的情况,使用spring原始异常处理
			Method errorMethod = ReflectionUtils.findMethod(RmiProxyFactoryBean.class, "handleRemoteConnectFailure",new Class[] { MethodInvocation.class, Exception.class });
			errorMethod.setAccessible(true);
			objRes = ReflectionUtils.invokeMethod(errorMethod, this,new Object[] { invocation, ex });
		}

		return objRes;
	}

	/**
	 * 
	 * @description 初始化远程信息并且缓存
	 * @param remoteBalance
	 * @param classType
	 */
	private void initStub(String groupKey, RemoteBalance remoteBalance,Class<?> classType) {
		rLock.lock();
		try {
 			if (remoteBalance != null && remoteBalance.getBalanceInfos() != null) {
				List<BalanceInfo> listBis = remoteBalance.getBalanceInfos();
				List<String> processors = new CopyOnWriteArrayList<String>();
				List<Integer> radios = new CopyOnWriteArrayList<Integer>();
				initRemoteStub(groupKey,listBis, remoteBalance.getBalanceType(), classType,processors, radios);
			}
		} finally {
			rLock.unlock();
		}
	}

	/**
	 * 
	 * @description 初始化远程信息
	 * @param listBis
	 * @param balanceType
	 * @param classType
	 * @param lists
	 *            rmi列表
	 * @param radios
	 *            权重
	 */
	public void initRemoteStub(String groupKey,List<BalanceInfo> listBis,BalanceType balanceType, Class<?> classType, List<String> lists,
			List<Integer> radios) {
			for (BalanceInfo bi : listBis) {
				RmiStub rs = new RmiStub();
				String key =  groupKey+":"+bi.getHost()+"_"+bi.getPort();
				try {
					rs.setBalanceType(balanceType);
					BeanUtils.copyProperties(bi, rs);
					this.setServiceUrl(getRemoteUrl(bi, classType));
					Remote stub = lookupStub();
					rs.setStub(stub);
					rs.setStubStatus(StubStatus.ACTIVITE);
				} catch (Exception e) {
					rs.setStubStatus(StubStatus.DEAD);
					logger.error(e.getMessage(), e);
				}
				
				if (!lists.contains(key)) {
					lists.add(key);
					if (radios != null) {
						radios.add(bi.getWeight());
					}
				}
				if(cacheAllStubMap.get(key) != null) {
					cacheAllStubMap.get(key).put(classType, rs);
				} else {
					Map<Class<?>, RmiStub> map = new HashMap<Class<?>, RmiStub>();
					map.put(classType, rs);
					cacheAllStubMap.put(key, map);
				}
			}
	}

	/**
	 * @description 取得远程URL
	 * @param rsc
	 *            配置文件
	 * @param fieldType
	 *            接口
	 * @return String DOM对象
	 * @throws
	 */
	private String getRemoteUrl(RmiServiceConfig rsc, Class<?> fieldType) {
		StringBuffer buf = new StringBuffer();
		buf.append("rmi://").append(rsc.getRemoteHost()).append(":")
				.append(rsc.getRemotePort()).append("/");
		if (StringUtils.isNotEmpty(rsc.getRemoteContextPath())) {
			buf.append(rsc.getRemoteContextPath()).append("/");
		}
		buf.append(fieldType.getSimpleName());
		return buf.toString();
	}

	private String getRemoteUrl(BalanceInfo bi, Class<?> fieldType) {
		StringBuffer buf = new StringBuffer();
		buf.append("rmi://").append(bi.getHost()).append(":")
				.append(bi.getPort()).append("/");
		if (StringUtils.isNotEmpty(bi.getBasePath())) {
			buf.append(bi.getBasePath()).append("/");
		}
		buf.append(fieldType.getSimpleName());
		return buf.toString();
	}

	private String getRemoteUrl(String host,int port, String basePath,Class<?> fieldType) {
		StringBuffer buf = new StringBuffer();
		buf.append("rmi://").append(host).append(":")
				.append(port).append("/");
		if (StringUtils.isNotEmpty(basePath)) {
			buf.append(basePath).append("/");
		}
		buf.append(fieldType.getSimpleName());
		return buf.toString();
	}

	/**
	 * 
	 * @description 失效检查
	 */
	public void checkRemote() {
		if (isStartedCheck) {
			return;
		}
		isStartedCheck = true;
//	   logger.info("====================================线程启动=============================================");
		Thread t = new Thread() {
			public void run() {
				while (true) {
					try {
						TimeUnit.MILLISECONDS.sleep(2000);
					    if (cacheErrorStub.isEmpty()) {
					    	continue;
					    }
 					    recoverLock.lock();
						try {
							logger.debug("cacheErrorStub.size()====="+cacheErrorStub.size());
							for(Iterator<Map.Entry<String, StubQueue>> it = cacheErrorStub.entrySet().iterator();it.hasNext();) {
								 Map.Entry<String, StubQueue> entry = it.next();
//								 String groupKeyHostPort = entry.getKey();
								 StubQueue sq = entry.getValue();
								 String groupKey = sq.getGroupKey();
								 String hostPort = sq.getHost()+"_"+sq.getPort();
								 List<Class<?>> interfaces = cacheAllInterfaceMap.get(groupKey);
 
								 boolean flag = NetUtils.telnetStringPort(sq.getHost(),String.valueOf(sq.getPort()), 5000);
								 logger.info("isConnected:" + flag);
 								if (flag) {
									String key = groupKey+":"+sq.getHost()+"_"+sq.getPort();
									Map<Class<?>, RmiStub> stubMap = new HashMap<Class<?>, RmiStub>();
									
									for (Class<?> interfaceType : interfaces) {
 										String serviceUrl = getRemoteUrl(sq.getHost(), sq.getPort(), sq.getBasePath(), interfaceType);
 										Remote stub = null;
 										try {
 											BidRmiProxyFactoryBean.this.setServiceUrl(serviceUrl);
 											stub = BidRmiProxyFactoryBean.this.lookupStub();
 										}catch (Exception e) {
 											TimeUnit.MILLISECONDS.sleep(500);
 											stub = BidRmiProxyFactoryBean.this.lookupStub();
										}
 
 										RmiStub rmiStub = new RmiStub();
 										rmiStub.setStub(stub);
 										rmiStub.setStubStatus(StubStatus.ACTIVITE);
 										rmiStub.setBalanceType(sq.getBalanceType());
 										rmiStub.setWeight(sq.getWeight());
 										rmiStub.setHost(sq.getHost());
 										rmiStub.setPort(sq.getPort());
 										rmiStub.setBasePath(sq.getBasePath());
 										stubMap.put(interfaceType, rmiStub);
									}
									cacheAllStubMap.put(key, stubMap);
	
									// 重新初始化新的
									List<String> processors = new ArrayList<String>();
									List<Integer> radios = new ArrayList<Integer>();
									
									if (BalanceType.WEIGTHED_ROUNDROBIN == sq.getBalanceType()) {
										processors = weightRoundRoubinBalanceMap.get(groupKey).addProcessor(hostPort).getProcessors();
										weightRoundRoubinBalanceMap.get(groupKey).getWeights().add(sq.getWeight());
										radios = weightRoundRoubinBalanceMap.get(groupKey).getWeights();
										// 将原值 覆盖
										weightRoundRoubinBalanceMap.put(groupKey,new WeightedRoundRobinLoadBalancer<String>(processors, radios));
										
									} else if (BalanceType.WEIGTHED_RANDOM == sq.getBalanceType()) {
										processors = weightRandomRoubinBalanceMap.get(groupKey).addProcessor(hostPort).getProcessors();
										weightRandomRoubinBalanceMap.get(groupKey).getWeights().add(sq.getWeight());
										radios = weightRandomRoubinBalanceMap.get(groupKey).getWeights();
										// 将原值 覆盖
										weightRandomRoubinBalanceMap.put(groupKey,new WeightedRandomLoadBalancer<String>(processors, radios));
										
									} else if (BalanceType.ROUNDROBIN == sq.getBalanceType()) {
										processors = roundrobinBalanceMap.get(groupKey).addProcessor(hostPort).getProcessors();
									} else if (BalanceType.HASH == sq.getBalanceType()) {
										processors = hashBalanceMap.get(groupKey).addProcessor(hostPort).getProcessors();
									} else if (BalanceType.RANDOM == sq.getBalanceType()) {
										processors = randomBalanceMap.get(groupKey).addProcessor(hostPort).getProcessors();
									}
									
									cacheErrorStub.remove(key);
								}
							}
						}finally {
							recoverLock.unlock();
						}
					} catch (InterruptedException e) {
						logger.error(e.getMessage(), e);
					} 
				}
			}
		};

		t.setDaemon(true);
		t.start();
//	   logger.info("======================================线程启动完成============================================");
	}


	public static ConcurrentHashMap<String, WeightedRoundRobinLoadBalancer<String>> getWeightRoundRoubinBalanceMap() {
		return weightRoundRoubinBalanceMap;
	}


	public static void setWeightRoundRoubinBalanceMap(
			ConcurrentHashMap<String, WeightedRoundRobinLoadBalancer<String>> weightRoundRoubinBalanceMap) {
		BidRmiProxyFactoryBean.weightRoundRoubinBalanceMap = weightRoundRoubinBalanceMap;
	}


	public static ConcurrentHashMap<String, WeightedRandomLoadBalancer<String>> getWeightRandomRoubinBalanceMap() {
		return weightRandomRoubinBalanceMap;
	}


	public static void setWeightRandomRoubinBalanceMap(
			ConcurrentHashMap<String, WeightedRandomLoadBalancer<String>> weightRandomRoubinBalanceMap) {
		BidRmiProxyFactoryBean.weightRandomRoubinBalanceMap = weightRandomRoubinBalanceMap;
	}


	public static ConcurrentHashMap<String, RoundRobinLoadBalancer<String>> getRoundrobinBalanceMap() {
		return roundrobinBalanceMap;
	}


	public static void setRoundrobinBalanceMap(
			ConcurrentHashMap<String, RoundRobinLoadBalancer<String>> roundrobinBalanceMap) {
		BidRmiProxyFactoryBean.roundrobinBalanceMap = roundrobinBalanceMap;
	}


	public static ConcurrentHashMap<String, ConsistHashLoadBalancer<String>> getHashBalanceMap() {
		return hashBalanceMap;
	}


	public static void setHashBalanceMap(
			ConcurrentHashMap<String, ConsistHashLoadBalancer<String>> hashBalanceMap) {
		BidRmiProxyFactoryBean.hashBalanceMap = hashBalanceMap;
	}


	public static ConcurrentHashMap<String, RandomLoadBalancer<String>> getRandomBalanceMap() {
		return randomBalanceMap;
	}


	public static void setRandomBalanceMap(
			ConcurrentHashMap<String, RandomLoadBalancer<String>> randomBalanceMap) {
		BidRmiProxyFactoryBean.randomBalanceMap = randomBalanceMap;
	}


	public static ConcurrentHashMap<String, List<Class<?>>> getCacheAllInterfaceMap() {
		return cacheAllInterfaceMap;
	}


	public static ConcurrentHashMap<String, StubQueue> getCacheErrorStub() {
		return cacheErrorStub;
	}


	public static ConcurrentHashMap<String, Map<Class<?>, RmiStub>> getCacheAllStubMap() {
		return cacheAllStubMap;
	}


    
}
