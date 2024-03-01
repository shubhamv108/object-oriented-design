package hospital.allergy.managers;

import hospital.allergy.PatientManager;
import hospital.allergy.entities.Allergy;
import hospital.allergy.managers.validators.CreateAllergyRequestValidator;
import hospital.allergy.managers.validators.UpdateAllergyRequestValidator;
import hospital.allergy.models.CreateAllergyRequest;
import hospital.allergy.models.UpdateAllergyRequest;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AllergyManager {

    private final Map<String, Allergy> allergies = new ConcurrentHashMap<>();

    public void create(final CreateAllergyRequest request) {
        CreateAllergyRequestValidator.validateOrThrowException(request);
        Allergy existing = this.allergies.get(request.getIdempotencyKey());
        if (existing != null)
            throw new IllegalArgumentException("request to create allergy already processed");

        final Allergy allergy = new Allergy(request.getPatientId(), request.getType(), request.getDescription());

        synchronized (request.getPatientId()) {
            existing = this.allergies.get(request.getIdempotencyKey());
            if (existing != null)
                throw new IllegalArgumentException("request to create allergy already processed");

            this.allergies.put(request.getIdempotencyKey(), allergy);
            PatientManager.getManager().addAllergy(request.getPatientId(), request.getIdempotencyKey());
        }
    }

    public Collection<Allergy> getByPatientId(final String patientId) {
        if (patientId == null || patientId.isEmpty())
            throw new IllegalArgumentException("patientId must not be null or empty");

        Optional.ofNullable(PatientManager.getManager().getAllergies(patientId))
                .orElseThrow(() -> new IllegalArgumentException("no allergies for patientId: " + patientId + " found"));

        synchronized (patientId) {
            return Optional.ofNullable(PatientManager.getManager().getAllergies(patientId))
                    .map(allergies -> allergies.stream().map(this.allergies::get).collect(Collectors.toSet()))
                    .orElseThrow(() -> new IllegalArgumentException("no allergies for patientId: " + patientId + " found"));
        }
    }

    public Allergy getById(final String id) {
        if (id == null || id.isEmpty())
            throw new IllegalArgumentException("id must not be null");

        return Optional.ofNullable(this.allergies.get(id))
                .orElseThrow(IllegalArgumentException::new);
    }

    public void update(final UpdateAllergyRequest request) {
        UpdateAllergyRequestValidator.validateOrThrowException(request);

        Allergy existing = Optional.ofNullable(this.getById(request.getAllergyId()))
                .orElseThrow(IllegalArgumentException::new);
        synchronized (request.getAllergyId()) {
            existing = Optional.ofNullable(this.getById(request.getAllergyId()))
                    .orElseThrow(IllegalArgumentException::new);

            existing.setType(request.getType());
            existing.setDescription(request.getDescription());
        }
    }

    public void delete(final String id) {
        if (id == null || id.isEmpty())
            throw new IllegalArgumentException("id must not be null");

       Optional.ofNullable(this.allergies.get(id))
               .orElseThrow(() -> new IllegalArgumentException("no allergy for given id " + id + " found"));

        synchronized (id) {
            Optional.ofNullable(this.allergies.remove(id))
                    .map(allergy -> {
                        synchronized (allergy.getPatientId()) {
                            PatientManager.getManager().remove(allergy.getPatientId(), id);
                        }
                        System.out.println("Deleted allergy with id: " + id);
                        return allergy;
                    })
                    .orElseThrow(() -> new IllegalArgumentException("no allergy for given id " + id + " found"));
        }
    }

    public static AllergyManager getManager() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final AllergyManager INSTANCE = new AllergyManager();
    }

}
