package org.example.xhc.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * cms_subject
 * @author xiaohongchao
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class subject implements Serializable {
    /**
     */
    private Long id;

    /**
     */
    private Long categoryId;

    /**
     */
    private String title;

    /**
     * 专题主图
     */
    private String pic;

    /**
     * 关联产品数量
     */
    private Integer productCnt;

    /**
     */
    private Integer recommendStatus;

    /**
     */
    private LocalDateTime createTime;

    /**
     */
    private Integer collectCnt;

    /**
     */
    private Integer readCnt;

    /**
     */
    private Integer commentCnt;

    /**
     * 画册图片用逗号分割
     */
    private String albumPics;

    /**
     */
    private String description;

    /**
     * 显示状态：0->不显示；1->显示
     */
    private Integer showStatus;

    /**
     * 转发数
     */
    private Integer forwardCnt;

    /**
     */
    private String content;

    private static final long serialVersionUID = 1L;
}