package com.sola.baselib.model.http;

/**
 * 
 */

public class ResultModel<T> {

	public T data;

	// 状态值
	public int statue = Statue.ERROR;

	public String message = "请求失败";

	public int count;
	
	public boolean isSuccess(){
		return statue == Statue.SUCCESS;
	}

	public String getToast() {
		switch (statue) {
		case Statue.NET_ERROR:
			return "网络加载失败";
		case Statue.ERROR:
			return
					null != message && message.length() > 0 ? message : "请求失败";
		case Statue.PARSER_ERROR:
			return "解析失败";
		default:
			return "";
		}
	}

}
