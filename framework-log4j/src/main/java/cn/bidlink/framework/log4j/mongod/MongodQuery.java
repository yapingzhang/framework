/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>MongodQuery.java</p>
 *   
 */
package cn.bidlink.framework.log4j.mongod;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午03:00:20
 */
public class MongodQuery {
	private DBObject condition;

	public MongodQuery() {
		condition = new BasicDBObject();
	}

	public DBObject getCondition() {
		return condition;
	}

	public void setCondition(final DBObject condition) {
		this.condition = condition;
	}

	/**
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param op
	 * @param value
	 */
	protected final void append(final String key, final String op, final Object value) {
		if (op == null) {
			condition.put(key, value);
			return;
		}
		Object obj = condition.get(key);
		DBObject dbo = null;
		if (!(obj instanceof DBObject)) {
			dbo = new BasicDBObject(op, value);
			condition.put(key, dbo);
		} else {
			dbo = (DBObject) condition.get(key);
			dbo.put(op, value);
		}
	}

	/**
	 * {field: value }
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param value
	 * @return
	 */
	public MongodQuery equals(final String key, final Object value) {
		append(key, null, value);
		return this;
	}

	/**
	 * {field: {$ne: value} }
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param value
	 * @return
	 */
	public MongodQuery notEquals(final String key, final Object value) {
		append(key, MongodOperator.NE, value);
		return this;
	}

	/**
	 * { field: { $gt: value} }
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param value
	 * @return
	 */
	public MongodQuery greaterThan(final String key, final Object value) {
		append(key, MongodOperator.GT, value);
		return this;
	}

	/**
	 * { field: { $gte: value} }
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param value
	 * @return
	 */
	public MongodQuery greaterThanEquals(final String key, final Object value) {
		append(key, MongodOperator.GTE, value);
		return this;
	}

	/**
	 * { field: { $lt: value} }
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param value
	 * @return
	 */
	public MongodQuery lessThan(final String key, final Object value) {
		append(key, MongodOperator.LT, value);
		return this;
	}

	/**
	 * { field: { $lte: value} }
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param value
	 * @return
	 */
	public MongodQuery lessThanEquals(final String key, final Object value) {
		append(key, MongodOperator.LTE, value);
		return this;
	}

	/**
	 * { field: { $in: [<value1>, <value2>, ... <valueN> ] } }
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param values
	 * @return
	 */
	public MongodQuery in(final String key, final Object... values) {
		append(key, MongodOperator.IN, values);
		return this;
	}

	/**
	 * { field: { $nin: [ <value1>, <value2> ... <valueN> ]} }
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param values
	 * @return
	 */
	public MongodQuery notIn(final String key, final Object... values) {
		append(key, MongodOperator.NIN, values);
		return this;
	}

	/**
	 * { field: { $all: [ <value> , <value1> ... ] }
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param values
	 * @return
	 */
	public MongodQuery all(final String key, final Object... values) {
		append(key, MongodOperator.ALL, values);
		return this;
	}

	/**
	 * { field: /acme.*corp/i }
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param regex
	 * @return
	 */
	public MongodQuery regex(final String key, final String regex) {
		append(key, null, Pattern.compile(regex));
		return this;
	}

	/**
	 * { field: { $size: 2 } }
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param value
	 * @return
	 */
	public MongodQuery size(final String key, final int value) {
		append(key, MongodOperator.SIZE, value);
		return this;
	}

	/**
	 * { field: { $mod: [ divisor, remainder ]}
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param divisor
	 * @param remainder
	 * @return
	 */
	public MongodQuery mod(final String key, final int divisor, final int remainder) {
		append(key, MongodOperator.MOD, new int[] { divisor, remainder });
		return this;
	}

	@SuppressWarnings("unchecked")
	public MongodQuery or(final MongodQuery... qs) {
		List<DBObject> list = (List<DBObject>) condition.get(MongodOperator.OR);
		if (list == null) {
			list = new ArrayList<DBObject>();
		}
		for (MongodQuery q : qs)
			list.add(q.getCondition());
		condition.put(MongodOperator.OR, list);
		return this;
	}

	@SuppressWarnings("unchecked")
	public MongodQuery and(final MongodQuery... qs) {
		List<DBObject> list = (List<DBObject>) condition.get(MongodOperator.AND);
		if (list == null) {
			list = new ArrayList<DBObject>();
		}
		for (MongodQuery q : qs)
			list.add(q.getCondition());
		condition.put(MongodOperator.AND, list);
		return this;
	}

	/**
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param value
	 *            -1,1
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public MongodQuery sort(final String key, final int value) {
		List<DBObject> list = (List<DBObject>) condition.get(MongodOperator.SORT);
		DBObject dbo = new BasicDBObject();
		dbo.put(key, value == 1 ? 1 : -1);
		if (list == null) {
			list = new ArrayList<DBObject>();
		}
		list.add(dbo);
		condition.put(MongodOperator.OR, list);
		return this;
	}

	/**
	 * { $skip : 5 }
	 * 
	 * @Description: TODO add description
	 * @param skip
	 * @return
	 */
	public MongodQuery skip(final int skip) {
		condition.put(MongodOperator.SKIP, skip);
		return this;
	}

	/**
	 * { $limit : 5 }
	 * 
	 * @Description: TODO add description
	 * @param limit
	 * @return
	 */
	public MongodQuery limit(final int limit) {
		condition.put(MongodOperator.LIMIT, limit);
		return this;
	}

}

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午03:00:20
 */
class MongodOperator {

	// id
	public static final String ID = "_id";

	// compare and conditon
	public static final String GT = "$gt";
	public static final String GTE = "$gte";
	public static final String LT = "$lt";
	public static final String LTE = "$lte";
	public static final String NE = "$ne";
	public static final String AND = "$and";
	public static final String OR = "$or";
	public static final String IN = "$in";
	public static final String NIN = "$nin";
	public static final String MOD = "$mod";
	public static final String ALL = "$all";
	public static final String SIZE = "$size";
	public static final String EXISTS = "$exists";

	// 2d and geo
	public static final String NEAR = "$near";
	public static final String WITHIN = "$within";
	public static final String CENTER = "$center";
	public static final String BOX = "$box";

	// modify
	public static final String SET = "$set";
	public static final String UNSET = "$unset";
	public static final String INC = "$inc";
	public static final String PUSH = "$push";
	public static final String PULL = "$pull";

	// aggregation
	public static final String PROJECT = "$project";
	public static final String MATCH = "$match";
	public static final String LIMIT = "$limit";
	public static final String SKIP = "$skip";
	public static final String UNWIND = "$unwind";
	public static final String GROUP = "$group";
	public static final String SORT = "$sort";

}
