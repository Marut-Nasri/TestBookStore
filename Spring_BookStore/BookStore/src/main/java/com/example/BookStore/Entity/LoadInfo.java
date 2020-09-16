package com.example.BookStore.Entity;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document
public class LoadInfo {
	
	@Id @JsonIgnore 
	private int id;
	private String recommendbookurl;
	private String allbookurl;
	private String lastupdate;
	private int requesttimeout;
	
	public LoadInfo() {}

    public LoadInfo(String recommendbookurl, String allbookurl,String lastupdate,int requesttimeout) 
    {
      this.id = 1;
      this.setRecommendBookURI(recommendbookurl);      
      this.setAllBookURI(allbookurl);
      this.setLastUpdate(lastupdate);
      this.setRequestTimeOut(requesttimeout);
    }
    
    public String getRecommendBookURI() {
		return this.recommendbookurl;
	}

	public void setRecommendBookURI(String recommendbookurl) {
		this.recommendbookurl = recommendbookurl;
	}

	public String getAllBookURI() {
		return this.allbookurl;
	}

	public void setAllBookURI(String allbookurl) {
		this.allbookurl = allbookurl;
	}    
    
    public String getLastUpdate() {
    	return this.lastupdate;
    }
    
    public void setLastUpdate(String lastupdate) {
		this.lastupdate = lastupdate;
	}
    
    public int getRequestTimeOut() {
    	return this.requesttimeout;
    }
    
    public void setRequestTimeOut(int requesttimeout) 
    {
    	this.requesttimeout = requesttimeout;
    }    
}
