1.framework-test 介绍:
  framework-test 整合了一下测试工具，使单元测试更加简单。
2.framework-test pom:
	<dependency>
	  <groupId>cn.bidlink.framework</groupId>
	  <artifactId>framework-test</artifactId>
	  <version>0.0.2-SNAPSHOT</version>
	</dependency>
3.framework-test 例子:
  cn.bidlink.framework.test.dao 下为Dao的单元测试例子，基于Spring，使用mysql数据库(与开发环境相同，但不要是同一个)，
  cn.bidlink.framework.test.service 下为Service的单元测试例子，未使用spring
  cn.bidlink.framework.test.action 下为Action的单元测试例子，未使用spring
  cn.bidlink.framework.test 下的StaticClassTest，其他情况的使用方法