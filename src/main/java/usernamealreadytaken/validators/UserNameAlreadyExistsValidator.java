package usernamealreadytaken.validators;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import multilevelcache.service.CacheService;
import usernamealreadytaken.ICacheService;
import usernamealreadytaken.IUserService;
import usernamealreadytaken.IUsernameAlreadyExistsValidator;
import usernamealreadytaken.cache.InMemoryTTLCache;
import usernamealreadytaken.user.MockUserService;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class UserNameAlreadyExistsValidator implements IUsernameAlreadyExistsValidator {

    private final BloomFilter<String> bloomFilter;
    private final ICacheService<String, Boolean> cacheService;
    private final IUserService userService;

    private static final int EXPECTED_INSERTIONS = 10000000;
    private static final double FALSE_POSITIVE_PROBABILITY = 0.01;

    private static final Long TIME_TO_LIVE_IN_MILLISECONDS = 500000000L;

    public UserNameAlreadyExistsValidator(ICacheService<String, Boolean> cacheService, IUserService userService) {
        this.bloomFilter = BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8),
                EXPECTED_INSERTIONS,
                FALSE_POSITIVE_PROBABILITY);
        this.cacheService = cacheService;
        this.userService = userService;
    }

    @Override
    public Boolean exists(String userName) {
        if (this.bloomFilter.mightContain(userName)) {
            return Optional.ofNullable(cacheService.get(userName))
                    .orElseGet(() -> {
                        boolean isExisting = this.userService.isUsernameTaken(userName);
                        cacheService.put(userName, isExisting, TIME_TO_LIVE_IN_MILLISECONDS);
                        return isExisting;
                    });
        } else {
            return false;
        }
    }

    @Override
    public void add(String userName) {
        this.bloomFilter.put(userName);
        cacheService.put(userName, true, TIME_TO_LIVE_IN_MILLISECONDS);
    }

}
