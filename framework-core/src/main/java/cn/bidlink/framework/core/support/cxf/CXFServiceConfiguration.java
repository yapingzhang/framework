package cn.bidlink.framework.core.support.cxf;

import java.lang.reflect.Method;

import javax.xml.namespace.QName;

import org.apache.cxf.service.factory.DefaultServiceConfiguration;
import org.apache.cxf.service.factory.ServiceConstructionException;
import org.apache.cxf.service.model.OperationInfo;

/**
 * used to adjust the xfire
 * @author wubin
 * Jan 5, 2012
 */
public class CXFServiceConfiguration extends DefaultServiceConfiguration{
	private String namespace;
	
	public CXFServiceConfiguration(String namespace) {
		this.setNamespace(namespace);
	}

	@Override
	public QName getOutParameterName(OperationInfo op, Method method,
			int paramNumber) {
		return new QName(op.getName().getNamespaceURI(), 
                getDefaultLocalName(op, method, paramNumber, "out"));
		
	}
	
	private String getDefaultLocalName(OperationInfo op, Method method, int paramNumber, String prefix) {
        Class<?> impl = getServiceFactory().getServiceClass();
        // try to grab the implementation class so we can read the debug symbols from it
        if (impl != null) {
            try {
                method = impl.getMethod(method.getName(), method.getParameterTypes());
            } catch (Exception e) {
                throw new ServiceConstructionException(e);
            }
        }
        
        return DefaultServiceConfiguration.createName(method, paramNumber, op.getInput()
            .getMessageParts().size(), false, prefix);
    }

	@Override
	public String getServiceNamespace() {
		return this.getNamespace();
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	
	
	
}
