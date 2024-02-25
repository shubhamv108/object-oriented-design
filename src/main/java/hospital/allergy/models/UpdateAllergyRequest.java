package hospital.allergy.models;

import hospital.allergy.entities.AllergySubType;

public class UpdateAllergyRequest {

    private String allergyId;
    private AllergySubType type;
    private String description;

    public UpdateAllergyRequest(
            final String allergyId,
            final AllergySubType type,
            final String description) {
        this.allergyId = allergyId;
        this.type = type;
        this.description = description;
    }

    public String getAllergyId() {
        return allergyId;
    }

    public AllergySubType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
