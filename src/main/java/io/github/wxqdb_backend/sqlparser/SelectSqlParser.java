package io.github.wxqdb_backend.sqlparser;
/**
 *
 * 单句查询语句解析器
 */
//正确
public class SelectSqlParser extends BaseSingleSqlParser{

	public SelectSqlParser(String originalSql)
	{
		super(originalSql);//调用基类的构造函数

	}

	@Override
	protected void initializeSegments(){//在构造函数里调用
		//System.out.println("调用了SelectSqlParser的initializeSegments方法，用于初始化正则表达式语句");
		segments.add(new SqlSegment("(select)(.+)(from)","[,]"));
		segments.add(new SqlSegment("(from)(.+?)(where |group\\s+by|having|order\\s+by | ENDOFSQL)","(,|s+lefts+joins+|s+rights+joins+|s+inners+joins+)"));
		segments.add(new SqlSegment("(where)(.+?)(group\\s+by |having| order\\s+by | ENDOFSQL)","(and|or)"));
		segments.add(new SqlSegment("(group\\s+by)(.+?)(having|order\\s+by| ENDOFSQL)","[,]"));
		segments.add(new SqlSegment("(having)(.+?)(order\\s+by| ENDOFSQL)","(and|or)"));
		segments.add(new SqlSegment("(order\\s+by)(.+)( ENDOFSQL)","[,]"));
	}

}
