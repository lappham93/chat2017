package com.mit.http;

import com.mit.define.ApiError;
import com.mit.utils.JsonUtils;

/**
 * Created by Hung Le on 2/13/17.
 */
public class ApiResponse<T> {
    private int code = 0;
    private String msg = "";
    public T data;
    private Paging paging;

    public ApiResponse() {
    }

    public ApiResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiResponse(ApiError apiError) {
        this.code = apiError.getValue();
        this.msg = apiError.getMessage();
    }

    public ApiResponse(T data) {
        this.data = data;
    }
    
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

    public static class Paging {
        private String previous;
        private String next;

        public Paging() {}

        public Paging(boolean hasMore, int current) {
        	next = hasMore ? current + 1 + "" : null;
        	previous  = current > 1 ? current - 1 + "" : null;
        }
        
        public Paging(String previous, String next) {
			super();
			this.previous = previous;
			this.next = next;
		}

		public String getPrevious() {
            return previous;
        }

        public void setPrevious(String previous) {
            this.previous = previous;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }
    }
}
