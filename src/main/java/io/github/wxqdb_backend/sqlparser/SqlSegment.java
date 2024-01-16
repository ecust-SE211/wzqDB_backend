package io.github.wxqdb_backend.sqlparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sql语句片段
 * @since 2018-4-4
 */
public class SqlSegment {
	private static final String Crlf = "|";
	@SuppressWarnings("unused")
	private static final String FourSpace = "　　";
	//Sql语句片段开头部分
	private String start;

	 //Sql语句片段中间部分
	private String body;

	 //Sql语句片段结束部分		　
	private String end;

	 //用于分割中间部分的正则表达式
	private String bodySplitPattern;

	 //表示片段的正则表达式
	private String segmentRegExp;

	 //分割后的Body小片段
	private List<String> bodyPieces;
	/**
	 　* 构造函数
	 　* @param segmentRegExp 表示这个Sql片段的正则表达式
	 　* @param bodySplitPattern 用于分割body的正则表达式
	 　*/
	public SqlSegment(String segmentRegExp,String bodySplitPattern)
	{
		start="";
		body="";
		end="";
		this.segmentRegExp=segmentRegExp;
		this.bodySplitPattern=bodySplitPattern;
		this.bodyPieces = new ArrayList<String>();
	}

	/**
	 　* 从sql中查找符合segmentRegExp的部分，并赋值到start,body,end等三个属性中
	 　* @param sql
	 　*/
	public void parse(String sql)
	{
		//进行测试（测试分块程序是否正确，该代码为正确的，现要测试一共分块了几次，结果都为什么？
//-----------------------------------------------------------------------------------------------------------
//		System.out.println();
//		System.out.println("开始对sql进行分块");
//		System.out.println("分块");
//-----------------------------------------------------------------------------------------------------------
		Pattern pattern=Pattern.compile(segmentRegExp,Pattern.CASE_INSENSITIVE);
		Matcher matcher=pattern.matcher(sql);
		while(matcher.find())
		{
			start=matcher.group(1);//关键字
			body=matcher.group(2);
			end=matcher.group(3);//关键字
//			System.out.println("start");
//			System.out.println(start);
//			System.out.println("end");
//			System.out.println(end);
			parseBody();

		}
	}

	/**
	 　* 解析body部分
	 　*
	 　*/
	private void  parseBody()//用针对body的分法再分块，对bodyPeces进行赋值
	{
		//System.out.println("分body");
		List<String> ls=new ArrayList<String>();
		Pattern p = Pattern.compile(bodySplitPattern,Pattern.CASE_INSENSITIVE);
		// 先清除掉前后空格
		body=body.trim();
		Matcher m = p.matcher(body);
		StringBuffer sb = new StringBuffer();
		boolean result = m.find();
//---------------------------------------------------------------------------------------------------------------------------
		//测试下group（0）是什么，他怎么进行替换的？
//		if(result)
//		{
//			System.out.println(m.group(0));
//		}
//---------------------------------------------------------------------------------------------------------------------------
		while (result)
		{
			m.appendReplacement(sb,Crlf);
			result = m.find();
		}
		m.appendTail(sb);
//---------------------------------------------------------------------------------------------------------------------------
//		System.out.println(sb.toString());
//---------------------------------------------------------------------------------------------------------------------------
		// 再按空格断行
		ls.add(start);
		String[] arr=sb.toString().split("[|]");
		int arrLength=arr.length;
		for(int i=0;i<arrLength;i++)
		{
//			System.out.println(arr[i]);
//			String temp=FourSpace+arr[i];
//			System.out.println(temp);
			ls.add(arr[i]);
		}
		bodyPieces = ls;
	}

	/**
	 　* 取得解析好的Sql片段
	 　* @return
	 　*/
	public String getParsedSqlSegment()
	{
		///这个函数返回一个字符串，该字符串包含一个SQL语句的解析部分。它使用一个字符串缓冲区来构建这个字符串，首先添加一个起始部分，然后循环遍历一个包含SQL语句各个部分的列表，并将每个部分添加到缓冲区中，每个部分之间添加一个换行符。最后，将缓冲区转换为字符串并返回。

		StringBuffer sb=new StringBuffer();
		sb.append(start+Crlf);
		for(String piece:bodyPieces)
		{
			sb.append(piece+Crlf);
		}
		return sb.toString();
	}


	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getBodySplitPattern() {
		return bodySplitPattern;
	}

	public void setBodySplitPattern(String bodySplitPattern) {
		this.bodySplitPattern = bodySplitPattern;
	}

	public String getSegmentRegExp() {
		return segmentRegExp;
	}

	public void setSegmentRegExp(String segmentRegExp) {
		this.segmentRegExp = segmentRegExp;
	}

	public List<String> getBodyPieces() {
		return bodyPieces;
	}

	public void setBodyPieces(List<String> bodyPieces) {
		this.bodyPieces = bodyPieces;
	}

}
