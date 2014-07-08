
package cn.bidlink.framework.core.utils;

import java.util.Random;

/**
 * 创建一个随机的任意字符串
 * (示例：DFDxw0OFSYg5qOYFUVgcGI1BbFZKbROr)
 */
public abstract class SpecialCodeGenerateUtils {

    /** 用于创建随机字符串的字符集 */
    private static StringBuffer CODE_SEED;

    /** 用于创建随机字符串的字符集长度 */
    private static int CODE_SEED_LENGTH = 62;

    /** 随机字符串的长度 */
    private static int CODE_LENGTH = 32;

    static {
        CODE_SEED = new StringBuffer("a,b,c,d,e,f,g,h,i,g,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z");
        CODE_SEED.append(",A,B,C,D,E,F,G,H,I,G,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z");
        CODE_SEED.append(",1,2,3,4,5,6,7,8,9,0");
    }

    private SpecialCodeGenerateUtils() {
    }

    /**
     * 获取一个长度为<b>CODE_LENGTH</b>的随机的字符串
     * @return String 随机的字符串
     */
    public static String getSpecialCode() {
        String[] arr = CODE_SEED.toString().split(",");
        StringBuffer b = new StringBuffer();
        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < CODE_LENGTH; i++) {
            int k = r.nextInt();
            b.append(String.valueOf(arr[Math.abs(k % CODE_SEED_LENGTH)]));
        }
        return b.toString();
    }
}
