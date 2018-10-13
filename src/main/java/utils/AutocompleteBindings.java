package utils;

import model.Dose;
import model.Medicine;
import model.Study;
import model.Treatment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AutocompleteBindings {
    private static AutocompleteBindings instance = new AutocompleteBindings();

    private List<String> treatmentNames;
    private List<String> studyNames;
    private List<String> medicineNames;

    private AutocompleteBindings() {
        this.treatmentNames = Treatment.find.query().select("name").findList().stream().map(Treatment::getName).collect(Collectors.toList());
        this.studyNames = Study.find.query().select("name").findList().stream().map(Study::getName).collect(Collectors.toList());
        this.medicineNames = Medicine.find.query().select("name").findList().stream().map(Medicine::getName).collect(Collectors.toList());
    }

    public static  AutocompleteBindings getInstance(){
        return instance;
    }

    public List<String> getTreatmentNames() {
        return treatmentNames;
    }

    public List<String> getStudyNames() {
        return studyNames;
    }

    public List<String> getMedicineNames() {
        return medicineNames;
    }

    public List<String> getDosesForMedicine(String medicine){
        List<String> doses = new ArrayList<>();
        Medicine medicineObj = Medicine.find.query().where().eq("name", medicine).findOne();

        if(medicineObj!=null) {
            doses = medicineObj.getDoses().stream().map(Dose::getDose).collect(Collectors.toList());
        }

        return doses;
    }

    public void addTreatmentName(String treatmentName) {
        this.treatmentNames.add(treatmentName);
    }

    public void addStudyName(String studyName) {
        this.studyNames.add(studyName);
    }

    public void addMedicineName(String medicineName) {
        this.medicineNames.add(medicineName);
    }

    public void removeTreatmentName(String treatmentName) {
        this.treatmentNames.remove(treatmentName);
    }

    public void removeStudyName(String studyName) {
        this.studyNames.remove(studyName);
    }

    public void removeMedicineName(String medicineName) {
        this.medicineNames.remove(medicineName);
    }
}
