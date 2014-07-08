1.framework-log4j
应用通过log4j输出日志到mongodb数据库中。  

2.framework-log4j说明：
1)cn.bidlink.framework.log4j.layout.ExtendedPatternLayout 扩展日志格式，%V表示进程号,%I标识IP,%H标识主机名。	
2)cn.bidlink.framework.log4j.varia.ExtendedFilter 扩展日志级别拦截器，拦截自己定义日志级别。
3)cn.bidlink.framework.log4j.appender.SynMongodExtendedAppender 扩展日志存储方式，使其支持Mongodb。
4)cn.bidlink.framework.log4j.level.ExtendedLevel 自定义日志级别，如需增加日志级别继承类扩展。
5)cn.bidlink.framework.log4j.utils.OperateMDC 一般需要收集的信息。
6)cn.bidlink.framework.log4j.utils.OperateType 日志的操作类型。
7)cn.bidlink.framework.log4j.mongod.MongodQuery mongodb查询时用的。
8)cn.bidlink.framework.log4j.mongod.MongodFactory mongodb工厂类。
9)cn.bidlink.framework.log4j.monitor.JvmMonitor cpu、内存使用信息。

3.log4j.xml配置
1)日志输出配置
<appender name="operateLog" class="org.apache.log4j.ConsoleAppender">
	<layout class="cn.bidlink.framework.log4j.layout.ExtendedPatternLayout">
		<param name="ConversionPattern" value="[%-3p] %C{1} h:%H - v:%V - ip:%I - %m%n" />
	</layout>
	<filter class="cn.bidlink.framework.log4j.varia.ExtendedFilter">
		<param name="LevelMax" value="20050" />
		<param name="LevelMin" value="20050" />
	</filter>
</appender>
2)日志存储配置
<appender name="operateAppender"
	class="cn.bidlink.framework.log4j.appender.SynMongodExtendedAppender">
	<!-- 是否开始cpu内存监控 -->
	<param name="jvmMonitor" value="true" /> 
	<!-- 数据库名称  -->
	<param name="databaseName" value="logmongod" />
	<!-- 数据库表名 -->
	<param name="collectionName" value="usetest" />
	<param name="hostnamePort" value="192.168.0.107:47017" />
	<!-- 数据库用户名 -->
	<param name="userName" value="root" />
	<!-- 数据库密码 -->
	<param name="password" value="root" />
	<!-- 应用标识 -->
	<param name="appCode" value="" />
	<!-- 需要拦截的日志级别 -->
	<filter class="cn.bidlink.framework.log4j.varia.ExtendedFilter">
		<param name="LevelMax" value="20050" />
		<param name="LevelMin" value="20050" />
	</filter>
</appender>
4.pom：
<dependency>
  <groupId>cn.bidlink.framework</groupId>
  <artifactId>framework-log4j</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
5.例子：
transient Logger logger = Logger.getLogger(getClass());
OperateMDC mdc = new OperateMDC();
mdc.setUserId("1111");
mdc.setCompanyId("1111");
logger.log(ExtendedLevel.OPERATE_LOG_LEVEL, "ExpansionLevelTest" + i);  