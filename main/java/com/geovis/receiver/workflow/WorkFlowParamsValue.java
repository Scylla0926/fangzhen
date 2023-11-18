package com.geovis.receiver.workflow;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geovis.receiver.dao.ThresholdValueDao;
import com.geovis.receiver.dao.WorkFlowDao;
import com.geovis.receiver.pojo.bean.ThresholdValueBean;
import com.geovis.receiver.pojo.bean.WorkFlowBean;
import com.geovis.receiver.tools.HttpClient;
import com.geovis.receiver.tools.ReadConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 调用算法工具类
 * 准备调用算法的参数
 */
@Component
public class WorkFlowParamsValue {


    @Value("${workflow_ip}")
    private String workflowIp;

    @Value("${workflow_port}")
    private String workflowPort;

    @Value("${workflow_app}")
    private String workflowApp;

    @Autowired
    private ReadConfig rc;
    @Resource
    private ThresholdValueDao thresholdValueDao;
    @Resource
    private HttpClient httpClient;
    @Resource
    private WorkFlowDao workFlowDao;


    /**
     * cora产品  调用相关算法
     *
     * @param fileDate
     * @return
     */
    public boolean sendPostCoraWorkFlowParams(String fileDate) {
        // 该类标识
        String code = "CORA";
        boolean flag = true;

        QueryWrapper<WorkFlowBean> wrapper = null;
        wrapper = new QueryWrapper<WorkFlowBean>();
        wrapper.like("wf_description", code);
        List<WorkFlowBean> workFlowBeans = workFlowDao.selectList(wrapper);

        if (workFlowBeans == null) {
            flag = false;
        }
        QueryWrapper<ThresholdValueBean> queryWrapper = null;

        for (int i = 0; i < workFlowBeans.size(); i++) {

            try {
                queryWrapper = new QueryWrapper<ThresholdValueBean>();
                queryWrapper.eq("ele_type", workFlowBeans.get(i).getWfDescription().split("_")[1]);
                ThresholdValueBean thresholdValueBean = thresholdValueDao.selectOne(queryWrapper);

                JSONObject jsonObject = new JSONObject();
                if (thresholdValueBean != null) {
                    jsonObject = JSONObject.parseObject(thresholdValueBean.getValue());
                }
                jsonObject.put("date", fileDate);

                JSONObject params = new JSONObject();
                params.put("workflowId", workFlowBeans.get(i).getId());
                params.put("appId", workFlowBeans.get(i).getAppId());
                params.put("delay", 0);
                params.put("initParams", jsonObject.toJSONString());

                httpClient.sendPost_json(workflowIp, workflowPort, workflowApp, "", params);
            } catch (Exception e) {
                flag = false;
            }
        }
        System.out.println(workFlowBeans.size());
        return flag;
    }

