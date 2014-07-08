<%@ page import="cn.bidlink.fileserver.util.CryptoUtils"%>
<%
CryptoUtils cryto = new CryptoUtils();

String key = cryto.basicEncrypt(
				String.valueOf(System.currentTimeMillis()), null);
key = cryto.base64EncodeString(key.getBytes());
StringBuffer script = new StringBuffer();
script.append("<script>");
script.append("var cryptkey='").append(key.trim()).append("';");
script.append("</script>");
out.print(script.toString());
%>