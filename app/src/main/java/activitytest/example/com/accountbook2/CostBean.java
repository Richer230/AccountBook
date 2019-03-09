package activitytest.example.com.accountbook2;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CostBean {

    private String costTitle;

    private String costDate;

    private double costMoney;

    private String uuid;

    private long costTime;

    private int costType;

    private String costMonth;

    private String costRemark;

    CostBean(){
        costTime = new Date().getTime();
        uuid = UUID.randomUUID().toString();
    }

    public String getCostTitle() {
        return costTitle;
    }

    public void setCostTitle(String costTitle) {
        this.costTitle = costTitle;
    }

    public String getCostDate() {
        return costDate;
    }

    public void setCostDate(String costDate) {
        this.costDate = costDate;
    }

    public double getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(double costMoney) {
        this.costMoney = costMoney;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public int getCostType() {
        return costType;
    }

    public void setCostType(int costType) {
        this.costType = costType;
    }

    public String getCostMonth() {
        return costMonth;
    }

    public void setCostMonth(String costMonth) {
        this.costMonth = costMonth;
    }

    public String getTime(){
      return  MyDate.getTime(costTime);
    }

    public String getCostRemark() {
        return costRemark;
    }

    public void setCostRemark(String costRemark) {
        this.costRemark = costRemark;
    }
}
