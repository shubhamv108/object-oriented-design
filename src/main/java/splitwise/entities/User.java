package splitwise.entities;

import splitwise.entities.expenditures.Expenditure;
import splitwise.entities.splits.Split;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class User {
    private String name;
    private String emailId;
    private String mobileNumber; // unique
    private String photoUrl;
    private String password;

    private List<Expenditure> expenditures;

    public User(final String name, final String emailId, final String mobileNumber, final String photoUrl,
                final String password, final List<Expenditure> expenditures) {
        this.name = name;
        this.emailId = emailId;
        this.mobileNumber = mobileNumber;
        this.photoUrl = photoUrl;
        this.password = password;
        this.expenditures = new ArrayList<>();
    }

    public void addExpenditures(final Expenditure<Split> expenditure) {
        if (expenditure != null)
            this.expenditures.add(expenditure);
    }

    public void addExpenditures(final Collection<Expenditure<Split>> expenditures) {
        if (expenditures != null)
            this.expenditures.addAll(expenditures);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Expenditure> getExpenditures() {
        return expenditures;
    }

    public void setExpenditures(List<Expenditure> expenditures) {
        this.expenditures = expenditures;
    }
}
