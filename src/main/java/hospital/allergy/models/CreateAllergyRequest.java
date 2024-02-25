package hospital.allergy.models;

import hospital.allergy.entities.AllergySubType;

public class CreateAllergyRequest {

    private String patientId;
    private AllergySubType type;
    private String description;
    private String idempotencyKey;

    public CreateAllergyRequest(
            final String patientId,
            final AllergySubType type,
            final String description,
            final String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
        this.patientId = patientId;
        this.type = type;
        this.description = description;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public AllergySubType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
