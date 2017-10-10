package com.zimny.socialfood.model;

/**
 * Created by ideo7 on 17.08.2017.
 */

public class Relationship {
    private String uidFriend1;
    private String uidFriend2;
    private Boolean invite;

    public Relationship() {
    }

    public Relationship(String uidFriend1, String uidFriend2, Boolean invite) {
        this.uidFriend1 = uidFriend1;
        this.uidFriend2 = uidFriend2;
        this.invite = invite;
    }

    public String getUidFriend1() {
        return uidFriend1;
    }

    public void setUidFriend1(String uidFriend1) {
        this.uidFriend1 = uidFriend1;
    }

    public String getUidFriend2() {
        return uidFriend2;
    }

    public void setUidFriend2(String uidFriend2) {
        this.uidFriend2 = uidFriend2;
    }

    public Boolean getInvite() {
        return invite;
    }

    public void setInvite(Boolean invite) {
        this.invite = invite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Relationship)) return false;

        Relationship that = (Relationship) o;

        if (((!uidFriend1.equals(that.uidFriend1) && !uidFriend2.equals(that.uidFriend2))) || ((!uidFriend2.equals(that.uidFriend1) && !uidFriend1.equals(that.uidFriend2))))
            return false;
        return invite.equals(that.invite);

    }

    @Override
    public int hashCode() {
        int result = uidFriend1.hashCode();
        result = 31 * result + uidFriend2.hashCode();
        result = 31 * result + invite.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Relationship{" +
                "uidFriend1='" + uidFriend1 + '\'' +
                ", uidFriend2='" + uidFriend2 + '\'' +
                ", invite=" + invite +
                '}';
    }
}
