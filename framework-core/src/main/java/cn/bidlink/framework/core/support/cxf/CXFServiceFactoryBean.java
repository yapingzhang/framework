package cn.bidlink.framework.core.support.cxf;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.apache.cxf.common.WSDLConstants;
import org.apache.cxf.common.i18n.Message;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.common.xmlschema.SchemaCollection;
import org.apache.cxf.service.factory.ReflectionServiceFactoryBean;
import org.apache.cxf.service.factory.ServiceConstructionException;
import org.apache.cxf.service.model.MessageInfo;
import org.apache.cxf.service.model.MessagePartInfo;
import org.apache.cxf.service.model.OperationInfo;
import org.apache.cxf.service.model.SchemaInfo;
import org.apache.cxf.service.model.ServiceInfo;
import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaExternal;
import org.apache.ws.commons.schema.XmlSchemaForm;
import org.apache.ws.commons.schema.XmlSchemaImport;
import org.apache.ws.commons.schema.XmlSchemaType;
import org.apache.ws.commons.schema.utils.NamespaceMap;

public class CXFServiceFactoryBean extends ReflectionServiceFactoryBean{
	
	public CXFServiceFactoryBean(String namespace) {
		super();
		getServiceConfigurations().add(0, new CXFServiceConfiguration(namespace));
	}


	private static final Logger LOG = LogUtils.getL7dLogger(CXFServiceFactoryBean.class);
	
	private void addImport(XmlSchema schema, String ns) {
        if (!ns.equals(schema.getTargetNamespace())
            && !ns.equals(WSDLConstants.NS_SCHEMA_XSD)
            && !isExistImport(schema, ns)) {
            XmlSchemaImport is = new XmlSchemaImport(schema);
            is.setNamespace(ns);
            if (this.schemaLocationMapping.get(ns) != null) {
                is.setSchemaLocation(this.schemaLocationMapping.get(ns));
            }
            if (!schema.getItems().contains(is)) {
                schema.getItems().add(is);
            }
        }
    }
	
	private boolean isExistImport(XmlSchema schema, String ns) {
        boolean isExist = false;

        for (XmlSchemaExternal ext : schema.getExternals()) {
            if (ext instanceof XmlSchemaImport) {
                XmlSchemaImport xsImport = (XmlSchemaImport)ext;
                if (xsImport.getNamespace().equals(ns)) {
                    isExist = true;
                    break;
                }
            }
        }
        return isExist;

    }
	
