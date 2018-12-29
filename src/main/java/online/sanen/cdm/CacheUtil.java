package online.sanen.cdm;

import java.net.URL;

import com.mhdt.analyse.Validate;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import online.sanen.cdm.basic.Structure;

/**
 * 
 * @author online.sanen
 * Date： 2017/11/22
 * Time: 20:34
 *
 */
public class CacheUtil {  
    //ehcache.xml Save under  src/main/resources/  
    private static final String path = "resource/cache.xml";  
  
    private URL url;  
  
    private CacheManager manager;  
  
    private static CacheUtil ehCache;  
  
    private CacheUtil(String path) {  
        url = getClass().getResource(path);  
        manager = CacheManager.create(url);  
    }  
  
    public static CacheUtil getInstance() {  
        if (ehCache == null) {  
            ehCache = new CacheUtil(path);  
        }  
        return ehCache;  
    }  
  
    public void put(String cacheName, Object key, Object value) {  
        Cache cache = manager.getCache(cacheName);  
        Element element = new Element(key, value);  
        cache.put(element);  
    }  
  
    public Object get(String cacheName, Object key) {  
        Cache cache = manager.getCache(cacheName);  
        if(cache==null)return null;
        Element element = cache.get(key);  
        return element == null ? null : element.getObjectValue();  
    }  
  
    public Cache getCache(String cacheName) {  
        return manager.getCache(cacheName);  
    }  
  
    public void remove(String cacheName, String key) {  
        Cache cache = manager.getCache(cacheName);  
        cache.remove(key);  
    }

	public Cache createCache(String tabelName) {
		if(manager.cacheExists(tabelName))
			return manager.getCache(tabelName);
			
		 Cache memoryOnlyCache = new Cache(tabelName, 5000, false, false, 7200, 7200);  
		 manager.addCache(memoryOnlyCache);
		 return memoryOnlyCache;
	}
	
	public static String defaultCache = "temp";

	@SuppressWarnings("deprecation")
	public String getCacheInfo(Structure structure) {
		try {
			String cacheName = Validate.isNullOrEmpty(structure.getTableName())?defaultCache:structure.getTableName();
			 int size = getCache(cacheName).getSize();
			 return  cacheName+"/"+size+"/"+getCache(cacheName).getCacheConfiguration().getMaxElementsInMemory();
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		 
	}  
}  