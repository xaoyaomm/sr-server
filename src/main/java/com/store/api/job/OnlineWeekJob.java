package com.store.api.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.api.common.Common;
import com.store.api.mongo.entity.OnlineTime;
import com.store.api.mongo.entity.UploadGpsGroup;
import com.store.api.mongo.service.OnlineTimeService;
import com.store.api.mongo.service.VeUploadGpsMGService;
import com.store.api.mysql.entity.PlUsers;
import com.store.api.mysql.entity.TbNews;
import com.store.api.mysql.entity.VeVehicleInfo;
import com.store.api.mysql.service.CreditAndTicketManagementService;
import com.store.api.mysql.service.PlUsersService;
import com.store.api.mysql.service.TbNewsService;
import com.store.api.mysql.service.VeVehicleInfoService;
import com.store.api.session.RedisService;
import com.store.api.utils.Utils;

@Service
public class OnlineWeekJob {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private static final String REDIS_LOCK_KEY = "GIVE_POINT_LOCK";

    private static final String REDIS_LOCK_VALUE = "YES";
    
    // 在线时长
    private long ONLINE_TIME = 1000 * 60 * 60 * 10;
    
    //周期
    private int CYCLE=7;

    @Autowired
    private OnlineTimeService onlineTimeService;

    @Autowired
    private VeUploadGpsMGService veUploadGpsMGService;

    @Autowired
    private PlUsersService plUsersService;

    @Autowired
    private VeVehicleInfoService veVehicleInfoService;

    @Autowired
    private TbNewsService tbNewsService;

    @Autowired
    private CreditAndTicketManagementService creditAndTicketManagementService;
    
    public long getONLINE_TIME() {
        return ONLINE_TIME;
    }

    public int getCYCLE() {
        return CYCLE;
    }

    public void setONLINE_TIME(long oNLINE_TIME) {
        ONLINE_TIME = oNLINE_TIME;
    }

    public void setCYCLE(int cYCLE) {
        CYCLE = cYCLE;
    }

    // LOCK过期时间
    private static final int EXPIRE = 60 * 10;
    
    private SimpleDateFormat sm = new SimpleDateFormat("MM.dd");

    public void execute() {
        LOG.info("OnlineWeekJob is run...");

        Long startTime = System.currentTimeMillis();

        try {
            String lock = RedisService.getInstance().get(REDIS_LOCK_KEY);
            // 是否其它服务器在执行
            if (Utils.isEmpty(lock)) {
                LOG.info("OnlineWeekJob is begin...");
                // 开始执行时加锁，防止其它服务器同时执行,设置过期时间是防止程序异常导致锁没被清除的情况
                RedisService.getInstance().set(REDIS_LOCK_KEY, REDIS_LOCK_VALUE, EXPIRE);
                
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
                calculate(cal.getTime(), CYCLE, ONLINE_TIME, false);
                
                List<Long> userIds = new LinkedList<Long>();
                List<OnlineTime> list = onlineTimeService.findAll();
                if (null != list && list.size() > 0) {
                    for (OnlineTime online : list) {
                        if (online.getGiveCount() >= CYCLE)
                            userIds.add(online.getId());
                    }
                    newsNotify(userIds);
                }
                // 操作完成，清除锁
                RedisService.getInstance().remove(REDIS_LOCK_KEY);
                LOG.info(userIds.size() + "位用户一周在线时长满足.");
                LOG.info("OnlineWeekJob is end...");
            }
        } catch (Exception e) {
            LOG.error("JOB执行异常", e);
        }
        long endTime = System.currentTimeMillis();
        LOG.info("JOB 执行时间(毫秒)：" + (endTime - startTime));
    }

