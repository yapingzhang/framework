package cn.bidlink.framework.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.jar.JarFile;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import cn.bidlink.framework.core.exceptions.GeneralException;

public abstract class ResourceUtils extends org.springframework.util.ResourceUtils {
	
	public static List<Resource> getResourcesInClassLoader(ClassLoader classLoader, 
														   boolean isScanAncestor,
														   String pattern, 
														   PathMatcher pathMatcher) {
		if(!(classLoader instanceof URLClassLoader)) {
			if(isScanAncestor
			   && classLoader.getParent() != null 
			   && classLoader.getParent() instanceof URLClassLoader) {
				return getResourcesInClassLoader(
							classLoader.getParent(), isScanAncestor, pattern, pathMatcher);
			}
			return null;
		}
		URL[] urls = ((URLClassLoader)classLoader).getURLs();
		List<File> files = new ArrayList<File>();
		File file = null;
		String fileEncoding = System.getProperty("file.encoding");
		if(!StringUtils.hasLength(fileEncoding)) {
			fileEncoding = "UTF-8";
		}
		try {
			for (URL url : urls) {
				file = new File(URLDecoder.decode(url.getFile(), fileEncoding));
				files.add(file);
			}
		} catch (UnsupportedEncodingException exception) {
			throw new GeneralException(
					" the char encoding [ " + fileEncoding + "] is unsupported ", exception);
		}
		List<Resource> resources =  convertToResources(files, pattern, pathMatcher);
		if(isScanAncestor
		   && classLoader.getParent() != null 
		   && classLoader.getParent() instanceof URLClassLoader) {
			List<Resource> parentResources = 
				getResourcesInClassLoader(classLoader.getParent(), isScanAncestor, pattern, pathMatcher);
			if(!CollectionUtils.isEmpty(parentResources)) {
				resources.addAll(parentResources);
			}
		}
		return resources;
	}
	
	public static List<Resource> convertToResources(List<File> files, 
													String pattern, 
													PathMatcher pathMatcher) {
		List<Resource> resources = new ArrayList<Resource>();
		for (File file : files) {
			String fileName = file.getName();
			if(file.isFile()
			   && StringUtils.hasLength(pattern) 
			   && pathMatcher != null 
			   && !pathMatcher.match(pattern, fileName)) {
				continue;
			}
			String extension = fileName.lastIndexOf(".") != -1 
				? fileName.substring(fileName.lastIndexOf(".") + 1)
				: null;
			if (file.isFile() && StringUtils.hasText(extension) && "jar".equals(extension)) {
				try {
					new JarFile(file);
				} catch (Exception exception) {
					continue;
				}
				try {
					String filePath = file.getCanonicalPath().replaceAll("\\\\", "/");
					if(StringUtils.hasLength(filePath)) {
						if(!filePath.startsWith("/")) {
							filePath = "/"  + filePath;
						}
						resources.add(new UrlResource(new URL("jar:file:" + filePath + "!/")));
					}
				} catch (Exception exception) {
					throw new GeneralException(exception);
				}
			}  else {
				resources.add(new FileSystemResource(file));
			}
		}
		Collections.sort(resources, new ResourceComparator());
		return resources;
	}

	static class ResourceComparator implements Comparator<Resource>, Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public int compare(Resource o1, Resource o2) {
			try {
				if(o1 instanceof FileSystemResource 
				  || (o1 instanceof UrlResource 
						  && ((UrlResource)o1).getURL().getProtocol().equals("file"))) {
					return 1;
				}
			} catch (IOException exception) {
				throw new GeneralException("invalid url protocol : " + o1);
			}
			return 0;
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> List<Class<T>> loadClasses(ApplicationContext applicationContext, String pattern, Class<T> filterClass) {
		List<Class<T>> classes = new ArrayList<Class<T>>(0);
		try {
			Resource[] resources = applicationContext.getResources(pattern);
			if(ObjectUtils.isEmpty(resources)) {
				return classes;
			}
			MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(applicationContext);
			for (Resource resource : resources) {
				if (resource.isReadable()) {
					MetadataReader reader = readerFactory.getMetadataReader(resource);
					String className = reader.getClassMetadata().getClassName();
					Class<?> clazz = Class.forName(className);
					if (filterClass != null) {
						if(!filterClass.equals(clazz) && filterClass.isAssignableFrom(clazz)) {
							classes.add((Class<T>) clazz);
						}
					} else {
						classes.add((Class<T>) clazz);
					}
				}
			}
		} catch (Exception e) {
			throw new GeneralException("init BeanDefinitionScanner failed", e);
		}
		return classes;
	}
}
