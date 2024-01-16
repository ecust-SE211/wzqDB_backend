package io.github.wxqdb_backend.sqlparser;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSingleSqlParser {

	//原始Sql语句
	protected String originalSql;

	//Sql语句片段
	protected List<SqlSegment> segments;//片段，sqlSegment中包含了start，end，body和很多正则表达式


	/** *//**
	 　* 构造函数，传入原始Sql语句，进行劈分。
	 　* @param originalSql
	 　*/
	public BaseSingleSqlParser(String originalSql)
	{
		//System.out.println("调用了BaseSingleSqlParser的构造函数");
		this.originalSql=originalSql;
		segments=new ArrayList<SqlSegment>();
		initializeSegments();//初始化
	}

	/**
	 　* 初始化segments，强制子类实现
	 　*
	 　*/
	protected abstract void initializeSegments();


	/**
	 　* 将originalSql劈分成一个个片段
	 　*
	 */
	public List<List<String>> splitSql2Segment()//分割成两个模块
	{
		List<List<String>> list=new ArrayList<List<String>>();

		//System.out.println("调用了BaseSingleSqlParser的splitSql2Segment方法，用于分割sql为不同的模块");
		for(SqlSegment sqlSegment:segments)//对于每一个正则表达式，进行对串的处理
		{
			sqlSegment.parse(originalSql);//对于初始的sql语句再进行正则分析,获取
			list.add(sqlSegment.getBodyPieces());//获取真正有用的部分
			System.out.println(sqlSegment.getBodyPieces());
		}
		return list;
	}

	/**
	 　* 得到解析完毕的Sql语句
	 　* @return
	 　*/
	public String getParsedSql()
	{
		StringBuffer sb=new StringBuffer();//线程安全的字符串
		for(SqlSegment sqlSegment:segments)
		{
			sb.append(sqlSegment.getParsedSqlSegment()+"n");
		}
		String retval=sb.toString().replaceAll("n+", "n");
		return retval;
	}

}
