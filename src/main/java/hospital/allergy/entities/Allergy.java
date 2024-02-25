package hospital.allergy.entities;

public class Allergy {

    private String patientId;
    private AllergySubType type;
    private String description;

    public String getPatientId() {
        return patientId;
    }

    public Allergy(final String patientId, final AllergySubType type, final String description) {
        this.patientId = patientId;
        this.type = type;
        this.description = description;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setType(AllergySubType type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Allergy{" +
                "patientId='" + patientId + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                '}';
    }
}
