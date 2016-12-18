package model.pojo;

public class User {
    private String id;
    private int stones;
    private boolean login;
    private boolean boss;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStones() {
        return stones;
    }

    public void setStones(int stones) {
        if(stones > 0) {
            this.stones = stones;
        }
        else {
            this.stones = 0;
        }
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public boolean isBoss() {
        return boss;
    }

    public void setBoss(boolean boss) {
        this.boss = boss;
    }
}
