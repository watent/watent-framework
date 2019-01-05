package com.watent.framework.advisor;

import java.util.List;

/**
 * 切面注册
 */
public interface AdvisorRegistry {

    /**
     * 注册切面
     */
    void registerAdvisor(Advisor advisor);

    /**
     * 获取切面集合
     */
    List<Advisor> getAdvisors();

}
