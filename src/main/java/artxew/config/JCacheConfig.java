package artxew.config;
import org.springframework.boot.context.event.ApplicationReadyEvent;
// import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import artxew.framework.layers.cmcd.dto.res.QueryList_CmCd_ResDto;
import artxew.framework.layers.cmcd.svc.CmCdSvc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Artxe2
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class JCacheConfig implements ApplicationListener<ApplicationReadyEvent> {
	// private final CacheManager cacheManager;
	private final CmCdSvc cmCdSvc;

	/**
	 * @author Artxe2
	 */
	@Override
	public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
		// cacheManager.getCache("queryList_CmCd").clear();
		QueryList_CmCd_ResDto.QueryItem[] array = cmCdSvc.queryList_CmCd();
		String key = null;
		for (var a : array) {
			if (!a.getGrpCd().equals(key)) {
				key = a.getGrpCd();
				cmCdSvc.queryList_CmCd(array, key);
			}
		}
		log.info("Cache loading is complete");
	}
}
