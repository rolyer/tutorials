package com.rolyer.springboothbase.entity;

/**
 * @ClassName: City
 * @Package com.rolyer.springboothbase.domain
 * @Description: ${TOTO} (用一句话描述该文件做什么)
 * @Author rolyer
 * @Date 2019/7/29 15:30
 * @Version V1.0
 */
public class City {
    private String name;
    private String code;
    private String parent;
    private Long created;
    private Long updated;

    public City() {
    }

    public City(String name, String code, String parent) {
        this.name = name;
        this.code = code;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }
}
