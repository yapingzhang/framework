package cn.bidlink.fileserver.util;

/**
 * 返回状态集
 * 
 * @author changzhiyuan
 * @date 2013-02-04
 * 
 */
public enum State {
	SUCCESS(0), NOPRMIN(1), EXCEEDLIMIT(2),SYSERR(3),NULL(4);
	private State(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	private int state;
}
