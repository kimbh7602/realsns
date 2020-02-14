package edu.ssafy.boot.dto;

public class TempImageVo {
	private String user_id;
	private String image_name;
	private String image_url;
	
	public TempImageVo() {
		super();
	}

	public TempImageVo(String user_id, String image_name, String image_url) {
		super();
		this.user_id = user_id;
		this.image_name = image_name;
		this.image_url = image_url;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getImage_name() {
		return image_name;
	}

	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	@Override
	public String toString() {
		return "TempImageVo [user_id=" + user_id + ", image_name=" + image_name + ", image_url=" + image_url + "]";
	}
	
	
}
