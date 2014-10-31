package com.store.api.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.api.mongo.entity.OnlineTime;
import com.store.api.mongo.entity.UploadGpsGroup;
import com.store.api.mongo.service.OnlineTimeService;
import com.store.api.mongo.service.VeUploadGpsMGService;
import com.store.api.mysql.entity.enumeration.UserType;
import com.store.api.mysql.service.CreditAndTicketManagementService;
import com.store.api.mysql.service.XmppService;
import com.store.api.session.RedisService;
import com.store.api.utils.JsonUtils;
import com.store.api.utils.Utils;

/**
 * Revision History
 * 
 * 2014年8月16日,vincent,created it
 */
@Service
public class OnlineTimeJob {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private static final String REDIS_LOCK_KEY = "GIVE_POINT_LOCK";

    private static final String REDIS_LOCK_VALUE = "YES";

    // LOCK过期时间
    private static final int EXPIRE = 60 * 5;

    // 间隔时间
    private static final long INTERVAL = 1000 * 60 * 60 * 14;

    @Autowired
    private OnlineTimeService onlineTimeService;

    @Autowired
    private VeUploadGpsMGService veUploadGpsMGService;

    @Autowired
    private CreditAndTicketManagementService creditAndTicketManagementService;

    @Autowired
    private XmppService xmppService;

    public void execute() {
        LOG.info("OnlineTimeJob is run...");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 5);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date start = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date end = cal.getTime();

        Long startTime = System.currentTimeMillis();

        try {
            String lock = RedisService.getInstance().get(REDIS_LOCK_KEY);
            // 是否其它服务器在执行
            if (Utils.isEmpty(lock)) {
                LOG.info("OnlineTimeJob is begin...");
                // 开始执行时加锁，防止其它服务器同时执行,设置过期时间是防止程序异常导致锁没被清除的情况
                RedisService.getInstance().set(REDIS_LOCK_KEY, REDIS_LOCK_VALUE, EXPIRE);
                List<UploadGpsGroup> group = veUploadGpsMGService.groupUploadGpsWithId(start.getTime(), end.getTime());
                int giveCount = 0;
                if (null != group && group.size() > 0) {
                    List<OnlineTime> onlineList = new ArrayList<OnlineTime>();
                    for (UploadGpsGroup gps : group) {
                        OnlineTime online = onlineTimeService.findOne(gps.getUserId());
                        boolean flag = gps.getLast() - gps.getFirst() >= INTERVAL;
                        if (null != online) {
                            online.setFirstTime(gps.getFirst());
                            online.setLastTime(gps.getLast());
                            if (flag) {
                                online.setGiveCount(online.getGiveCount() + 1);
                                giveCount++;
                            }
                            onlineList.add(online);
                        } else {// 没有online记录的，写入新记录
                            if (flag) {
                                OnlineTime newonline = new OnlineTime();
                                newonline.setFirstTime(gps.getFirst());
                                newonline.setLastTime(0L);
                                newonline.setId(gps.getUserId());
                                newonline.setGiveCount(1);
                                onlineList.add(newonline);
                                giveCount++;
                            }
                        }
                    }
                    if (onlineList.size() > 0)
                        onlineTimeService.save(onlineList);
                }
                // 操作完成，清除锁
                RedisService.getInstance().remove(REDIS_LOCK_KEY);
                LOG.info(giveCount + "位用户当天在线时长满足.");
                LOG.info("OnlineTimeJob is end...");
            }
        } catch (Exception e) {
            LOG.error("JOB执行异常", e);
        }
        long endTime = System.currentTimeMillis();
        LOG.info("JOB 执行时间(毫秒)：" + (endTime - startTime));
    }
}
