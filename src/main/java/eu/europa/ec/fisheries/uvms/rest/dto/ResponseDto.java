package eu.europa.ec.fisheries.uvms.rest.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResponseDto<T> {

    private T data;
    private int code;
    private String msg;

    public ResponseDto(T data, int code) {
        this.data = data;
        this.code = code;
    }

    public ResponseDto(int code) {
        this.code = code;
    }

    public ResponseDto(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public ResponseDto(T data, int code, String msg) {
    	this.data = data;
        this.code = code;
        this.msg = msg;
    }
    
    public ResponseDto() {
    }
    
    
    public T getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.data);
        hash = 23 * hash + Objects.hashCode(this.code);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResponseDto<?> other = (ResponseDto<?>) obj;
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ResponseDto{" + "data=" + data + ", code=" + code + '}';
    }

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
