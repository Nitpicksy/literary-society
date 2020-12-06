package nitpicksy.literarysociety.serviceimpl;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import nitpicksy.literarysociety.model.Log;
import nitpicksy.literarysociety.service.LogService;
import nitpicksy.literarysociety.utils.IPAddressProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class ChangePasswordAttemptService {

    private final String CLASS_PATH = this.getClass().getCanonicalName();
    private final String CLASS_NAME = this.getClass().getSimpleName();

    private final int MAX_ATTEMPT = 3;

    private final HttpServletRequest request;

    private final IPAddressProvider ipAddressProvider;

    private final LogService logService;

    private LoadingCache<String, Integer> changePassAttemptsCache = CacheBuilder.newBuilder().
            expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<>() {
        public Integer load(String key) {
            return 0;
        }
    });

    public void changePassSucceeded() {
        String key = getClientIP();
        changePassAttemptsCache.invalidate(key);
    }

    public void changePassFailed() {
        String key = getClientIP();
        int attempts;
        try {
            if (changePassAttemptsCache.get(key) == MAX_ATTEMPT) {
                logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CPW", String.format("Because of too many attempts, user from %s is blocked.", ipAddressProvider.get())));
            }
            attempts = changePassAttemptsCache.get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        changePassAttemptsCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        try {
            if (changePassAttemptsCache.get(key) == MAX_ATTEMPT) {
                logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CPW", String.format("Because of too many attempts, user from %s is blocked.", ipAddressProvider.get())));
            }
            return changePassAttemptsCache.get(key) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    @Autowired
    public ChangePasswordAttemptService(HttpServletRequest request, IPAddressProvider ipAddressProvider, LogService logService) {
        this.request = request;
        this.ipAddressProvider = ipAddressProvider;
        this.logService = logService;
    }
}

