package com.nino.chat.server.model;

import java.util.Date;

/**
 * @author ss
 * @date 2018/9/11 14:29
 */
public class User extends Deliver {

    private int id;
    private String nickName;
    private int sex;
    private String email;
    private String phone;
    private Date birthday;

    private User() {
        super(DeliverType.USER);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int bId;
        private String bNickName;
        private int bSex;
        private String bEmail;
        private String bPhone;
        private Date bBirthday;

        public Builder id(int id) {
            this.bId = id;
            return this;
        }

        public Builder nickName(String nickName) {
            this.bNickName = nickName;
            return this;
        }

        public Builder sex(int sex) {
            this.bSex = sex;
            return this;
        }

        public Builder email(String email) {
            this.bEmail = email;
            return this;
        }

        public Builder phone(String phone) {
            this.bPhone = phone;
            return this;
        }

        public Builder birthDay(Date birthDay) {
            this.bBirthday = birthDay;
            return this;
        }

        public User build() {
            User user = new User();
            user.setId(bId);
            user.setBirthday(bBirthday);
            user.setEmail(bEmail);
            user.setNickName(bNickName);
            user.setPhone(bPhone);
            user.setSex(bSex);
            return user;
        }
    }
}
