package cn.bidlink.framework.core.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * @remark 第三方源码
 * @author zhongfeng (mailto:djangofan@163.com)
 * @quoter dejian.liu 
 * @quoteDate 2012-12-01
 * @param <E>
 */
public class WeightedRandomLoadBalancer<E> extends WeightedLoadBalancer<E> {
    private final Random rnd = new Random();
    private final int distributionRatioSum;
    private int runtimeRatioSum;
    
    public WeightedRandomLoadBalancer(List<E> processors,List<Integer> distributionRatioList) {
        super(processors,distributionRatioList);
        int sum = 0;
        for (Integer distributionRatio : getDistributionRatioList()) {
            sum += distributionRatio;
        }
        distributionRatioSum = sum;
        runtimeRatioSum = distributionRatioSum;
    }

	@Override
	protected E doSelect(List<E> processors) {        
        int selectedProcessorIndex = selectProcessIndex();
        return processors.get(selectedProcessorIndex);
    }
    
    private synchronized int selectProcessIndex() {
//        if (runtimeRatioSum == 0) { // every processor is exhausted, reload for a new distribution round
//            for (DistributionRatio distributionRatio : getRuntimeRatios()) {
//                int weight = distributionRatio.getDistributionWeight();
//                distributionRatio.setRuntimeWeight(weight);
//            }
//            runtimeRatioSum = distributionRatioSum;
//        }

        DistributionRatio selected = null;
        int randomWeight = rnd.nextInt(runtimeRatioSum);
        int choiceWeight = 0;
        List<DistributionRatio> lists =  getRuntimeRatios();
        if(lists == null || lists.size() == 0) {
        	return 0;
        }
        for (DistributionRatio distributionRatio : lists) {
            choiceWeight += distributionRatio.getRuntimeWeight();
            if (randomWeight < choiceWeight) {
                selected = distributionRatio;
                break;
            }
        }
        
       // selected.setRuntimeWeight(selected.getRuntimeWeight() - 1);
       // runtimeRatioSum--;
        if (selected == null) {
        	return 0;
        }
        return selected.getProcessorPosition();
    }
}
