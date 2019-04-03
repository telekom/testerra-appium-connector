package eu.tsystems.mms.tic.testframework.mobile.pageobjects.guielement;

/**
 * Created by rnhb on 08.01.2016.
 */
public class StatusContainer {

    private String status;

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return " Status = " + status;
    }
}
