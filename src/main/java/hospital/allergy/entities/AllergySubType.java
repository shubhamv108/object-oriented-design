package hospital.allergy.entities;

public enum AllergySubType {

    AA(AllergyType.A),
    AB(AllergyType.A),
    BA(AllergyType.B),
    BB(AllergyType.B),
    CA(AllergyType.C),
    CB(AllergyType.C);
    private AllergyType type;

    AllergySubType(final AllergyType type) {
        this.type = type;
    }
}
