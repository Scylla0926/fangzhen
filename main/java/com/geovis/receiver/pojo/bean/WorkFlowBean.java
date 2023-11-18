package com.geovis.receiver.pojo.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("workflow_info")
public class WorkFlowBean {
    private Integer id;
    @TableField("app_id")
    private Long appId;
    @TableField("wf_name")
    private String wfName;
    @TableField("wf_description")
    private String wfDescription;
    private String extra;
    private String lifecycle;
    @TableField("max_wf_instance_num")
    private Integer maxWfInstanceNum;

    @TableField("next_trigger_time")
    private Long nextTriggerTime;
    @TableField("notify_user_ids")
    private String notifyUserIds;
    private String pedag;
    private Integer status;
    @TableField("time_expression")
    private String timeExpression;
    @TableField("time_expression_type")
    private Integer timeExpressionType;
    @TableField("gmt_create")
    private Date gmt_create;
    @TableField("gmt_modified")
    private Date gmt_modified;

}
