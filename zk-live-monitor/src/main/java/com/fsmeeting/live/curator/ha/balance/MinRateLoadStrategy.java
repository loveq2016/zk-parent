package com.fsmeeting.live.curator.ha.balance;

import com.alibaba.fastjson.JSON;
import com.fsmeeting.live.bean.LiveService;
import com.fsmeeting.live.enums.LiveServerType;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Description:服务器策略:负载比率最小
 *
 * @Author:yicai.liu<虚竹子>
 */
public class MinRateLoadStrategy extends LoadStrategy {

    private static final Logger logger = LoggerFactory.getLogger(MinRateLoadStrategy.class);

    /**
     * 服务缓存
     */
    private Map<String, LiveService> serviceCaches;

    public MinRateLoadStrategy(Map<String, LiveService> serviceCaches) {
        this.serviceCaches = serviceCaches;
    }

    @Override
    public LiveService getService(final LiveServerType appType, final String oldAddress, Object... params) {

        //过滤
        Collection<LiveService> services = Collections2.filter(serviceCaches.values(), new Predicate<LiveService>() {
            @Override
            public boolean apply(LiveService liveService) {
                //1、type匹配 2、旧地址不择取
                return !(appType.getCode() != liveService.getAppType() || (null != oldAddress && oldAddress.equals(liveService.getAddress())));
            }
        });

        if (services.isEmpty()) {
            logger.info("Service address not found!");
            return null;
        }

        //排序
        List<LiveService> filterServices = Lists.newArrayList(services);
        Collections.sort(filterServices, new Comparator<LiveService>() {

            @Override
            public int compare(LiveService left, LiveService right) {
                BigDecimal leftLoadRate = new BigDecimal(Integer.toString(left.getCurLoad()))
                        .divide(new BigDecimal(Integer.toString(left.getLoad())), 4, RoundingMode.HALF_UP);

                BigDecimal rightLoadRate = new BigDecimal(Integer.toString(right.getCurLoad()))
                        .divide(new BigDecimal(Integer.toString(right.getLoad())), 4, RoundingMode.HALF_UP);
                return leftLoadRate.compareTo(rightLoadRate);
            }
        });

        logger.info("Pick the first one:" + new BigDecimal(filterServices.get(0).getCurLoad())
                .divide(new BigDecimal(filterServices.get(0).getLoad()), 4, RoundingMode.HALF_UP).toString());

        //选择
        return filterServices.get(0);
    }

    public static void main(String[] args) {
        Map<String, LiveService> serviceCaches = new HashMap<String, LiveService>();

        LiveService service = new LiveService();
        service.setAppId("01");
        service.setAppType(1);//直播
        service.setCurLoad(2);
        service.setLoad(1000);
        service.setAddress("01");

        LiveService service1 = new LiveService();
        service1.setAppId("02");
        service1.setAppType(1);//直播
        service1.setCurLoad(3);
        service1.setLoad(1000);
        service1.setAddress("02");

        LiveService service2 = new LiveService();
        service2.setAppId("03");
        service2.setAppType(1);//直播
        service2.setCurLoad(4);
        service2.setLoad(1000);
        service2.setAddress("03");

        LiveService service3 = new LiveService();
        service3.setAppId("04");
        service3.setAppType(1);//直播
        service3.setCurLoad(5);
        service3.setLoad(1000);
        service3.setAddress("04");

        LiveService service4 = new LiveService();
        service4.setAppId("05");
        service4.setAppType(2);//直播
        service4.setCurLoad(1);
        service4.setLoad(1000);
        service4.setAddress("05");

        serviceCaches.put(service.getAppId(), service);
        serviceCaches.put(service1.getAppId(), service1);
        serviceCaches.put(service2.getAppId(), service2);
        serviceCaches.put(service3.getAppId(), service3);
        serviceCaches.put(service4.getAppId(), service4);

        LiveService result = new MinRateLoadStrategy(serviceCaches).getService(LiveServerType.LIVE, "01");

        logger.info(JSON.toJSONString(result));
    }
}
