package model;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Setting extends Model {
    @Id
    private String setting;

    @Column(nullable = false)
    private String value;

    public static Finder<String, Setting> find = new Finder<>(Setting.class);

    public static Setting create (String setting, String value) {
        Setting settingObj = new Setting();

        settingObj.setting = setting;
        settingObj.value = value;

        settingObj.save();
        return settingObj;
    }

    public String getSetting() {
        return setting;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
