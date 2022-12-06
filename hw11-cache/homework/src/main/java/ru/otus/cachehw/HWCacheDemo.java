package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    public static void main(String[] args) {
        new HWCacheDemo().demo();
    }

    private void demo() {
        HwCache<String, Integer> cache = new MyCache<>();

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        HwListener<String, Integer> listener = new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                logger.info("L1_key:{}, value:{}, action: {}", key, value, action);
            }
        };
        HwListener<String, Integer> listener2 = new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                logger.info("L2_key:{}, value:{}, action: {}", key, value, action);
            }
        };

        cache.addListener(listener);
        cache.addListener(listener2);

        cache.put(new String("1"), 1);
        cache.put(String.valueOf(2), 2);
        cache.put("3", 3); // автоматическое интернирование литералов
        cache.put(new String("4").intern(), 4);

        logger.info("getValue:{}", cache.get("1"));

        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        logger.info("getValue 1:{}", cache.get("1")); // null
        logger.info("getValue 2:{}", cache.get("2")); // null
        logger.info("getValue 3:{}", cache.get("3")); // 3
        logger.info("getValue 4:{}", cache.get("4")); // 4
        cache.remove("1");

        cache.removeListener(listener);
        cache.removeListener(listener2);
    }
}
