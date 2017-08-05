package net.lvtushiguang.trip.bean;

/**
 * 登录用户实体类
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
@SuppressWarnings("serial")
public class User extends Entity {

    private String loginName;

    private String password;

    private String no;

    private String name;

    private String gender;//保留字段

    private String email;

    private String phone;

    private String mobile;

    private String photo;

    private String loginIp;

    private String loginDate;

    private String companyName;//归属公司

    private String officeName;//归属部门

    private String roleName;//用户角色

    private String alias;//别名

    private String aseKey;

    // 本地缓存多余信息
    private String cookie;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAseKey() {
        return aseKey;
    }

    public void setAseKey(String aseKey) {
        this.aseKey = aseKey;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    @Override
    public String toString() {
        return "User [uid=" + id + ", loginName=" + loginName + ", password=" + password
                + ", no=" + no + ", name=" + name + ", gender="
                + gender + ", email=" + email + ", phone=" + phone
                + ", mobile=" + mobile + ", photo=" + photo
                + ", loginIp=" + loginIp + ", loginDate=" + loginDate
                + ", companyName=" + companyName + ", officeName=" + officeName
                + ", roleName=" + roleName + ",alias=" + alias + ",aseKey=" + aseKey + "]";
    }
}
