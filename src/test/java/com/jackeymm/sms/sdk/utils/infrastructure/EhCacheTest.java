package com.jackeymm.sms.sdk.utils.infrastructure;

import com.jackeymm.sms.sdk.domains.KeyPair;
import com.jackeymm.sms.sdk.infrastructure.EhCache;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ehcache.config.builders.ExpiryPolicyBuilder.timeToLiveExpiration;

public class EhCacheTest {

    private EhCache<String, KeyPair> ehCache;

    private final String key = "test";

    @Before
    public void init(){
        this.ehCache = new EhCache<>(10, String.class, KeyPair.class,timeToLiveExpiration(Duration.ofSeconds(10)));
    }
    

    @Test
    public void userEhCacheGetValueSuccessfully(){
        KeyPair keyPair = new KeyPair("token","a@t.email");
        ehCache.put(this.key, keyPair);

        Optional<KeyPair> ok = ehCache.get(this.key);
        assertThat(ok).isPresent();
        assertThat(ok.get()).isEqualToComparingFieldByField(keyPair);

    }
}