    /**
     * nwp产品
     *
     * @param fileDate
     * @param timer
     * @return
     */
    public boolean sendPostNwpWorkFlowParams(String fileDate, String timer) {
        // 该类标识
        String code = "NWP";
        boolean flag = true;

        QueryWrapper<WorkFlowBean> wrapper = null;
        wrapper = new QueryWrapper<WorkFlowBean>();
        wrapper.like("wf_description", code);
        List<WorkFlowBean> workFlowBeans = workFlowDao.selectList(wrapper);

        if (workFlowBeans == null) {
            flag = false;
        }
        QueryWrapper<ThresholdValueBean> queryWrapper = null;

        for (int i = 0; i < workFlowBeans.size(); i++) {

            try {
                queryWrapper = new QueryWrapper<ThresholdValueBean>();
                queryWrapper.eq("ele_type", workFlowBeans.get(i).getWfDescription().split("_")[1]);
                ThresholdValueBean thresholdValueBean = thresholdValueDao.selectOne(queryWrapper);

                JSONObject jsonObject = new JSONObject();
                if (thresholdValueBean != null) {
                    jsonObject = JSONObject.parseObject(thresholdValueBean.getValue());
                }
                jsonObject.put("date", fileDate.substring(0, 8));
                jsonObject.put("time", timer);
                jsonObject.put("timeInt", Integer.valueOf(timer));
                jsonObject.put("hour", fileDate.substring(8, 10));

                JSONObject params = new JSONObject();
                params.put("workflowId", workFlowBeans.get(i).getId());
                params.put("appId", workFlowBeans.get(i).getAppId());
                params.put("delay", 0);
                params.put("initParams", jsonObject.toJSONString());

                httpClient.sendPost_json(workflowIp, workflowPort, workflowApp, "", params);
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * Modas产品
     *
     * @param fileDate
     * @return
     */
    public boolean sendPostModasWorkFlowParams(String fileDate) {
        // 该类标识
        String code = "MODAS";
        boolean flag = true;

        QueryWrapper<WorkFlowBean> wrapper = null;
        wrapper = new QueryWrapper<WorkFlowBean>();
        wrapper.like("wf_description", code);
        List<WorkFlowBean> workFlowBeans = workFlowDao.selectList(wrapper);

        if (workFlowBeans == null) {
            flag = false;
        }
        QueryWrapper<ThresholdValueBean> queryWrapper = null;

        for (int i = 0; i < workFlowBeans.size(); i++) {

            try {
                queryWrapper = new QueryWrapper<ThresholdValueBean>();
                queryWrapper.eq("ele_type", workFlowBeans.get(i).getWfDescription().split("_")[1]);
                ThresholdValueBean thresholdValueBean = thresholdValueDao.selectOne(queryWrapper);

                JSONObject jsonObject = new JSONObject();
                if (thresholdValueBean != null) {
                    jsonObject = JSONObject.parseObject(thresholdValueBean.getValue());
                }
                jsonObject.put("date", fileDate);

                JSONObject params = new JSONObject();
                params.put("workflowId", workFlowBeans.get(i).getId());
                params.put("appId", workFlowBeans.get(i).getAppId());
                params.put("delay", 0);
                params.put("initParams", jsonObject.toJSONString());

                httpClient.sendPost_json(workflowIp, workflowPort, workflowApp, "", params);
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * Woa18产品
     *
     * @return
     */
    public boolean sendPostWoa18WorkFlowParams(String fileDate) {
        // 该类标识
        String code = "WOA18";
        boolean flag = true;

        QueryWrapper<WorkFlowBean> wrapper = null;
        wrapper = new QueryWrapper<WorkFlowBean>();
        wrapper.like("wf_description", code);
        List<WorkFlowBean> workFlowBeans = workFlowDao.selectList(wrapper);

        if (workFlowBeans == null) {
            flag = false;
        }
        QueryWrapper<ThresholdValueBean> queryWrapper = null;

        for (int i = 0; i < workFlowBeans.size(); i++) {
            try {
                queryWrapper = new QueryWrapper<ThresholdValueBean>();
                queryWrapper.eq("ele_type", workFlowBeans.get(i).getWfDescription().split("_")[1]);
                ThresholdValueBean thresholdValueBean = thresholdValueDao.selectOne(queryWrapper);

                JSONObject jsonObject = new JSONObject();
                if (thresholdValueBean != null) {
                    jsonObject = JSONObject.parseObject(thresholdValueBean.getValue());
                }
                jsonObject.put("mon", fileDate);

                JSONObject params = new JSONObject();
                params.put("workflowId", workFlowBeans.get(i).getId());
                params.put("appId", workFlowBeans.get(i).getAppId());
                params.put("delay", 0);
                params.put("initParams", jsonObject.toJSONString());

                httpClient.sendPost_json(workflowIp, workflowPort, workflowApp, "", params);
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * Argo产品
     *
     * @return
     */
    public boolean sendPostArgoWorkFlowParams(String fileDate, String fileName) {
        // 该类标识
        String code = "ARGO";
        boolean flag = true;

        QueryWrapper<WorkFlowBean> wrapper = null;
        wrapper = new QueryWrapper<WorkFlowBean>();
        wrapper.like("wf_description", code);
        List<WorkFlowBean> workFlowBeans = workFlowDao.selectList(wrapper);

        if (workFlowBeans == null) {
            flag = false;
        }
        QueryWrapper<ThresholdValueBean> queryWrapper = null;

        for (int i = 0; i < workFlowBeans.size(); i++) {
            try {
                queryWrapper = new QueryWrapper<ThresholdValueBean>();
                queryWrapper.eq("ele_type", workFlowBeans.get(i).getWfDescription().split("_")[1]);
                ThresholdValueBean thresholdValueBean = thresholdValueDao.selectOne(queryWrapper);

                JSONObject jsonObject = new JSONObject();
                if (thresholdValueBean != null) {
                    jsonObject = JSONObject.parseObject(thresholdValueBean.getValue());
                }
                jsonObject.put("date", fileDate);
                jsonObject.put("filename", fileName);

                JSONObject params = new JSONObject();
                params.put("workflowId", workFlowBeans.get(i).getId());
                params.put("appId", workFlowBeans.get(i).getAppId());
                params.put("delay", 0);
                params.put("initParams", jsonObject.toJSONString());

                httpClient.sendPost_json(workflowIp, workflowPort, workflowApp, "", params);
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * HyCom产品
     *
     * @param fileDate
     * @return
     */
    public boolean sendPostHyComWorkFlowParams(String fileDate) {
        // 该类标识
        String code = "HYCOM";
        boolean flag = true;

        QueryWrapper<WorkFlowBean> wrapper = null;
        wrapper = new QueryWrapper<WorkFlowBean>();
        wrapper.like("wf_description", code);
        List<WorkFlowBean> workFlowBeans = workFlowDao.selectList(wrapper);

        if (workFlowBeans == null) {
            flag = false;
        }
        QueryWrapper<ThresholdValueBean> queryWrapper = null;

        for (int i = 0; i < workFlowBeans.size(); i++) {
            try {
                queryWrapper = new QueryWrapper<ThresholdValueBean>();
                queryWrapper.eq("ele_type", workFlowBeans.get(i).getWfDescription().split("_")[1]);
                ThresholdValueBean thresholdValueBean = thresholdValueDao.selectOne(queryWrapper);

                JSONObject jsonObject = new JSONObject();
                if (thresholdValueBean != null) {
                    jsonObject = JSONObject.parseObject(thresholdValueBean.getValue());
                }
                jsonObject.put("date", fileDate);

                JSONObject params = new JSONObject();
                params.put("workflowId", workFlowBeans.get(i).getId());
                params.put("appId", workFlowBeans.get(i).getAppId());
                params.put("delay", 0);
                params.put("initParams", jsonObject.toJSONString());

                httpClient.sendPost_json(workflowIp, workflowPort, workflowApp, "", params);
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * Oscar产品
     *
     * @param fileDate
     * @return
     */
    public boolean sendPostOscarWorkFlowParams(String fileDate) {
        // 该类标识
        String code = "OSCAR";
        boolean flag = true;

        QueryWrapper<WorkFlowBean> wrapper = null;
        wrapper = new QueryWrapper<WorkFlowBean>();
        wrapper.like("wf_description", code);
        List<WorkFlowBean> workFlowBeans = workFlowDao.selectList(wrapper);

        if (workFlowBeans == null) {
            flag = false;
        }
        QueryWrapper<ThresholdValueBean> queryWrapper = null;

        for (int i = 0; i < workFlowBeans.size(); i++) {
            try {
                queryWrapper = new QueryWrapper<ThresholdValueBean>();
                queryWrapper.eq("ele_type", workFlowBeans.get(i).getWfDescription().split("_")[1]);
                ThresholdValueBean thresholdValueBean = thresholdValueDao.selectOne(queryWrapper);

                JSONObject jsonObject = new JSONObject();
                if (thresholdValueBean != null) {
                    jsonObject = JSONObject.parseObject(thresholdValueBean.getValue());
                }
                jsonObject.put("date", fileDate);

                JSONObject params = new JSONObject();
                params.put("workflowId", workFlowBeans.get(i).getId());
                params.put("appId", workFlowBeans.get(i).getAppId());
                params.put("delay", 0);
                params.put("initParams", jsonObject.toJSONString());

                httpClient.sendPost_json(workflowIp, workflowPort, workflowApp, "", params);
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * Aviso产品
     *
     * @param fileDate
     * @return
     */
    public boolean sendPostAvisoWorkFlowParams(String fileDate) {
        // 该类标识
        String code = "AVISO";
        boolean flag = true;

        QueryWrapper<WorkFlowBean> wrapper = null;
        wrapper = new QueryWrapper<WorkFlowBean>();
        wrapper.like("wf_description", code);
        List<WorkFlowBean> workFlowBeans = workFlowDao.selectList(wrapper);

        if (workFlowBeans == null) {
            flag = false;
        }
        QueryWrapper<ThresholdValueBean> queryWrapper = null;

        for (int i = 0; i < workFlowBeans.size(); i++) {
            try {
                queryWrapper = new QueryWrapper<ThresholdValueBean>();
                queryWrapper.eq("ele_type", workFlowBeans.get(i).getWfDescription().split("_")[1]);
                ThresholdValueBean thresholdValueBean = thresholdValueDao.selectOne(queryWrapper);

                JSONObject jsonObject = new JSONObject();
                if (thresholdValueBean != null) {
                    jsonObject = JSONObject.parseObject(thresholdValueBean.getValue());
                }
                jsonObject.put("date", fileDate);

                JSONObject params = new JSONObject();
                params.put("workflowId", workFlowBeans.get(i).getId());
                params.put("appId", workFlowBeans.get(i).getAppId());
                params.put("delay", 0);
                params.put("initParams", jsonObject.toJSONString());

                httpClient.sendPost_json(workflowIp, workflowPort, workflowApp, "", params);
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * SST-VAM产品
     *
     * @param fileDate
     * @return
     */
    public boolean sendPostSstVamWorkFlowParams(String fileDate) {
        // 该类标识
        String code = "SST";
        boolean flag = true;

        QueryWrapper<WorkFlowBean> wrapper = null;
        wrapper = new QueryWrapper<WorkFlowBean>();
        wrapper.like("wf_description", code);
        List<WorkFlowBean> workFlowBeans = workFlowDao.selectList(wrapper);

        if (workFlowBeans == null) {
            flag = false;
        }
        QueryWrapper<ThresholdValueBean> queryWrapper = null;

        for (int i = 0; i < workFlowBeans.size(); i++) {
            try {
                queryWrapper = new QueryWrapper<ThresholdValueBean>();
                queryWrapper.eq("ele_type", workFlowBeans.get(i).getWfDescription().split("_")[1]);
                ThresholdValueBean thresholdValueBean = thresholdValueDao.selectOne(queryWrapper);

                JSONObject jsonObject = new JSONObject();
                if (thresholdValueBean != null) {
                    jsonObject = JSONObject.parseObject(thresholdValueBean.getValue());
                }
                jsonObject.put("date", fileDate.substring(0, 8));
                jsonObject.put("hour", fileDate.substring(8, 10));

                JSONObject params = new JSONObject();
                params.put("workflowId", workFlowBeans.get(i).getId());
                params.put("appId", workFlowBeans.get(i).getAppId());
                params.put("delay", 0);
                params.put("initParams", jsonObject.toJSONString());

                httpClient.sendPost_json(workflowIp, workflowPort, workflowApp, "", params);
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 遥感融合产品
     *
     * @param fileDate
     * @return
     */
    public boolean sendPostFuseWorkFlowParams(String fileDate) {
        // 该类标识
        String code = "FUSE";
        boolean flag = true;

        QueryWrapper<WorkFlowBean> wrapper = null;
        wrapper = new QueryWrapper<WorkFlowBean>();
        wrapper.like("wf_description", code);
        List<WorkFlowBean> workFlowBeans = workFlowDao.selectList(wrapper);

        if (workFlowBeans == null) {
            flag = false;
        }
        QueryWrapper<ThresholdValueBean> queryWrapper = null;

        for (int i = 0; i < workFlowBeans.size(); i++) {
            try {
                queryWrapper = new QueryWrapper<ThresholdValueBean>();
                queryWrapper.eq("ele_type", workFlowBeans.get(i).getWfDescription().split("_")[1]);
                ThresholdValueBean thresholdValueBean = thresholdValueDao.selectOne(queryWrapper);

                JSONObject jsonObject = new JSONObject();
                if (thresholdValueBean != null) {
                    jsonObject = JSONObject.parseObject(thresholdValueBean.getValue());
                }
                jsonObject.put("date", fileDate);
                jsonObject.put("hour", "00");

                JSONObject params = new JSONObject();
                params.put("workflowId", workFlowBeans.get(i).getId());
                params.put("appId", workFlowBeans.get(i).getAppId());
                params.put("delay", 0);
                params.put("initParams", jsonObject.toJSONString());

                httpClient.sendPost_json(workflowIp, workflowPort, workflowApp, "", params);
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }


    /**
     * 调用海流统计
     *
     * @param year
     * @param season
     * @return
     */
    public boolean sendPostStatCurrentWorkFlowParams(String year, String season) {
        // 该类标识
        String code = "STAT_CURRENT";
        boolean flag = true;

        QueryWrapper<WorkFlowBean> wrapper = null;
        wrapper = new QueryWrapper<WorkFlowBean>();
        wrapper.like("wf_description", code);
        List<WorkFlowBean> workFlowBeans = workFlowDao.selectList(wrapper);

        if (workFlowBeans == null) {
            flag = false;
        }
        QueryWrapper<ThresholdValueBean> queryWrapper = null;

        for (int i = 0; i < workFlowBeans.size(); i++) {
            try {
                queryWrapper = new QueryWrapper<ThresholdValueBean>();
                queryWrapper.eq("ele_type", workFlowBeans.get(i).getWfDescription().split("_")[1]);
                ThresholdValueBean thresholdValueBean = thresholdValueDao.selectOne(queryWrapper);

                JSONObject jsonObject = new JSONObject();
                if (thresholdValueBean != null) {
                    jsonObject = JSONObject.parseObject(thresholdValueBean.getValue());
                }
                jsonObject.put("year", year);
                jsonObject.put("hour", season);

                JSONObject params = new JSONObject();
                params.put("workflowId", workFlowBeans.get(i).getId());
                params.put("appId", workFlowBeans.get(i).getAppId());
                params.put("delay", 0);
                params.put("initParams", jsonObject.toJSONString());

                httpClient.sendPost_json(workflowIp, workflowPort, workflowApp, "", params);
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * d调用 通用统计算法
     *
     * @param year
     * @param month
     * @return
     */
    public boolean sendPostStatWorkFlowParams(String year, String month) {
        // 该类标识
        String code = "STAT";
        boolean flag = true;

        QueryWrapper<WorkFlowBean> wrapper = null;
        wrapper = new QueryWrapper<WorkFlowBean>();
        wrapper.like("wf_description", code);
        // 海流统计是 季统计除外
        wrapper.ne("wf_description", "STAT_CURRENT");
        List<WorkFlowBean> workFlowBeans = workFlowDao.selectList(wrapper);

        if (workFlowBeans == null) {
            flag = false;
        }
        QueryWrapper<ThresholdValueBean> queryWrapper = null;

        for (int i = 0; i < workFlowBeans.size(); i++) {
            try {
                queryWrapper = new QueryWrapper<ThresholdValueBean>();
                queryWrapper.eq("ele_type", workFlowBeans.get(i).getWfDescription().split("_")[1]);
                ThresholdValueBean thresholdValueBean = thresholdValueDao.selectOne(queryWrapper);

                JSONObject jsonObject = new JSONObject();
                if (thresholdValueBean != null) {
                    jsonObject = JSONObject.parseObject(thresholdValueBean.getValue());
                }
                jsonObject.put("year", year);
                jsonObject.put("mon", month);

                JSONObject params = new JSONObject();
                params.put("workflowId", workFlowBeans.get(i).getId());
                params.put("appId", workFlowBeans.get(i).getAppId());
                params.put("delay", 0);
                params.put("initParams", jsonObject.toJSONString());

                httpClient.sendPost_json(workflowIp, workflowPort, workflowApp, "", params);
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }
}
