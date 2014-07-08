package cn.bidlink.fileserver.util;

/**
 * 
 * @author changzhiyuan
 * @date 2012-02-05
 * 
 */
public interface Constants {
	// 上传文件最大100m
	int FILE_MAX_SIZE = 100 * 1024 * 1024;
	// 内存缓冲上传文件大小 默认100k
	int CACHE_THRESHOLD_SIZE = 100 * 1024;
	//redis中存放token的前缀
	String TOKEN_PREFIX="fileserver_token_";

}
