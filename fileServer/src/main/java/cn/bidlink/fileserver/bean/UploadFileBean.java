package cn.bidlink.fileserver.bean;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Description 上传文件bean,该类主要用于获取处理后的上传文件
 */
public class UploadFileBean {
	//规则表单域对象
	public Object form;
    //附件
	public Set<Accessory> accessorys = new HashSet<Accessory>();
	//不规则表单域 键值对
	private Map<String,Object> mapOtherForm = null;

	public Object getForm() {
		return form;
	}

	public void setForm(Object form) {
		this.form = form;
	}

    
	public Set<Accessory> getAccessorys() {
		return accessorys;
	}

	public void setAccessorys(Set<Accessory> accessorys) {
		this.accessorys = accessorys;
	}

	public Map<String, Object> getMapOtherForm() {
		return mapOtherForm;
	}

	public void setMapOtherForm(Map<String, Object> mapOtherForm) {
		this.mapOtherForm = mapOtherForm;
	}

	
	
}