	private SchemaInfo getOrCreateSchema(ServiceInfo serviceInfo, String namespaceURI, boolean qualified) {
        for (SchemaInfo s : serviceInfo.getSchemas()) {
            if (s.getNamespaceURI().equals(namespaceURI)) {
                return s;
            }
        }

        SchemaInfo schemaInfo = new SchemaInfo(namespaceURI);
        SchemaCollection col = serviceInfo.getXmlSchemaCollection();
        XmlSchema schema = col.getSchemaByTargetNamespace(namespaceURI);

        if (schema != null) {
            schemaInfo.setSchema(schema);
            serviceInfo.addSchema(schemaInfo);
            return schemaInfo;
        }

        schema = col.newXmlSchemaInCollection(namespaceURI);
        if (qualified) {
            schema.setElementFormDefault(XmlSchemaForm.QUALIFIED);
        }
        schemaInfo.setSchema(schema);

        Map<String, String> explicitNamespaceMappings = this.getDataBinding().getDeclaredNamespaceMappings();
        if (explicitNamespaceMappings == null) {
            explicitNamespaceMappings = Collections.emptyMap();
        }
        NamespaceMap nsMap = new NamespaceMap();
        for (Map.Entry<String, String> mapping : explicitNamespaceMappings.entrySet()) {
            nsMap.add(mapping.getValue(), mapping.getKey());
        }

        if (!explicitNamespaceMappings.containsKey(WSDLConstants.NS_SCHEMA_XSD)) {
            nsMap.add(WSDLConstants.NP_SCHEMA_XSD, WSDLConstants.NS_SCHEMA_XSD);
        }
        if (!explicitNamespaceMappings.containsKey(serviceInfo.getTargetNamespace())) {
            nsMap.add(WSDLConstants.CONVENTIONAL_TNS_PREFIX, serviceInfo.getTargetNamespace());
        }
        schema.setNamespaceContext(nsMap);
        serviceInfo.addSchema(schemaInfo);
        return schemaInfo;
    }
	
	
	@Override
	protected void createBareMessage(ServiceInfo serviceInfo,
			OperationInfo opInfo, boolean isOut) {
		
		//super.createBareMessage(serviceInfo, opInfo, isOut);
		MessageInfo message = isOut ? opInfo.getOutput() : opInfo.getInput();

        if (message.getMessageParts().size() == 0) {
            return;
        }

        Method method = (Method)opInfo.getProperty(METHOD);
        int paraNumber = 0;
        for (MessagePartInfo mpi : message.getMessageParts()) {
            SchemaInfo schemaInfo = null;
            XmlSchema schema = null;

            QName qname = (QName)mpi.getProperty(ELEMENT_NAME);
            if (message.getMessageParts().size() == 1 && qname == null) {
                qname = !isOut ? getInParameterName(opInfo, method, -1) 
                        : getOutParameterName(opInfo, method, -1);
                
                if (qname.getLocalPart().startsWith("arg") || qname.getLocalPart().startsWith("return")) {
                    qname = isOut
                        ? new QName(qname.getNamespaceURI(), method.getName() + "Response") : new QName(qname
                            .getNamespaceURI(), method.getName());
                }
            } else if (isOut && message.getMessageParts().size() > 1 && qname == null) {
                while (!isOutParam(method, paraNumber)) {
                    paraNumber++;
                }
                qname = getOutParameterName(opInfo, method, paraNumber);
            } else if (qname == null) {
                qname = getInParameterName(opInfo, method, paraNumber);
            }

            for (SchemaInfo s : serviceInfo.getSchemas()) {
                if (s.getNamespaceURI().equals(qname.getNamespaceURI())) {
                    schemaInfo = s;
                    break;
                }
            }

            if (schemaInfo == null) {
                schemaInfo = getOrCreateSchema(serviceInfo, qname.getNamespaceURI(), true);
                schema = schemaInfo.getSchema();
            } else {
                schema = schemaInfo.getSchema();
                if (schema != null && schema.getElementByName(qname) != null) {
                    mpi.setElement(true);
                    mpi.setElementQName(qname);
                    mpi.setXmlSchema(schema.getElementByName(qname));
                    paraNumber++;
                    continue;
                }
            }

            schemaInfo.setElement(null); //cached element is now invalid
            XmlSchemaElement el = new XmlSchemaElement(schema, true);
            el.setName(qname.getLocalPart());
            el.setNillable(true);

            if (mpi.isElement()) {
                XmlSchemaElement oldEl = (XmlSchemaElement)mpi.getXmlSchema();
                if (null != oldEl && !oldEl.getQName().equals(qname)) {
                    el.setSchemaTypeName(oldEl.getSchemaTypeName());
                    el.setSchemaType(oldEl.getSchemaType());
                    if (oldEl.getSchemaTypeName() != null) {
                        addImport(schema, oldEl.getSchemaTypeName().getNamespaceURI());
                    }
                }
                mpi.setElement(true);
                mpi.setXmlSchema(el);
                mpi.setElementQName(qname);
                mpi.setConcreteName(qname);
                continue;
            } else {
                if (null == mpi.getTypeQName() && mpi.getXmlSchema() == null) {
                    throw new ServiceConstructionException(new Message("UNMAPPABLE_PORT_TYPE", LOG,
                                                                       method.getDeclaringClass().getName(),
                                                                       method.getName(),
                                                                       mpi.getName()));
                }
                if (mpi.getTypeQName() != null) {
                    el.setSchemaTypeName(mpi.getTypeQName());
                } else {
                    el.setSchemaType((XmlSchemaType)mpi.getXmlSchema());
                }
                mpi.setXmlSchema(el);
                mpi.setConcreteName(qname);
                if (mpi.getTypeQName() != null) {
                    addImport(schema, mpi.getTypeQName().getNamespaceURI());
                }
            }

            mpi.setElement(true);
            mpi.setElementQName(qname);
            paraNumber++;
        }
	}
	
}
