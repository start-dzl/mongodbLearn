package com.dzl.mongodb.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * OA 组织机构表/ 由OA 的部门表 和 分部表组合而成
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaOrganization implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 机构ID
     */
    private Long id;

    /**
     * 原机构id
     */
    private String orgId;

    /**
     * 机构编码
     */
    private String orgCode;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 父机构编码
     */
    private Long parentId;

    /**
     * 父机构类型
     */
    private Integer parentType;

    /**
     * 机构id 和 类型的组合 唯一键
     */
    private String parentIdType;

    /**
     * 同级排序
     */
    private String orgSort;


    private Long companyId;

    /**
     * 是否为临时机构
     */
    private Integer isTemp;

    /**
     * 机构类型
     */
    private Integer orgType;

    /**
     * 机构id 和 类型的组合 唯一键
     */
    private String orgIdType;

    /**
     * 创建时间
     */

    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
