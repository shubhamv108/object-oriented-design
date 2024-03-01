package hospital.allergy;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PatientManager {

    private final Map<String, Set<String>> allergyIds = new ConcurrentHashMap<>();

    public void addAllergy(final String patientId, final String allergyId) {
        this.allergyIds.computeIfAbsent(patientId, k -> new HashSet<>()).add(allergyId);
    }

    public Set<String> getAllergies(final String patientId) {
        return this.allergyIds.get(patientId);
    }

    public static PatientManager getManager() {
        return SingletonHolder.INSTANCE;
    }

    public void remove(final String patientId, final String id) {
        Optional.ofNullable(this.allergyIds.get(patientId))
                .ifPresent(ids -> ids.remove(id));
    }

    private static final class SingletonHolder {
        private static final PatientManager INSTANCE = new PatientManager();
    }
}
