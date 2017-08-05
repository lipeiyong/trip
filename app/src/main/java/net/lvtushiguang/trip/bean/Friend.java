package net.lvtushiguang.trip.bean;

/**
 * 好友实体类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年11月6日 上午11:37:31
 * 
 */
@SuppressWarnings("serial")
public class Friend extends Entity {
	
    private String userid;
    
    private String name;
    
    private String from;
    
    private String portrait;
    
    private String expertise;
    
    private int gender;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
