package hospital.allergy;

import hospital.allergy.entities.AllergySubType;
import hospital.allergy.managers.AllergyManager;
import hospital.allergy.models.CreateAllergyRequest;
import hospital.allergy.models.UpdateAllergyRequest;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AllergySystemDriver {

    public static void main(String[] args) {
        final String clientAllergyId = UUID.randomUUID().toString();
        final AllergyManager allergyManager = AllergyManager.getManager();

        ExecutorService executor = Executors.newFixedThreadPool(20);


        executor.submit(() -> allergyManager.create(new CreateAllergyRequest("1", AllergySubType.AA, "dsad", clientAllergyId)));
        executor.submit(() -> allergyManager.create(new CreateAllergyRequest("1", AllergySubType.AA, "dsad", clientAllergyId)));
        executor.submit(() -> allergyManager.create(new CreateAllergyRequest("2", AllergySubType.AA, "dsad", clientAllergyId)));
        executor.submit(() -> System.out.println(allergyManager.getById(clientAllergyId)));
        executor.submit(() -> System.out.println(allergyManager.getByPatientId("1")));
        executor.submit(() -> allergyManager.update(new UpdateAllergyRequest(clientAllergyId, AllergySubType.AB, "dsadad")));
        executor.submit(() -> System.out.println(allergyManager.getById(clientAllergyId)));

        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.submit(() -> System.out.println(allergyManager.getById(clientAllergyId)));
        executor.submit(() -> allergyManager.delete(clientAllergyId));
        executor.submit(() -> System.out.println(allergyManager.getById(clientAllergyId)));

        while (!executor.isTerminated())
            executor.shutdown();
    }

}