    private void newsNotify(List<Long> userIds) throws Exception {
        if (null != userIds && userIds.size() > 0) {
            StringBuffer sb = new StringBuffer();
            sb.append("<span style=\"background-color:#FF9900;\"><span style=\"background-color:#E56600;\"><p style=\"margin-top:0.3em;margin-bottom:0.3em;line-height:28px;color:#666464;font-family:'Lucida Grande', Verdana, sans-serif;font-size:14px;white-space:normal;background-color:#FFFFFF;\"><table style=\"width:100%;\" cellpadding=2 cellspacing=0 border=1 bordercolor=\"#000000\"><tbody><tr><td><strong>车牌号</strong></td><td><strong>车主姓名</strong></td><td><strong>所获奖项</strong></td></tr>");
            for (Long id : userIds) {
                PlUsers user = plUsersService.findOne(id);
                if (null != user) {
                    List<VeVehicleInfo> infos = veVehicleInfoService.findVehicleByUserId(id);
                    if (null != infos && infos.size() > 0) {
                        VeVehicleInfo info = infos.get(0);
                        sb.append("<tr id=\"").append(user.getMobile()).append("\"><td>");
                        sb.append(Common.hidePlate(info.getVePlates())).append("</td>");
                        sb.append("<td>").append(user.getUserName()).append("</td>");
                        sb.append("<td>20元现金</td></tr>");
                    }
                }
            }
            sb.append("</tbody></table></p></span></span>");

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
            String endd = sm.format(cal.getTime());
            cal.set(Calendar.DATE, cal.get(Calendar.DATE) - CYCLE-1);
            String startd = sm.format(cal.getTime());
            String title = "上周(" + startd + "-" + endd + ")“在线送话费”获奖名单";

            TbNews news = new TbNews();
            news.setTitle(title);
            news.setContent(sb.toString());
            news.setCreatetime(new Date());
            news.setUpdatetime(new Date());
            news.setPublictime(new Date());
            news.setNewstypeid(1L);
            news.setType(1L);
            tbNewsService.save(news);
        }
    }

    /**
     * 计算从endDate往前的一星期内用户每天在线满interval的数据，记入MONGO
     * 
     * @param endDate
     *            结束日期
     * @param cycle
     *            周期
     * @param onlineTime
     *            每日在线时长
     * @param isShowCount
     *            是否统计一星期内所有满足在线条件的用户，信息打印在日志中
     */
    public String calculate(Date endDate, int cycle, long onlineTime, boolean ifShowCount) {
        onlineTimeService.deleteAll(); // 清除所有数据
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < cycle; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            cal.set(Calendar.DATE, cal.get(Calendar.DATE) - i);
            cal.set(Calendar.HOUR_OF_DAY, 5);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            Date start = cal.getTime();

            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            Date end = cal.getTime();
            List<UploadGpsGroup> group = veUploadGpsMGService.groupUploadGpsWithId(start.getTime(), end.getTime());
            int giveCount = 0;
            if (null != group && group.size() > 0) {
                List<OnlineTime> onlineList = new ArrayList<OnlineTime>();
                for (UploadGpsGroup gps : group) {
                    OnlineTime online = onlineTimeService.findOne(gps.getUserId());
                    boolean flag = gps.getLast() - gps.getFirst() >= onlineTime;
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
                String info=sm.format(cal.getTime()) + "共有" + giveCount + "位用户当天在线时长满足"+onlineTime/60/60/1000+"小时";
                sb.append(info).append("<br/>");
                LOG.info(info);
                
            }
        }
        if (ifShowCount) {
            List<Long> userIds = new LinkedList<Long>();
            List<OnlineTime> list = onlineTimeService.findAll();
            StringBuffer cont=new StringBuffer();
            
            if (null != list && list.size() > 0) {
                for (OnlineTime online : list) {
                    if (online.getGiveCount() >= cycle){
                        userIds.add(online.getId());
                        PlUsers user = plUsersService.findOne(online.getId());
                        cont.append("<ol>").append(user.getMobile()).append("  ").append(user.getUserName()).append("</ol>");
                    }
                    
                }
            }
            String info="<storg>共有"+userIds.size()+"位用户满足"+cycle+"天在线时间</storg>";
            sb.append(info).append("<br/>").append(cont);
            LOG.info(info);
        }
        return sb.toString();
    }

}
