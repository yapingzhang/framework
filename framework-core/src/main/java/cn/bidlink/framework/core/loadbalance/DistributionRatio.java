package cn.bidlink.framework.core.loadbalance;

/**
 * @remark 第三方源码
 * @author zhongfeng (mailto:djangofan@163.com)
 * @quoter dejian.liu 
 * @quoteDate 2012-12-01
 * @param <E>
 */
public class DistributionRatio {
    
	/**
     * 
     */
    private int processorPosition;
    
    /**
     * 
     */
    private int distributionWeight;
    
    /**
     * 
     */
    private int runtimeWeight;
    
    public DistributionRatio(int processorPosition, int distributionWeight) {
        this(processorPosition, distributionWeight, distributionWeight);
    }

    public DistributionRatio(int processorPosition, int distributionWeight, int runtimeWeight) {
        this.processorPosition = processorPosition;
        this.distributionWeight = distributionWeight;
        this.runtimeWeight = runtimeWeight;
    }
    
    public int getProcessorPosition() {
        return processorPosition;
    }

    public void setProcessorPosition(int processorPosition) {
        this.processorPosition = processorPosition;
    }

    public int getDistributionWeight() {
        return distributionWeight;
    }

    public void setDistributionWeight(int distributionWeight) {
        this.distributionWeight = distributionWeight;
    }

    public int getRuntimeWeight() {
        return runtimeWeight;
    }

    public void setRuntimeWeight(int runtimeWeight) {
        this.runtimeWeight = runtimeWeight;
    }
    
}
