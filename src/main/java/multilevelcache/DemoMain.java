package multilevelcache;

import multilevelcache.facades.LevelCacheCreatorFacade;
import multilevelcache.service.ICacheService;

import java.util.Arrays;

public class DemoMain {

    private final ICacheService<Integer, Integer> cacheService;

    public DemoMain(final ICacheService<Integer, Integer> cacheService) {
        this.cacheService = cacheService;
    }

    public void demo() {
        for (int k = 1; k < 12; k++) {
            this.cacheService.set(k, 1);
        }
        System.out.println(this.cacheService.get(1));
        System.out.println(this.cacheService.getCacheAvailableCapacity());
        System.out.println(this.cacheService.getCacheStatistics(200));
    }

    public static void main(String[] args) {
        ICacheService<Integer, Integer> cacheService
                = new LevelCacheCreatorFacade<Integer, Integer>().getCacheService(Arrays.asList(10, 100, 1000));
        DemoMain main = new DemoMain(cacheService);
        main.demo();
    }

}
